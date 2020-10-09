package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.RouteLegExpedition;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface RouteLegExpeditionRepository extends ApplicationRepository<RouteLegExpedition>, JpaSpecificationExecutor {

    List<RouteLegExpedition> findByKey(Integer key);

    List<RouteLegExpedition> findByLinehaulRouteLegIdAndDepartureDateTimeGreaterThanOrderByDepartureDateTimeAsc(Long routeLegId, LocalDateTime departure);

}
