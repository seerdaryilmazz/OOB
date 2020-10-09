package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.RouteLegExpeditionTrip;

import java.util.List;

/**
 * Created by burak on 15/12/17.
 */
public interface
RouteLegExpeditionTripRepository extends ApplicationRepository<RouteLegExpeditionTrip> {

    public RouteLegExpeditionTrip findByExpeditionIdAndTripId(Long expeditionId, Long tripId);

    public List<RouteLegExpeditionTrip> findAllByExpeditionId(Long expeditionId);

    public int countByExpeditionId(Long expeditionId);
}
