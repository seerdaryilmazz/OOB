package ekol.orders.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.orders.search.domain.HSCodeExtendedSearchDocument;
import ekol.orders.search.service.HSCodeExtendedIndexService;
import ekol.orders.search.service.HSCodeExtendedSearchService;

@RestController
@RequestMapping("/search/hscode-extended")
public class HSCodeExtendedSearchController {

	@Autowired
    private HSCodeExtendedSearchService extendedSearchService;

	@Autowired
    private HSCodeExtendedIndexService hsCodeExtendedIndexingService;

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public void index() {
    	hsCodeExtendedIndexingService.indexHSCodes();
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public Page<HSCodeExtendedSearchDocument> search(@RequestParam String q, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page) {
        if (size == null || page == null) {
            return extendedSearchService.searchHSCode(q);
        } else {
            return extendedSearchService.searchHSCode(q, page, size);

        }
    }
}
