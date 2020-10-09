package ekol.crm.account.service;

import ekol.crm.account.domain.enumaration.CountryPointType;
import ekol.crm.account.domain.model.CountryPoint;
import ekol.crm.account.repository.CountryPointRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CountryPointCrudService {

    private CountryPointRepository repository;
    
    public CountryPoint getById(Long id) {
    	return repository.findOne(id);
    }

    public List<CountryPoint> getByCountryAndType(String iso, CountryPointType type){
        return repository.findByCountryIsoAndTypeOrderByName(iso, type);
    }

    public Optional<CountryPoint> getByCountryAndTypeAndCode(String iso, CountryPointType type, String code) {
        return repository.findByCountryIsoAndTypeAndCode(iso, type, code);
    }

    public Optional<CountryPoint> getByCountryAndTypeAndName(String iso, CountryPointType type, String name) {
        return repository.findByCountryIsoAndTypeAndNameIgnoreCase(iso, type, name);
    }
    
    public Optional<CountryPoint> getByCountryNameAndTypeAndCode(String countryName, CountryPointType type, String code) {
    	return repository.findByCountryNameIgnoreCaseAndTypeAndCode(countryName, type, code);
    }
    
    public Optional<CountryPoint> getByCountryNameAndTypeAndName(String countryName, CountryPointType type, String name) {
    	return repository.findByCountryNameIgnoreCaseAndTypeAndNameIgnoreCase(countryName, type, name);
    }
    
    public List<CountryPoint> getAllPointsByType(CountryPointType type){
    	return repository.findAllByType(type);
    }
}



