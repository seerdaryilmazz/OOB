package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.PolygonRegionToAdministrativeRegion;

import java.util.List;

public interface PolygonRegionToAdministrativeRegionRepository extends ApplicationRepository<PolygonRegionToAdministrativeRegion> {

    PolygonRegionToAdministrativeRegion findByPolygonRegion(String polygonRegion);

    PolygonRegionToAdministrativeRegion findByAdministrativeRegion(String administrativeRegion);

    List<PolygonRegionToAdministrativeRegion> findAllByAdministrativeRegionIn(List<String> administrativeRegionList);

    List<PolygonRegionToAdministrativeRegion> findAllByCountryIsoAlpha3Code(String countryIsoAlpha3Code);
}
