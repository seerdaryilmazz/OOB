package ekol.kartoteks.controller;

import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.common.Country;
import ekol.kartoteks.repository.common.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by fatmaozyildirim on 3/17/16.
 */
@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Country> findAllWorkingWith() {
        return countryRepository.findByWorkingWithOrderByCountryName(true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Country get(@PathVariable Long id) {
        Country c = countryRepository.findOne(id);
        if (c == null) {
            throw new ResourceNotFoundException("Country with id {0} is not found", id);
        }
        return c;
    }

    @RequestMapping(value = "/iso/{iso}", method = RequestMethod.GET)
    public Country getByIso(@PathVariable String iso) {
        Country c = countryRepository.findByIsoIgnoreCase(iso);
        if (c == null) {
            throw new ResourceNotFoundException("Country with iso {0} is not found", iso);
        }
        return c;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Country> findAll() {
        return countryRepository.findAllByOrderByCountryName();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Country> search(@RequestParam String q) {
        return countryRepository.findByIsoStartingWithIgnoreCase(q);
    }
}