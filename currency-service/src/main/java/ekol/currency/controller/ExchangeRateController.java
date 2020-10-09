package ekol.currency.controller;

import ekol.currency.domain.ExchangeRate;
import ekol.currency.domain.ExchangeRatePublisher;
import ekol.currency.domain.dto.ExchangeRateForUI;
import ekol.currency.domain.dto.PagedDataHolder;
import ekol.currency.repository.ExchangeRateRepository;
import ekol.currency.service.ExchangeRateService;
import ekol.currency.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/exchange-rate")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public void create(
            @RequestParam(value = "publisherCode") String publisherCode,
            @RequestParam(value = "publishDate") String publishDateStr) {

        ExchangeRatePublisher publisher = ExchangeRatePublisher.valueOf(publisherCode);
        LocalDate publishDate = LocalDate.parse(publishDateStr, Constants.DEFAULT_DATE_FORMATTER);

        exchangeRateService.getExchangeRatesAndSave(publisher, publishDate);
    }

    @RequestMapping(value = "/publish-dates", method = RequestMethod.GET)
    public PagedDataHolder<LocalDate> findDistinctPublishDatesForPublisher(
            @RequestParam(value = "pageNumber") Integer pageNumber,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "publisherCode") String publisherCode) {

        ExchangeRatePublisher publisher = ExchangeRatePublisher.valueOf(publisherCode);

        List<LocalDate> currentPageContent = exchangeRateRepository.findDistinctPublishDatesByPublisher(publisher, new PageRequest(pageNumber, pageSize));

        long numberOfElements = exchangeRateRepository.countDistinctPublishDatesByPublisher(publisher);

        return new PagedDataHolder(currentPageContent, numberOfElements);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public PagedDataHolder<ExchangeRateForUI> findAll(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "publisherCode", required = false) String publisherCode,
            @RequestParam(value = "publishDate", required = false) String publishDateStr,
            @RequestParam(value = "fromCurrencyCode", required = false) String fromCurrencyCode,
            @RequestParam(value = "toCurrencyCode", required = false) String toCurrencyCode) {

        return exchangeRateService.findAllByGivenParams(
                pageNumber == null ? Constants.FIRST_PAGE_NUMBER : pageNumber,
                pageSize == null ? Constants.DEFAULT_PAGE_SIZE : pageSize,
                publisherCode, publishDateStr, fromCurrencyCode, toCurrencyCode);
    }

    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public BigDecimal convert(
            @RequestParam(value = "publisherCode") String publisherCode,
            @RequestParam(value = "conversionDate") String conversionDateStr,
            @RequestParam(value = "fromCurrencyCode") String fromCurrencyCode,
            @RequestParam(value = "toCurrencyCode") String toCurrencyCode,
            @RequestParam(value = "crossRateCurrencyCode", required = false) String crossRateCurrencyCode,
            @RequestParam(value = "preferredValueCode", required = false) String preferredValueCode) {

        System.out.println("publisherCode: " + publisherCode);
        System.out.println("conversionDateStr: " + conversionDateStr);
        System.out.println("fromCurrencyCode: " + fromCurrencyCode);
        System.out.println("toCurrencyCode: " + toCurrencyCode);
        System.out.println("crossRateCurrencyCode: " + crossRateCurrencyCode);
        System.out.println("preferredValueCode: " + preferredValueCode);

        return exchangeRateService.convert(publisherCode, conversionDateStr, fromCurrencyCode, toCurrencyCode, crossRateCurrencyCode, preferredValueCode);
    }
}
