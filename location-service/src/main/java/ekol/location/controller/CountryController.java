package ekol.location.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.location.domain.Country;
import ekol.location.service.CountryService;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 13/12/16.
 */
@RestController
@RequestMapping("/country")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CountryController {

    private CountryService countryService;

    @GetMapping(value = "")
    public Iterable<Country> findAll() {
        return countryService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Country find(@PathVariable Long id) {
        return countryService.findByIdOrThrowResourceNotFoundException(id);
    }

    @GetMapping(value = "/by-iso/{iso}")
    public Country findByIso(@PathVariable String iso) {
        return countryService.findByIsoOrThrowResourceNotFoundException(iso);
    }

    @Authorize(operations="location.country.manage")
    @PostMapping(value = {"", "/"})
    public Country create(@RequestBody Country country) {

        if (country != null && country.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return countryService.create(country);
    }

    @Authorize(operations="location.country.manage")
    @PutMapping(value = {"/{id}", "/{id}/"})
    public Country update(@PathVariable Long id, @RequestBody Country country) {

        if (!id.equals(country.getId())) {
            throw new BadRequestException("Country.id must be " + id + ".");
        }

        return countryService.update(country);
    }

    @Authorize(operations="location.country.manage")
    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public void delete(@PathVariable Long id) {
        countryService.softDelete(id);
    }
}
