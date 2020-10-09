package ekol.crm.quote.controller;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;

import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ekol.crm.quote.client.*;
import ekol.crm.quote.domain.EmailJson;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.dto.quote.QuoteJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.enumaration.Currency;
import ekol.crm.quote.domain.model.BillingItem;
import ekol.crm.quote.domain.model.OwnerCompany;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.event.*;
import ekol.crm.quote.service.*;
import ekol.crm.quote.util.Utils;
import ekol.crm.quote.validator.CreateSpotQuoteValidator;
import ekol.event.auth.Authorize;
import ekol.exceptions.*;
import ekol.model.CodeNamePair;
import ekol.notification.annotation.SendNotification;
import ekol.notification.dto.TargetType;
import ekol.resource.validation.OneOrderValidation;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/quote")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
class QuoteController {

    private CalculationService calculationService;
    private PriceCalculationService priceCalculationService;
    private KartoteksService kartoteksService;
    private ExchangeRateService exchangeRateService;
    private ApplicationEventPublisher publisher;
    private QuoteService quoteService;
    private BundledProductExcelService bundledProductExcelService;
    private SpotQuoteService spotQuoteService;
    private OwnerCompanyService ownerCompanyService;

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations={"crm-quote.create", "crm-quote.price-admin"})
    @PostMapping
    @SendNotification(concern = "QUOTE_PRICE_REQUEST", targetType = TargetType.AUTH_OPERATION, target = "crm-quote.price-admin", beforeCondition = "@spotQuoteNotificationConditon.isPriceRequestedBefore(args[0])")
    @OneOrderValidation(CreateSpotQuoteValidator.class)
    public QuoteJson createQuote(@RequestBody QuoteJson request, @RequestParam(required = false) Map<String, String> parameters) {
        request.setStatus(QuoteStatus.OPEN);
        request.setQuoteAttribute(parameters);
        return quoteService.save(request.toEntity()).toJson();
    }

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations={"crm-quote.create", "crm-quote.price-admin"})
    @PutMapping("/{id}")
	@SendNotification(concern = "QUOTE_PRICE_REQUEST", beforeCondition = "@spotQuoteNotificationConditon.isPriceRequestedBefore(args[1])", targetType = TargetType.AUTH_OPERATION, target = "crm-quote.price-admin")
	@SendNotification(concern = "QUOTE_PRICE_APPROVE", beforeCondition = "@spotQuoteNotificationConditon.isPriceApprovedBefore(args[1])", afterCondition = "@spotQuoteNotificationConditon.isPriceApprovedAfter(result)")
    public QuoteJson updateQuote(@PathVariable Long id, @RequestBody QuoteJson request, @RequestParam(required = false) Map<String, String> parameters) {
        request.setId(id);
        request.validateQuote();
        request.setQuoteAttribute(parameters);
        Quote quote = quoteService.update(id, request.toEntity());
        return quote.toJson();
    }

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/{id}")
    public QuoteJson retrieveQuote(@PathVariable Long id) {
    	return quoteService.getById(id).toJson();
    }
    
    @GetMapping("/quotes-by-potential/{potentialId}")
    public void checkQuotesByPotential(@PathVariable Long potentialId) {
    	List<SpotQuote> quotes = spotQuoteService.findAll(potentialId);
    	
    	List<SpotQuote> notCanceledQuotes = quotes.stream()
    	.filter(p -> QuoteStatus.CANCELED != p.getStatus())
    	.collect(Collectors.toList());
    	
    	if(!notCanceledQuotes.isEmpty()) {
    		throw new ValidationException("The potential with active quotes can't ignored!"); 
    	}
    }

    @GetMapping("/quotes-by-attribute")
    public List<QuoteJson> retrieveQuotesByAttribute(@RequestParam String attributeKey,
                                                 @RequestParam String attributeValue,
                                                 @RequestParam(required = false, defaultValue = "true") Boolean ignoreCanceled) {
        List<Quote> quotes = quoteService.findAllByAttribute(attributeKey, attributeValue);
        if (ignoreCanceled.booleanValue()) {
            quotes = quotes.stream().filter(p -> QuoteStatus.CANCELED != p.getStatus()).collect(Collectors.toList());
        }
        return quotes.stream().map(Quote::toJson).collect(Collectors.toList());
    }

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/byQuoteNumber")
    public QuoteJson retrieveQuoteByNumber(@RequestParam Long quoteNumber) {
    	return quoteService.getByNumber(quoteNumber).toJson();
    }

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/byReferrerTask")
    public QuoteJson retrieveQuoteByReferrerTask(@RequestParam String referrerTaskId) {
    	return quoteService.getByReferrerTask(referrerTaskId).toJson();
    }

    @GetMapping
    public Page<QuoteJson> list(@RequestParam @Max(100) Integer size, @RequestParam Integer page) {

        Pageable pageRequest = new PageRequest(page, size);

        Page<Quote> entityPage = quoteService.list(pageRequest);

        List<QuoteJson> quotes = entityPage.getContent().stream().map(Quote::toJson).collect(Collectors.toList());
        return new PageImpl<>(quotes, pageRequest, entityPage.getTotalElements());
    }

    @PutMapping("/{quoteId}/notes")
    public List<NoteJson> updateNotes(@PathVariable Long quoteId, @RequestBody List<NoteJson> request) {
        return quoteService.updateNotes(quoteId, request).stream().map(NoteJson::fromEntity).collect(Collectors.toList());
    }

    @PutMapping("/{quoteId}/documents")
    public List<DocumentJson> updateDocuments(@PathVariable Long quoteId, @RequestBody List<DocumentJson> request) {
        return quoteService.updateDocuments(quoteId, request).stream().map(DocumentJson::fromEntity).collect(Collectors.toList());
    }

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/calculatePrices")
    public CalculatedPriceResultJson calculatePrices(@Valid @RequestBody QuoteJson request){
        return priceCalculationService.calculatePrices(request);
    }

    @PostMapping("/validate/price/{billingItemCode}")
    public void checkPriceValidity(@PathVariable String billingItemCode, @RequestBody QuoteJson request){
    	priceCalculationService.checkPriceValidity(billingItemCode, request.toEntity());
    }

    @PostMapping("/bundledProduct/exchange-amount")
    public List<ProductJson> calculateBundledProductExchangeAmount(@RequestBody List<ProductJson> request, @RequestParam(required=false) Long subsidiaryId){
        return exchangeRateService.calculateBundledProductExchangeAmount(request, subsidiaryId);
    }

    @PostMapping("/price/exchangePrice")
    public BigDecimal getExchangeRate(@RequestBody Currency currency, @RequestParam(required=false) Long subsidiaryId){
        return exchangeRateService.getExchangeRate(currency, subsidiaryId);
    }

    @PostMapping("/determineBillingItems")
    public List<BillingItem> determineBillingItems(@RequestBody QuoteJson request){
        return calculationService.determineBillingItems(request);
    }

    @PostMapping("/calculatePayWeight")
    public PayWeightCalculationJson calculatePayWeight(@RequestBody PayWeightCalculationParams request){
        return calculationService.calculatePayWeight(request);
    }
    
    @PostMapping("/calculateChargeableWeight")
    public BigDecimal calculateChargeableWeight(@RequestBody ChargeableWeightParams request){
        return calculationService.calculateChargeableWeight(request);
    }


    @PostMapping("/{quoteId}/emailQuote")
    public void emailQuote(@PathVariable Long quoteId, @RequestBody EmailJson request) {
    	quoteService.emailQuote(quoteId, request);
    }

    @GetMapping("/downloadBundledProductListTemplate")
    public void downloadBundledProductListTemplate(HttpServletResponse response) {
        String filePath = "bundled_product_list_template.xlsx";
        Utils.downloadFile(filePath, true, response);
    }

    @Timed(value = "crm-quote.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping("/convertExcelToBundledProductList")
    public Iterable<ProductJson> convertExcelToBundledProductList(@RequestParam String serviceAreaCode, @RequestParam("file") MultipartFile multipartFile) {

        CodeNamePair serviceArea = kartoteksService.findServiceAreaByCode(serviceAreaCode,true);
        CountryPointType countryPointType = CountryPointType.findByServiceArea(serviceArea.getCode());
        InputStream inputStream = null;
        File file;

        try {

            String originalFilename = FilenameUtils.getName(multipartFile.getOriginalFilename());
            String extension = FilenameUtils.getExtension(originalFilename);
            String physicalFilename;

            if (StringUtils.isNotEmpty(extension)) {
                physicalFilename = FilenameUtils.getBaseName(originalFilename) + "_" + UUID.randomUUID().toString() + "." + extension;
            } else {
                physicalFilename = originalFilename + "_" + UUID.randomUUID().toString();
            }

            Path tempDirectoryPath = Paths.get(FileUtils.getTempDirectoryPath());
            file = tempDirectoryPath.resolve(physicalFilename).toFile();

            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                throw new ApplicationException("Input stream could not be opened.", e);
            }

            try {
                FileUtils.copyToFile(inputStream, file);
            } catch (IOException e) {
                throw new ApplicationException("File could not be uploaded: " + originalFilename, e);
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                throw new ApplicationException("Input stream could not be closed.", e);
            }

        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return bundledProductExcelService.processBundledProductListExcel(file, serviceArea, countryPointType);
    }
    
    @PatchMapping("/id-mapping")
    public QuoteJson saveIdMapping(@RequestParam Long number, @RequestBody QuoteIdMappingJson request) {
    	return quoteService.updateApplicationIdMapping(number, request).toJson();
    }
    
    @PatchMapping("/order-mapping")
    public QuoteJson saveOrderMapping(@RequestParam Long number, @RequestBody QuoteOrderMappingJson request) {
    	return Optional.ofNullable(quoteService.updateOrderMapping(number, request)).map(Quote::toJson).orElse(null);
    }
    
    /**
     * Burası test amacıyla yapıldı.
     */
    @GetMapping("/fake-force-location-export-event")
    public String createFakeForceLocationExportEvent(@RequestParam Long locationId) {

        ForceLocationExportEventMessage message = new ForceLocationExportEventMessage();
        message.setLocationId(locationId);

        publisher.publishEvent(message);

        return "success";
    }

    /**
     * Burası test amacıyla yapıldı.
     */
    @GetMapping("/fake-company-import-event")
    public String createFakeCompanyImportEvent(@RequestParam String locationIds) {

        FakeCompanyImportEventMessage message = new FakeCompanyImportEventMessage();
        message.setLocationIds(new ArrayList<>());

        for (String item : locationIds.split(",")) {
            message.getLocationIds().add(Long.valueOf(item));
        }

        publisher.publishEvent(message);

        return "success";
    }
    
    @GetMapping("/transporting-companies-by-type")
    public List<OwnerCompany> getTransportingCompanyByType(@RequestParam String type){
    	
    	return ownerCompanyService.getByType(type);
    	
    }
}
