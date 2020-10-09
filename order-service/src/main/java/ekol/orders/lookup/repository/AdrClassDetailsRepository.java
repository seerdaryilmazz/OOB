package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.lookup.domain.AdrClassDetails;
import ekol.orders.lookup.domain.AdrClassDetailsVersion;

import java.util.List;
import java.util.Optional;

public interface AdrClassDetailsRepository extends ApplicationRepository<AdrClassDetails> {

    List<AdrClassDetails> findByAdrVersionAndUnNumber(AdrClassDetailsVersion adrVersion, String unNumber);

    List<AdrClassDetails> findByAdrVersion(AdrClassDetailsVersion adrVersion);

    List<AdrClassDetails> findByIdIn(List<Long> ids);

    Optional<AdrClassDetails> findById(Long id);
}
