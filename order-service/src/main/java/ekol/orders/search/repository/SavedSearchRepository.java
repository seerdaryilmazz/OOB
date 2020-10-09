package ekol.orders.search.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.search.domain.SavedSearch;

import java.util.List;

public interface SavedSearchRepository extends ApplicationRepository<SavedSearch> {

    List<SavedSearch> findByUserId(Long userId);
}
