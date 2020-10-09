package ekol.kartoteks.controller;

import java.util.Map;

import ekol.kartoteks.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.kartoteks.domain.search.CompanySearchDoc;
import ekol.kartoteks.service.*;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 11/01/2017.
 */
@RestController
@RequestMapping("/search")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SearchController {

    private CompanyRepository companyRepository;
    private CompanyIndexingService companyIndexingService;
    private SearchService searchService;

    @Authorize(operations = "kartoteks.company-search.index")
    @GetMapping("/index")
    public void index(){
        companyIndexingService.indexImportedCompanyData();
    }

    @GetMapping
    public Page<CompanySearchDoc> searchPrefix(@RequestParam String q,
                                               @RequestParam(required = false) Boolean shortNameChecked,
                                               @RequestParam(required = false) Integer size,
                                               @RequestParam(required = false) Integer page){
        return searchService.searchCompanyPrefix(q, shortNameChecked, page, size);
    }

    @GetMapping("/query")
    public Page<CompanySearchDoc> searchTerm(@RequestParam String q,
                                         @RequestParam(required = false) Integer size,
                                         @RequestParam(required = false) Integer page){
        return searchService.searchCompanyMatchWithAnd(q, page, size);
    }

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/more-like-this")
    public Page<CompanySearchDoc> moreLikeThis(@RequestParam String q, @RequestParam(required = false) String taxId,
                                               @RequestParam(required = false) String taxofficeCode,
                                               @RequestParam int size, @RequestParam int page){
        return searchService.moreLikeThis(q, taxId, taxofficeCode, page, size);
    }

    @GetMapping("/aggregate-country")
    public Map<String, Long> aggregateCountry(){
        return searchService.aggregateCountry();
    }

    @GetMapping("/by-country")
    public Page<CompanySearchDoc> searchCountry(@RequestParam String country,
                                           @RequestParam(required = false) Integer size,
                                           @RequestParam(required = false) Integer page){
        return searchService.searchByCountry(country, page, size);
    }

    @GetMapping("/by-city-and-district")
    public Page<Long> searchCompanyIdsByCityAndDistrict(
            @RequestParam String city,
            @RequestParam(required = false) String district,
            @RequestParam boolean searchOnlyInDefaultLocations,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer page) {
        return searchService.searchCompanyIdsByCityAndDistrict(city, district, searchOnlyInDefaultLocations, page, size);
    }

    @Authorize(operations = "kartoteks.company-search.index")
    @GetMapping("/index/{id}")
    public String indexById(@PathVariable Long id){
        companyIndexingService.indexCompany(companyRepository.findById(id));
        return "Company with id: "+ id +" is indexed successfully";
    }

}

