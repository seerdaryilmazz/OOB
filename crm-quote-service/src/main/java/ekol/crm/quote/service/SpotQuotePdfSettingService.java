package ekol.crm.quote.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.client.*;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.authorizationservice.Subsidiary;
import ekol.crm.quote.domain.model.SpotQuotePdfSetting;
import ekol.crm.quote.repository.SpotQuotePdfSettingRepository;
import ekol.exceptions.*;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SpotQuotePdfSettingService {

    public static final int MAX_HTML_TEXT_LENGTH = 50000;

    private SpotQuotePdfSettingRepository repository;
    private AuthorizationService authorizationService;
    private TranslatorService translatorService;

    public SpotQuotePdfSetting findByIdOrThrowResourceNotFoundException(Long id) {
        Optional<SpotQuotePdfSetting> optional = repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("Spot quote pdf setting not found. id: {0}", id);
        }
    }

    @Transactional
    public SpotQuotePdfSetting create(SpotQuotePdfSettingJson requestDataJson) {
        return createOrUpdate(null, requestDataJson);
    }

    @Transactional
    public SpotQuotePdfSetting update(Long id, SpotQuotePdfSettingJson requestDataJson) {
        return createOrUpdate(id, requestDataJson);
    }

    private SpotQuotePdfSetting createOrUpdate(Long id, SpotQuotePdfSettingJson requestDataJson) {

        if (requestDataJson == null) {
            throw new BadRequestException("A request data must be specified.");
        }

        SpotQuotePdfSetting requestData = requestDataJson.toEntity();
        SpotQuotePdfSetting entity;

        if (id != null) {
            entity = findByIdOrThrowResourceNotFoundException(id);
        } else {
            entity = new SpotQuotePdfSetting();
        }

        if (requestData.getSubsidiary() == null || requestData.getSubsidiary().getId() == null) {
            throw new BadRequestException("A subsidiary must be specified.");
        }

        Subsidiary subsidiary = authorizationService.findSubsidiaryById(requestData.getSubsidiary().getId(), false);

        if (requestData.getServiceArea() == null) {
            throw new BadRequestException("A service area must be specified.");
        }

        if (requestData.getLanguage() == null || requestData.getLanguage().getId() == null) {
            throw new BadRequestException("A language must be specified.");
        }

        SupportedLocale language = translatorService.findLocaleOrThrowResourceNotFoundException(requestData.getLanguage().getId());

        if (requestData.getAboutCompany() != null && requestData.getAboutCompany().length() > MAX_HTML_TEXT_LENGTH) {
            throw new BadRequestException("About company text can have maximum {0} characters.", MAX_HTML_TEXT_LENGTH);
        }

        if (requestData.getImportGeneralConditions() != null && requestData.getImportGeneralConditions().length() > MAX_HTML_TEXT_LENGTH) {
            throw new BadRequestException("Import general conditions text can have maximum {0} characters.", MAX_HTML_TEXT_LENGTH);
        }

        if (requestData.getExportGeneralConditions() != null && requestData.getExportGeneralConditions().length() > MAX_HTML_TEXT_LENGTH) {
            throw new BadRequestException("Export general conditions text can have maximum {0} characters.", MAX_HTML_TEXT_LENGTH);
        }

        if (id != null) {
            if (repository.countBySubsidiaryIdAndServiceAreaAndLanguageIdAndIdNot(subsidiary.getId(), requestData.getServiceArea(), language.getId(), id) > 0) {
                throw new BadRequestException("There is already a setting for specified subsidiary, service area and language.");
            }
        } else {
            if (repository.countBySubsidiaryIdAndServiceAreaAndLanguageId(subsidiary.getId(), requestData.getServiceArea(), language.getId()) > 0) {
                throw new BadRequestException("There is already a setting for specified subsidiary, service area and language.");
            }
        }

        entity.setSubsidiary(new IdNamePair(subsidiary.getId(), subsidiary.getName()));
        entity.setServiceArea(requestData.getServiceArea());
        entity.setLanguage(language);
        entity.setAboutCompany(requestData.getAboutCompany());
        entity.setImportGeneralConditions(requestData.getImportGeneralConditions());
        entity.setExportGeneralConditions(requestData.getExportGeneralConditions());

        return repository.save(entity);
    }

    @Transactional
    public SpotQuotePdfSetting delete(Long id) {
        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }
        SpotQuotePdfSetting persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        return repository.save(persistedEntity);
    }
}
