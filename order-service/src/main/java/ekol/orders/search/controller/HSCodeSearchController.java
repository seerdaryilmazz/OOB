package ekol.orders.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.orders.search.domain.HSCodeSearchDocument;
import ekol.orders.search.service.HSCodeIndexService;
import ekol.orders.search.service.HSCodeSearchService;

@RestController
@RequestMapping("/search/hscode")
public class HSCodeSearchController {

    private HSCodeSearchService searchService;
    private HSCodeIndexService hsCodeIndexingService;

    @Autowired
    public HSCodeSearchController(HSCodeSearchService searchService, HSCodeIndexService hsCodeIndexingService){
        this.searchService = searchService;
        this.hsCodeIndexingService = hsCodeIndexingService;
    }

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public void index() {
        hsCodeIndexingService.indexHSCodes();
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public Page<HSCodeSearchDocument> search(@RequestParam String q, @RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page) {
        if (size == null || page == null) {
            return searchService.searchHSCode(q);
        } else {
            return searchService.searchHSCode(q, page, size);

        }
    }

    @RequestMapping(value = {"/byCode"}, method = RequestMethod.GET)
    public HSCodeSearchDocument search(@RequestParam String code) {
        return searchService.findHSCode(code);
    }

}
