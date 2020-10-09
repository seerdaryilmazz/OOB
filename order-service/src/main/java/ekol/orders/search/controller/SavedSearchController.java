package ekol.orders.search.controller;

import ekol.orders.search.domain.SavedSearch;
import ekol.orders.search.service.SavedSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ozer on 24/10/16.
 */
@RestController
public class SavedSearchController {

    private SavedSearchService savedSearchService;

    @Autowired
    public SavedSearchController(SavedSearchService savedSearchService){
        this.savedSearchService = savedSearchService;
    }

    @RequestMapping(value = "/mySavedSearches", method = RequestMethod.GET)
    public List<SavedSearch> getMySavedSearches() {
        return savedSearchService.getMySavedSearches();
    }

    @RequestMapping(value = "/mySavedSearches/{id}", method = RequestMethod.GET)
    public SavedSearch getMySavedSearch(@PathVariable Long id) {
        return savedSearchService.getMySavedSearch(id);
    }

    @RequestMapping(value = "/mySavedSearches", method = RequestMethod.POST)
    public SavedSearch addMySavedSearch(@RequestBody SavedSearch savedSearch) {
        return savedSearchService.saveMySavedSearch(savedSearch);
    }

    @RequestMapping(value = "/mySavedSearches/{id}", method = RequestMethod.PUT)
    public void updateMySavedSearch(@PathVariable Long id, @RequestBody SavedSearch savedSearch) {
        savedSearch.setId(id);
        savedSearchService.saveMySavedSearch(savedSearch);
    }

    @RequestMapping(value = "/mySavedSearches/{id}", method = RequestMethod.DELETE)
    public void deleteMySavedSearch(@PathVariable Long id) {
        savedSearchService.deleteMySavedSearch(id);
    }
}
