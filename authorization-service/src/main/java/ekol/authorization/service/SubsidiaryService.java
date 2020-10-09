package ekol.authorization.service;

import ekol.authorization.domain.Subsidiary;
import ekol.authorization.domain.auth.AuthSubsidiary;
import ekol.authorization.dto.Company;
import ekol.authorization.dto.Country;
import ekol.authorization.dto.IdNamePair;
import ekol.authorization.repository.SubsidiaryRepository;
import ekol.authorization.repository.auth.AuthSubsidiaryRepository;
import ekol.authorization.service.auth.AuthSubsidiaryService;
import ekol.authorization.util.Util;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class SubsidiaryService {

    @Autowired
    private SubsidiaryRepository subsidiaryRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AuthSubsidiaryService authSubsidiaryService;

    @Autowired
    private AuthSubsidiaryRepository authSubsidiaryRepository;

    public Subsidiary findByIdOrThrowResourceNotFoundException(Long id) {

        Subsidiary persistedEntity = subsidiaryRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Subsidiary with id {0} cannot be found.", id);
        }
    }

    public Subsidiary findByCompanyIdOrThrowResourceNotFoundException(Long companyId) {

        Subsidiary persistedEntity = null;

        // TODO: Subsidiary sayısı çok az olacağından aşağıdaki sorgu sorun çıkarmayacaktır belki ama kayıtların hepsini hafızaya almadan sorgulamak daha doğru olur.
        for (Subsidiary s : subsidiaryRepository.findAll()) {
            if (Util.getIds(s.getCompanies()).contains(companyId)) {
                persistedEntity = s;
                break;
            }
        }

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Subsidiary of company with id {0} cannot be found.", companyId);
        }
    }

    @Transactional
    public Subsidiary createOrUpdate(Subsidiary subsidiary) {

        boolean isCreation = true;

        if (subsidiary == null) {
            throw new BadRequestException("A subsidiary must be specified.");
        }

        if (subsidiary.getId() != null) {
            findByIdOrThrowResourceNotFoundException(subsidiary.getId());
            isCreation = false;
        }

        if (StringUtils.isBlank(subsidiary.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        subsidiary.setName(subsidiary.getName().trim().toUpperCase());

        Subsidiary queryResult = subsidiaryRepository.findByName(subsidiary.getName());

        if (queryResult != null) {
            if (isCreation || !queryResult.getId().equals(subsidiary.getId())) {
                throw new BadRequestException("There cannot be two subsidiaries with same name.");
            }
        }

        // Subsidiary sayısı çok az olacağından ve subsidiary oluşturma işlemi çok sık yapılmayacağından, aşağıdaki sorgu sorun çıkarmayacaktır.
        Iterable<Subsidiary> otherSubsidiaries = isCreation ? subsidiaryRepository.findAll() : subsidiaryRepository.findAllByIdNot(subsidiary.getId());

        checkCompanies(subsidiary, otherSubsidiaries);

        Subsidiary persistedEntity = subsidiaryRepository.save(subsidiary);

        AuthSubsidiary authSubsidiary;

        if (isCreation) {
            // İki ayrı veritabanı söz konusu olduğundan yarım kalmış bir işlemden dolayı Graph DB'de kayıt kalmış olabilir.
            // Örnek: Kayıt Graph DB'de yaratıldı, Relational DB'de yaratılamadı (örneğin commit aşamasında uygulama ve Relational DB arasındaki bağlantı gitti)
            // ve aynı isimle tekrar kayıt yaratılmaya çalışılıyor.
            authSubsidiary = authSubsidiaryRepository.findByName(persistedEntity.getName());
            if (authSubsidiary != null) {
                authSubsidiary.setExternalId(persistedEntity.getId());
            } else {
                authSubsidiary = new AuthSubsidiary();
                authSubsidiary.setName(persistedEntity.getName());
                authSubsidiary.setExternalId(persistedEntity.getId());
            }
        } else {
            authSubsidiary = authSubsidiaryRepository.findByExternalId(persistedEntity.getId());
            authSubsidiary.setName(persistedEntity.getName());
        }

        authSubsidiaryService.createOrUpdate(authSubsidiary);

        return persistedEntity;
    }

    private void checkCompanies(Subsidiary subsidiary, Iterable<Subsidiary> otherSubsidiaries) {

        Set<Long> companyIds = new HashSet<>();

        if (subsidiary.getCompanies() != null) {
            for (IdNamePair company : subsidiary.getCompanies()) {
                if (company == null || company.getId() == null) {
                    throw new BadRequestException("A company must be specified.");
                }
                companyIds.add(company.getId());
            }
        }

        if (companyIds.size() == 0) {
            throw new BadRequestException("At least one company must be specified.");
        }

        Set<IdNamePair> companies = new HashSet<>();
        Country country = null;

        for (Long companyId : companyIds) {
            Company company = companyService.findByIdOrThrowResourceNotFoundException(companyId);
            if (country == null) {
                country = company.getCountry();
            } else {
                if (!country.getIso().equals(company.getCountry().getIso())) {
                    throw new BadRequestException("All companies of a subsidiary must be in the same country.");
                }
            }
            companies.add(new IdNamePair(company.getId(), company.getName()));
        }

        subsidiary.setCompanies(companies);

        if (subsidiary.getDefaultInvoiceCompany() == null || subsidiary.getDefaultInvoiceCompany().getId() == null) {
            throw new BadRequestException("A default invoice company must be specified.");
        }

        if (!companyIds.contains(subsidiary.getDefaultInvoiceCompany().getId())) {
            throw new BadRequestException("Default invoice company must be one of the companies of subsidiary.");
        }

        subsidiary.setDefaultInvoiceCompany(companyService.findIdNamePairByIdOrThrowResourceNotFoundException(subsidiary.getDefaultInvoiceCompany().getId()));

        for (Subsidiary s : otherSubsidiaries) {
            Collection<Long> intersection = CollectionUtils.intersection(companyIds, Util.getIds(s.getCompanies()));
            if (intersection.size() > 0) {
                if (intersection.size() == 1) {
                    throw new BadRequestException("Company with id {0} is a company of another subsidiary ({1}).", intersection.iterator().next(), s.getName());
                } else {
                    throw new BadRequestException("Companies with ids {0} are companies of another subsidiary ({1}).", StringUtils.join(intersection, ", "), s.getName());
                }
            }
        }
    }

    @Transactional
    public void softDelete(Long id) {
        Subsidiary persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        authSubsidiaryService.delete(authSubsidiaryRepository.findByExternalId(persistedEntity.getId()).getId());
        persistedEntity.setDeleted(true);
        subsidiaryRepository.save(persistedEntity);
    }
}
