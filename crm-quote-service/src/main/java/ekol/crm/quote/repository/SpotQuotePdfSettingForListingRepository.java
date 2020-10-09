package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.SpotQuotePdfSettingForListing;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface SpotQuotePdfSettingForListingRepository extends ApplicationRepository<SpotQuotePdfSettingForListing> {

    List<SpotQuotePdfSettingForListing> findAllByOrderByLanguageName();

}
