package ekol.crm.account.controller.lookup;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.account.domain.dto.CountryJson;
import ekol.crm.account.service.CountryCrudService;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/lookup/country")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CountryController {

    private CountryCrudService service;

    @GetMapping
    public List<CountryJson> list() {
        return service.list().stream().map(CountryJson::fromEntity).collect(Collectors.toList());
    }

    @GetMapping("/byIso")
    public CountryJson findByIso(@RequestParam String iso) {
        return service.findByIso(iso).map(CountryJson::fromEntity).orElseThrow(()->new ResourceNotFoundException("Country not found. iso: {0}", iso));
    }

    @GetMapping("/byName")
    public CountryJson findByName(@RequestParam String name) {
    	return service.findByName(name).map(CountryJson::fromEntity).orElseThrow(()->new ResourceNotFoundException("Country not found. name: {0}", name));
    }
}
