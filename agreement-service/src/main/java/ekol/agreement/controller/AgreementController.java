package ekol.agreement.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import ekol.agreement.domain.enumaration.AgreementStatus;
import ekol.agreement.validation.SaveAgreementValidation;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ekol.agreement.domain.dto.agreement.*;
import ekol.agreement.domain.model.*;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.service.*;
import ekol.agreement.validation.RetrieveAgreementValidation;
import ekol.event.auth.Authorize;
import ekol.resource.validation.OneOrderValidation;

@RestController
@RequestMapping("/agreement")
@Validated
public class AgreementController {

    @Autowired
    private AgreementService agreementService;
    
    @Autowired
    private DeleteService deleteService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @GetMapping
    public Page<AgreementJson> retrieveAgreementList(@RequestParam Integer size,
                                                     @RequestParam Integer page,
                                                     @RequestParam(required = false) Long accountId) {
        Pageable pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "lastUpdated");
        Page<Agreement> entityPage = agreementService.retrieveAgreementList(pageRequest, accountId);

        List<AgreementJson> agreementJsonList = entityPage.getContent().stream().map(Agreement::toJson).collect(Collectors.toList());
        return new PageImpl<>(agreementJsonList, pageRequest, entityPage.getTotalElements());
    }

    @GetMapping("/list-by-accountId")
    public List<AgreementJson> retrieveAgreementList(@RequestParam Long accountId,
                                                     @RequestParam(required = false, defaultValue = "true") boolean ignoreCanceled) {
        List<AgreementJson> agreements = agreementService.retrieveByAccountId(accountId);
        if (ignoreCanceled) {
            agreements = agreements.stream().filter(a -> AgreementStatus.CANCELED != a.getStatus()).collect(Collectors.toList());
        }
        return agreements;
    }

    @Authorize(operations = {"agreement.view"})
    @GetMapping("/{id:[0-9]+}")
    public AgreementJson retrieveAgreement(@PathVariable Long id) {
        return agreementService.getAgreementById(id).toJson();
    }

    @OneOrderValidation(RetrieveAgreementValidation.class)
    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.OPTIONS)
    public void validateViewAgreementAsAuthorized(@PathVariable Long id) { }

    @GetMapping("/{id:[0-9]+}/as-authorized")
    @OneOrderValidation(RetrieveAgreementValidation.class)
    public AgreementJson retrieveAgreementAsAuthorized(@PathVariable Long id) {
        return agreementService.getAgreementById(id).toJson();
    }

    @PostMapping
    @OneOrderValidation(SaveAgreementValidation.class)
    public AgreementJson createAgreement(@Valid @RequestBody AgreementJson request) {
        return agreementService.save(request).toJson();
    }

    @PutMapping("/{id}")
    @OneOrderValidation(SaveAgreementValidation.class)
    public AgreementJson updateAgreement(@PathVariable Long id, @Valid @RequestBody AgreementJson request) {
        request.setId(id);
        return agreementService.save(request).toJson();
    }

    @PutMapping("/{agreementId}/notes")
    public List<NoteJson> updateNotes(@PathVariable Long agreementId, @RequestBody List<NoteJson> request) {
        return agreementService.updateNotes(agreementId, request).stream().map(NoteJson::fromEntity).collect(Collectors.toList());
    }

    @PutMapping("/{agreementId}/documents")
    public List<DocumentJson> updateDocuments(@PathVariable Long agreementId, @RequestBody List<DocumentJson> request) {
        return agreementService.updateDocuments(agreementId, request).stream().map(DocumentJson::fromEntity).collect(Collectors.toList());
    }

    @GetMapping("/historyAdpModel/{modelId}")
    public List<HistoryModelJson> retrieveHistoryModelsByModelId(@PathVariable Long modelId){
        return agreementService.retrieveHistoryModelsByModelId(modelId).stream().map(HistoryModel::toJson).collect(Collectors.toList());
    }

    @PostMapping("/historyAdpModel")
    public HistoryModelJson createHistoryModel(@RequestBody HistoryModelJson request){
        return agreementService.createHistoryModel(request).toJson();
    }

    @GetMapping("/historyUnitPrice/{unitPriceId}")
    public List<HistoryUnitPriceJson> retrieveHistoryUnitPricesByUnitPriceId(@PathVariable Long unitPriceId){
        List<HistoryUnitPriceJson> list = new ArrayList<>();
        for (HistoryUnitPrice historyUnitPrice : agreementService.retrieveHistoryUnitPricesByUnitPriceId(unitPriceId)) {
            HistoryUnitPriceJson toJson = historyUnitPrice.toJson();
            if(historyUnitPrice.getPriceModelId() != null){
                toJson.setPriceModel(agreementService.retrievePriceAdpModelById(historyUnitPrice.getPriceModelId()));
            }
            list.add(toJson);
        }
        return list;
    }

    @DeleteMapping("/unit-price-histories/{id}")
    public void deleteHistoryUnitPriceByUnitPriceId(@PathVariable Long id){
        deleteService.deleteHistoriesOfUnitPriceByUnitPriceId(id);
    }

    @DeleteMapping("/unit-price-history/{id}")
    public void deleteHistoryUnitPriceById(@PathVariable Long id){
        deleteService.deleteHistoryOfUnitPriceById(id);
    }

    @PostMapping("/historyUnitPrice")
    public HistoryUnitPriceJson createHistoryModel(@RequestBody HistoryUnitPriceJson request){
        return agreementService.createHistoryUnitPrice(request).toJson();
    }

    @PostMapping("/calculate-current-price")
    public BigDecimal calculateCurrentPrice(@RequestBody CalculationPriceObject calculationPriceObject) {
        return agreementService.calculateCurrentPrice(agreementService.setNullValues(calculationPriceObject));
    }

    @GetMapping("/extend-agreement/{id}")
    public AgreementJson saveAgreementExtension(@PathVariable Long id, @RequestParam String newEndDate){
        return agreementService.extendAgreement(id, newEndDate).toJson();
    }

    @GetMapping("/calculate-auto-renewal-date")
    public String calculateAutoRenewalDate(@RequestParam String endDate,
                                           @RequestParam String addType,
                                           @RequestParam Integer addCount) {
        LocalDate autoRenewalDate = agreementService.calculateAutoRenewalDate(endDate, addType, addCount);
        return autoRenewalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
