package ekol.authorization.service.auth;

import ekol.authorization.domain.auth.AuthSubsidiary;
import ekol.authorization.repository.auth.AuthSubsidiaryRepository;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthSubsidiaryService {

    @Autowired
    private AuthSubsidiaryRepository authSubsidiaryRepository;

    public AuthSubsidiary findByIdOrThrowResourceNotFoundException(Long id) {

        AuthSubsidiary persistedEntity = authSubsidiaryRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Subsidiary with id {0} cannot be found.", id);
        }
    }

    @Transactional
    public AuthSubsidiary createOrUpdate(AuthSubsidiary subsidiary) {

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

        AuthSubsidiary queryResult = authSubsidiaryRepository.findByName(subsidiary.getName());

        if (queryResult != null) {
            if (isCreation || !queryResult.getId().equals(subsidiary.getId())) {
                throw new BadRequestException("There cannot be two subsidiaries with same name.");
            }
        }

        if (subsidiary.getExternalId() == null) {
            throw new BadRequestException("An external id must be specified.");
        }

        AuthSubsidiary queryResult2 = authSubsidiaryRepository.findByExternalId(subsidiary.getExternalId());

        if (queryResult2 != null) {
            if (isCreation || !queryResult2.getId().equals(subsidiary.getId())) {
                throw new BadRequestException("There cannot be two subsidiaries with same external id.");
            }
        }

        return authSubsidiaryRepository.save(subsidiary);
    }

    @Transactional
    public void delete(Long id) {

        AuthSubsidiary persistedEntity = findByIdOrThrowResourceNotFoundException(id);

        long relationCount = authSubsidiaryRepository.countRelations(persistedEntity.getId());

        if (relationCount == 0) {
            authSubsidiaryRepository.delete(id);
        } else {
            throw new BadRequestException("Specified subsidiary is being used, it cannot be deleted.");
        }
    }
}
