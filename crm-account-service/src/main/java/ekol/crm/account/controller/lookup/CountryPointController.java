package ekol.crm.account.controller.lookup;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.account.domain.dto.CountryPointJson;
import ekol.crm.account.domain.enumaration.CountryPointType;
import ekol.crm.account.domain.model.CountryPoint;
import ekol.crm.account.service.CountryPointCrudService;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/lookup/country-point")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CountryPointController {

    private CountryPointCrudService service;
    
    @GetMapping("/{id}")
    public CountryPoint getById(@PathVariable Long id) {
    	return service.getById(id);
    }

    @GetMapping("/byCountry/{iso}")
    public List<CountryPointJson> list(@PathVariable String iso, @RequestParam CountryPointType type) {
        return service.getByCountryAndType(iso, type).stream().map(CountryPointJson::fromEntity).collect(Collectors.toList());
    }
    
    @GetMapping("/listByType")
    public List <CountryPointJson> listAllByType(@RequestParam CountryPointType type){
    	return service.getAllPointsByType(type).stream().map(CountryPointJson::fromEntity).collect(Collectors.toList());
    }

    @GetMapping("/byCountryAndTypeAndCode")
    public CountryPointJson findByCountryAndTypeAndCode(@RequestParam String iso, @RequestParam CountryPointType type, @RequestParam String code) {
        return service.getByCountryAndTypeAndCode(iso, type, code).map(CountryPointJson::fromEntity).orElseThrow(()->new ResourceNotFoundException("Country point not found. country: {0}, type: {1}, code: {2}", iso, type.name(), code));
    }

    @GetMapping("/byCountryAndTypeAndName")
    public CountryPointJson findByCountryAndTypeAndName(@RequestParam String iso, @RequestParam CountryPointType type, @RequestParam String name) {
    	return service.getByCountryAndTypeAndName(iso, type, name).map(CountryPointJson::fromEntity).orElseThrow(()->new ResourceNotFoundException("Country point not found. country: {0}, type: {1}, name: {2}", iso, type.name(), name));
    }
    
    @GetMapping("/byCountryNameAndTypeAndCode")
    public CountryPointJson findByCountryNameAndTypeAndCode(@RequestParam String countryName, @RequestParam CountryPointType type, @RequestParam String code) {
    	return service.getByCountryNameAndTypeAndCode(countryName, type, code).map(CountryPointJson::fromEntity).orElseThrow(()->new ResourceNotFoundException("Country point not found. country: {0}, type: {1}, code: {2}", countryName, type.name(), code));
    }
    
    @GetMapping("/byCountryNameAndTypeAndName")
    public CountryPointJson findByCountryNameAndTypeAndName(@RequestParam String countryName, @RequestParam CountryPointType type, @RequestParam String name) {
    	return service.getByCountryNameAndTypeAndName(countryName, type, name).map(CountryPointJson::fromEntity).orElseThrow(()->new ResourceNotFoundException("Country point not found. country: {0}, type: {1}, name: {2}", countryName, type.name(), name));
    }
}
