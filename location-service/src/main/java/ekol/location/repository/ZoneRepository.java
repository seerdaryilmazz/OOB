package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.Zone;
import ekol.location.domain.ZoneType;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

/**
 * Created by ozer on 13/12/16.
 */
public interface ZoneRepository extends ApplicationRepository<Zone> {

    @EntityGraph(value = "Zone.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Zone findWithDetailsById(Long id);

    @EntityGraph(value = "Zone.withType", type = EntityGraph.EntityGraphType.LOAD)
    List<Zone> findAllByOrderByNameAsc();

    List<Zone> findByZoneTypeOrderByNameAsc(ZoneType zoneType);

    @EntityGraph(value = "Zone.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    Zone getByZoneTypeCodeAndPolygonRegionsIdIn(String zoneTypeCode, List<Long> polygonRegionIds);

    @EntityGraph(value = "Zone.coordinateSearch", type = EntityGraph.EntityGraphType.LOAD)
    List<Zone> findAllForCoordinateSearchDistinctByZoneTypeCode(String zoneTypeCode);

}
