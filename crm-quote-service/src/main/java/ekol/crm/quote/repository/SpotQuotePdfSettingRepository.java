package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.SpotQuotePdfSetting;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.Optional;

public interface SpotQuotePdfSettingRepository extends ApplicationRepository<SpotQuotePdfSetting> {

    long countBySubsidiaryIdAndServiceAreaAndLanguageId(Long subsidiaryId, String serviceArea, Long languageId);

    long countBySubsidiaryIdAndServiceAreaAndLanguageIdAndIdNot(Long subsidiaryId, String serviceArea, Long languageId, Long id);

    Optional<SpotQuotePdfSetting> findById(Long id);

    Optional<SpotQuotePdfSetting> findBySubsidiaryIdAndServiceAreaAndLanguageId(Long subsidiaryId, String serviceArea, Long languageId);

}
