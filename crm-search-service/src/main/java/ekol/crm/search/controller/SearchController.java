package ekol.crm.search.controller;

import javax.validation.Valid;

import ekol.crm.search.domain.opportunity.OpportunitySearchDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ekol.crm.search.config.SearchConfig;
import ekol.crm.search.domain.*;
import ekol.crm.search.domain.account.AccountSearchDoc;
import ekol.crm.search.domain.dto.*;
import ekol.crm.search.domain.enumeration.QuoteType;
import ekol.crm.search.domain.quote.QuoteSearchDoc;
import ekol.crm.search.service.SearchService;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;


@Validated
@RestController
@RequestMapping("/search")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SearchController {

    private SearchService searchService;

    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/query")
    public Page<SearchDoc> query(@Valid Query query){
        return searchService.searchDocumentByMatchWithAnd(query);
    }

    @GetMapping("/account/prefix")
    public Page<AccountSearchDoc> searchAccountsByTerm(@Valid Query query) {
        return searchService.searchAccountsByTerm(query);
    }
    
    @GetMapping("/more-like-this")
    public Page<AccountSearchDoc> moreLikeThis(@RequestParam String q, @RequestParam int size, @RequestParam int page){
        return searchService.moreLikeThis(q, page, size);
    }

    @PostMapping("/{documents}/query")
    public Page<SearchDoc> searchDocument(@RequestBody SearchConfig searchConfig, @PathVariable DocumentType[] documents){
        return searchService.searchQuoteAsPage(searchConfig, documents);
    }
    
    @PostMapping("/{documents}/query-list")
    public Iterable<SearchDoc> searchDocumentAsList(@RequestBody SearchConfig searchConfig, @PathVariable DocumentType[] documents){
    	return searchService.searchQuoteAsList(searchConfig, documents);
    }

    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/quoteHomePageStatistics")
    public QuoteHomePageStatistics getQuoteHomePageStatistics(@RequestParam QuoteType quoteType) {
        return searchService.getQuoteHomePageStatistics(quoteType);
    }
    
    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/accountHomePageStatistics")
    public AccountHomePageStatistics getAccountHomePageStatistics() {
    	return searchService.getAccountHomePageStatistics();
    }

    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/searchQuotesForHomePage")
    public Page<QuoteSearchDoc> searchQuotesForHomePage(@RequestBody QuoteSearchJson search) {
        return searchService.searchQuotes(search,true);
    }
    
    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/searchAccountsForHomePage")
    public Page<AccountSearchDoc> searchAccountsForHomePage(@RequestBody SearchConfig searchConfig) {
        return searchService.searchAccountsForHomePage(searchConfig);
    }
    
    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/searchQuotes")
    public Page<QuoteSearchDoc> searchQuotes(@RequestBody QuoteSearchJson search) {
    	return searchService.searchQuotes(search,false);
    }

    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/searchAccounts")
    public Page<AccountSearchDoc> searchQuotes(@RequestBody AccountSearchJson search) {
        return searchService.searchAccounts(search);
    }

    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/searchOpportunitiesForHomePage")
    public Page<OpportunitySearchDoc> searchOpportunitiesForHomePage(@RequestBody SearchConfig searchConfig) {
        return searchService.searchOpportunitiesForHomePage(searchConfig);
    }

    @Timed(value = "crm-search.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/opportunityHomePageStatistics")
    public OpportunityHomePageStatistics getOpportunityHomePageStatistic() {
        return searchService.getOpportunityHomePageStatistic();
    }
}

