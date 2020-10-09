package ekol.crm.account.repository;

import ekol.crm.account.domain.enumaration.CountryPointType;
import ekol.crm.account.domain.model.CountryPoint;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface CountryPointRepository extends ApplicationRepository<CountryPoint> {

    Optional<CountryPoint> findById(Long id);
    
    Optional<CountryPoint> findByCountryIsoAndTypeAndCode(String countryIso, CountryPointType type, String code);
    Optional<CountryPoint> findByCountryNameIgnoreCaseAndTypeAndCode(String countryName, CountryPointType type, String code);
    
    Optional<CountryPoint> findByCountryIsoAndTypeAndNameIgnoreCase(String countryIso, CountryPointType type, String name);
    Optional<CountryPoint> findByCountryNameIgnoreCaseAndTypeAndNameIgnoreCase(String countryName, CountryPointType type, String name);
    
    List<CountryPoint> findByCountryIsoAndTypeOrderByName(String countryIso, CountryPointType type);
    
    List<CountryPoint> findAllByType(CountryPointType type);

}
