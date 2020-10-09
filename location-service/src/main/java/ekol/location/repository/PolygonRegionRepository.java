package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.PolygonRegion;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface PolygonRegionRepository extends ApplicationRepository<PolygonRegion> {

    List<PolygonRegion> findAllByCountryIsoAlpha3CodeAndLevel(String countryIsoAlpha3Code, Integer level);

    List<PolygonRegion> findAllByCountryIsoAlpha3CodeAndNameAndLevel(String countryIsoAlpha3Code, String name, Integer level);

    List<PolygonRegion> findAllByLevel(Integer level);

    @EntityGraph(value = "PolygonRegion.coordinateSearch", type = EntityGraph.EntityGraphType.LOAD)
    List<PolygonRegion> findAllForCoordinateSearchDistinctByLevel(Integer level);

    List<PolygonRegion> findAllByParent(String parent);

    @EntityGraph(value = "PolygonRegion.coordinateSearch", type = EntityGraph.EntityGraphType.LOAD)
    List<PolygonRegion> findAllForCoordinateSearchDistinctByParent(String parent);

    PolygonRegion findByParentAndName(String parent, String name);

    @EntityGraph(value = "PolygonRegion.drawing", type = EntityGraph.EntityGraphType.LOAD)
    List<PolygonRegion> findAllForDrawingDistinctByCountryIsoAlpha3CodeAndLevel(String countryIsoAlpha3Code, Integer level);

    @EntityGraph(value = "PolygonRegion.drawing", type = EntityGraph.EntityGraphType.LOAD)
    List<PolygonRegion> findAllForDrawingDistinctByParent(String parent);

    @EntityGraph(value = "PolygonRegion.drawing", type = EntityGraph.EntityGraphType.LOAD)
    PolygonRegion findForDrawingDistinctByParentAndName(String parent, String name);

    @EntityGraph(value = "PolygonRegion.drawing", type = EntityGraph.EntityGraphType.LOAD)
    List<PolygonRegion> findAllForDrawingDistinctByIdIn(List<Long> ids);

    @EntityGraph(value = "PolygonRegion.drawing", type = EntityGraph.EntityGraphType.LOAD)
    PolygonRegion findForDrawingById(Long id);

    List<PolygonRegion> findAllByIdIn(List<Long> ids);

}
