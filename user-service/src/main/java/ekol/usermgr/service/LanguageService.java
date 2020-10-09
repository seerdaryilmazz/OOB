package ekol.usermgr.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.usermgr.domain.Language;
import ekol.usermgr.repository.LanguageRepository;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public Language findByIdOrThrowResourceNotFoundException(Long id) {

        Language persistedEntity = languageRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Language with specified id cannot be found: " + id);
        }
    }

    public Language findByIsoCodeOrThrowResourceNotFoundException(String isoCode) {

        Language persistedEntity = languageRepository.findByIsoCodeIgnoreCase(isoCode);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Language with specified isoCode cannot be found: " + isoCode);
        }
    }

    @Transactional
    public Language createOrUpdate(Language language) {

        if (language == null) {
            throw new BadRequestException("Language cannot be null.");
        }
        if (language.getId() != null) {
            findByIdOrThrowResourceNotFoundException(language.getId());
        }

        if (StringUtils.isBlank(language.getName())) {
            throw new BadRequestException("Language.name must contain at least 1 char.");
        }

        language.setName(language.getName().trim());

        if (StringUtils.isBlank(language.getIsoCode()) || language.getIsoCode().length() != 2) {
            throw new BadRequestException("Language.isoCode must contain exactly 2 chars.");
        }

        language.setIsoCode(language.getName().trim().toLowerCase());

        return languageRepository.save(language);
    }

    @Transactional
    public void softDelete(Long id) {
        Language persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        languageRepository.save(persistedEntity);
    }
}
