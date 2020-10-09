package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.ZonePolygonRegion;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

/**
 * Created by ozer on 15/12/16.
 */
public interface ZonePolygonRegionRepository extends ApplicationRepository<ZonePolygonRegion> {

    @EntityGraph(value = "ZonePolygonRegion.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<ZonePolygonRegion> findByZoneId(Long zoneId);

    List<ZonePolygonRegion> findBySelected(boolean selected);

}
