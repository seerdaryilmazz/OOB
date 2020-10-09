package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.AdministrativeRegionPostcode;

import java.util.List;

public interface AdministrativeRegionPostcodeRepository extends ApplicationRepository<AdministrativeRegionPostcode> {

    List<AdministrativeRegionPostcode> findAllByRegion_CountryIsoAlpha2CodeAndPostcode(String regionCountryIsoAlpha2Code, String postcode);

    List<AdministrativeRegionPostcode> findAllByRegion_CountryIsoAlpha2CodeAndPostcodeLike(String regionCountryIsoAlpha2Code, String postcode);

}
