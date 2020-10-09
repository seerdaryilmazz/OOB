package ekol.agreement.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.agreement.config.ExportCompanyNotificationMailList;
import ekol.agreement.domain.dto.*;
import ekol.agreement.domain.dto.agreement.*;
import ekol.agreement.domain.dto.kartoteks.*;
import ekol.agreement.domain.enumaration.AgreementStatus;
import ekol.agreement.domain.model.*;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.event.*;
import ekol.agreement.repository.*;
import ekol.agreement.util.Utils;
import ekol.exceptions.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AgreementService {

    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final String QUADRO = "QUADRO";

    private AgreementRepository agreementRepository;
    private NoteCrudService noteCrudService;
    private DocumentCrudService documentCrudService;
    private HistoryModelRepository historyModelRepository;
    private HistoryUnitPriceRepository historyUnitPriceRepository;
    private PriceAdaptationModelRepository priceAdaptationModelRepository;
    private ApplicationEventPublisher eventPublisher;
    private StringRedisTemplate redisTemplate;
    private CustomAgreementRepository customAgreementRepository;
    private AccountService accountService;
    private KartoteksService kartoteksService;
    private ExportCompanyNotificationMailList exportCompanyNotificationMailList;

    private static final String AGREEMENT_INCREMENT_KEY = "increment_agreementNumber";
    private static final int AGREEMENT_INCREMENT_START = 10000;

    private Long produceAgreementNumber(){
        redisTemplate.opsForValue().setIfAbsent(AGREEMENT_INCREMENT_KEY, String.valueOf(AGREEMENT_INCREMENT_START));
        redisTemplate.opsForValue().increment(AGREEMENT_INCREMENT_KEY, 1);
        return Long.valueOf(redisTemplate.opsForValue().get(AGREEMENT_INCREMENT_KEY));
    }

    public Agreement getAgreementById(Long id) {
        Agreement agreement = getByIdOrThrowException(id);
        fillNotesAndDocuments(agreement);
        return agreement;
    }

    public Agreement getByIdOrThrowException(Long id) {
        return agreementRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Agreement with id {0} is not found.", id)
        );
    }

    public Page<Agreement> retrieveAgreementList(Pageable pageable, Long accountId) {
        return agreementRepository.findAllByAccountId(pageable, accountId);
    }
    public Iterable<Agreement> listAll(Set<Long> ids) {
        return agreementRepository.findAll(ids);
    }

    public List<AgreementJson> retrieveByAccountId(Long accountId) {
        List<Agreement> agreements = agreementRepository.findAllByAccountId(accountId);
        return agreements.stream().map(agreement -> {
            fillNotesAndDocuments(agreement);
            return agreement.toJson();
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public Agreement save(AgreementJson request) {
        Agreement agreement = request.toEntity();
        if(agreement.getId() == null && agreement.getNumber() == null){
            agreement.setNumber(produceAgreementNumber());
        }else {
            Agreement existed = getByIdOrThrowException(request.getId());
            customAgreementRepository.detach(existed);
            if(isStatusChanging(request, existed)){
                if(request.getStatus() == AgreementStatus.APPROVED){
                    shouldExportAccount(request);
                }
            }
        }
        saveRelationsForAgreement(agreement);
        Agreement savedAgreement = agreementRepository.save(agreement);
        fillNotesAndDocuments(savedAgreement);
        eventPublisher.publishEvent(AgreementEventData.with(savedAgreement, Operation.UPDATED));
        return savedAgreement;
    }

    public boolean isStatusChanging(AgreementJson agreement, Agreement existed){
        return agreement.getStatus() != existed.getStatus();
    }

    private void saveRelationsForAgreement(Agreement agreement) {
        if (Optional.ofNullable(agreement.getKpiInfos()).isPresent()) {
            agreement.getKpiInfos().forEach(kpiInfo -> kpiInfo.setAgreement(agreement));
        }
        if (Optional.ofNullable(agreement.getOwnerInfos()).isPresent()) {
            agreement.getOwnerInfos().forEach(ownerInfo -> ownerInfo.setAgreement(agreement));
        }
        if (Optional.ofNullable(agreement.getLetterOfGuarentees()).isPresent()) {
            agreement.getLetterOfGuarentees().forEach(letterOfGuarentee -> letterOfGuarentee.setAgreement(agreement));
        }
        if(Optional.ofNullable(agreement.getInsuranceInfos()).isPresent()) {
            agreement.getInsuranceInfos().forEach(insuranceInfo -> insuranceInfo.setAgreement(agreement));
        }
        if(Optional.ofNullable(agreement.getSignatureInfos()).isPresent()) {
            agreement.getSignatureInfos().forEach(signatureInfo -> signatureInfo.setAgreement(agreement));
        }
        if(Optional.ofNullable(agreement.getPriceAdaptationModels()).isPresent()) {
            agreement.getPriceAdaptationModels().forEach(priceAdaptationModel -> priceAdaptationModel.setAgreement(agreement));
        }
        if(Optional.ofNullable(agreement.getUnitPrices()).isPresent()) {
            agreement.getUnitPrices().forEach(unitPrice -> {
                unitPrice.setAgreement(agreement);
                if(Objects.nonNull(unitPrice.getPriceModel()))
                    agreement.getPriceAdaptationModels().stream().filter(unitPrice.getPriceModel()::equals).findFirst().ifPresent(unitPrice::setPriceModel);
            });
        }
    }

    public HistoryModel createHistoryModel(HistoryModelJson request){
        HistoryModel historyModel = request.toEntity();
        return historyModelRepository.save(historyModel);
    }

    public List<HistoryModel> retrieveHistoryModelsByModelId(Long modelId){
//        List<HistoryModel> historyModels = historyModelRepository.findByModelId(modelId);
//        historyModels.sort(Comparator.comparing(HistoryModel::getValidityEndDate).reversed());
        return historyModelRepository.findByModelId(modelId, new Sort(new Sort.Order(Sort.Direction.DESC, "lastUpdated")));
    }


    /** Audit tablosundan logları çekme:
     * public List<PriceAdaptationModel> retrieveHistoryModels(Long id){
     *         AuditQuery q = AuditReaderFactory.get(em).createQuery().forRevisionsOfEntity(PriceAdaptationModel.class, false, true);
     *         q.add(AuditEntity.id().eq(id));
     *         return (List<PriceAdaptationModel>) q.getResultList();
     *     }
     */


    public HistoryUnitPrice createHistoryUnitPrice(HistoryUnitPriceJson request){
        HistoryUnitPrice historyUnitPrice = request.toEntity();
        return historyUnitPriceRepository.save(historyUnitPrice);
    }

    public List<HistoryUnitPrice> retrieveHistoryUnitPricesByUnitPriceId(Long unitPriceId){
        return historyUnitPriceRepository.findByUnitPriceId(unitPriceId, new Sort(new Sort.Order(Sort.Direction.DESC, "lastUpdated")));
    }

    public PriceAdaptationModelJson retrievePriceAdpModelById(Long id){
        return PriceAdaptationModelJson.fromEntity(priceAdaptationModelRepository.findOne(id));
    }


    private void fillNotesAndDocuments(Agreement agreement){
        agreement.setNotes(noteCrudService.getByAgreement(agreement));
        agreement.setDocuments(documentCrudService.getByAgreement(agreement));
    }

    public List<Note> updateNotes(Long id, List<NoteJson> request) {
        Agreement agreement = getByIdOrThrowException(id);
        agreement.setNotes(noteCrudService.getByAgreement(agreement));
        return noteCrudService.save(agreement, request);
    }

    public List<Document> updateDocuments(Long id, List<DocumentJson> request) {
        Agreement agreement = getByIdOrThrowException(id);
        agreement.setDocuments(documentCrudService.getByAgreement(agreement));
        return documentCrudService.save(agreement, request);
    }

    public CalculationPriceObject setNullValues(CalculationPriceObject calculationPriceObject){
        UnitPriceJson unitPrice = calculationPriceObject.getUnitPrice();
        HistoryUnitPriceJson historyUnitPrice = calculationPriceObject.getHistoryUnitPrice();
        if(Objects.isNull(unitPrice.getEurRef())){ unitPrice.setEurRef(BigDecimal.valueOf(1)); }
        if(Objects.isNull(unitPrice.getUsdRef())){ unitPrice.setUsdRef(BigDecimal.valueOf(1)); }
        if(Objects.isNull(unitPrice.getMinimumWageRef())){ unitPrice.setMinimumWageRef(BigDecimal.valueOf(1)); }
        if(Objects.isNull(unitPrice.getInflationRef())){ unitPrice.setInflationRef(1); }
        if(Objects.isNull(historyUnitPrice.getEurRef())){ historyUnitPrice.setEurRef(BigDecimal.valueOf(1)); }
        if(Objects.isNull(historyUnitPrice.getUsdRef())){ historyUnitPrice.setUsdRef(BigDecimal.valueOf(1)); }
        if(Objects.isNull(historyUnitPrice.getMinimumWageRef())){ historyUnitPrice.setMinimumWageRef(BigDecimal.valueOf(1)); }
        if(Objects.isNull(historyUnitPrice.getInflationRef())){ historyUnitPrice.setInflationRef(1); }
        calculationPriceObject.setHistoryUnitPrice(historyUnitPrice);
        calculationPriceObject.setUnitPrice(unitPrice);
        return calculationPriceObject;
    }

    public BigDecimal calculateCurrentPrice(CalculationPriceObject calculationPriceObject) {
        UnitPriceJson unitPrice = calculationPriceObject.getUnitPrice();
        HistoryUnitPriceJson historyUnitPrice = calculationPriceObject.getHistoryUnitPrice();
        BigDecimal currentPrice;
        if("EUR".equals(unitPrice.getCurrency())){
            currentPrice = (historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getEur()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getEurRef()).multiply(unitPrice.getEurRef().divide(historyUnitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP)).divide(unitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP)
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getUsd()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getEurRef()).multiply(unitPrice.getUsdRef().divide(historyUnitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP)).divide(unitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getMinimumWage()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getEurRef()).multiply(unitPrice.getMinimumWageRef().divide(historyUnitPrice.getMinimumWageRef(),5, BigDecimal.ROUND_HALF_UP)).divide(unitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getInflation()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getEurRef()).multiply(new BigDecimal((1+(unitPrice.getInflationRef().floatValue() / 100)))).divide(unitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP)));
        }else if("USD".equals(unitPrice.getCurrency())){
            currentPrice = (historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getEur()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getUsdRef()).multiply(unitPrice.getEurRef().divide(historyUnitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP)).divide(unitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP)
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getUsd()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getUsdRef()).multiply(unitPrice.getUsdRef().divide(historyUnitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP)).divide(unitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getMinimumWage()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getUsdRef()).multiply(unitPrice.getMinimumWageRef().divide(historyUnitPrice.getMinimumWageRef(),5, BigDecimal.ROUND_HALF_UP)).divide(unitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getInflation()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(historyUnitPrice.getUsdRef()).multiply(new BigDecimal((1+(unitPrice.getInflationRef().floatValue() / 100)))).divide(unitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP)));
        }else if("TRY".equals(unitPrice.getCurrency())){
            currentPrice = (historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getEur()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(unitPrice.getEurRef().divide(historyUnitPrice.getEurRef(),5, BigDecimal.ROUND_HALF_UP))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getUsd()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(unitPrice.getUsdRef().divide(historyUnitPrice.getUsdRef(),5, BigDecimal.ROUND_HALF_UP)))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getMinimumWage()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(unitPrice.getMinimumWageRef().divide(historyUnitPrice.getMinimumWageRef(),5, BigDecimal.ROUND_HALF_UP)))
                    .add(historyUnitPrice.getPrice().multiply(new BigDecimal(unitPrice.getPriceModel().getInflation()).divide(HUNDRED,5, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal((1+(unitPrice.getInflationRef().floatValue() / 100))))));
        }else{
            throw new ApplicationException("There is no calculation defined for the chosen currency");
        }
        return currentPrice;
    }

    public LocalDate calculateAutoRenewalDate(String endDate, String addType, Integer addCount){
        LocalDate date = Utils.deserializeLocalDateStr(endDate);
        LocalDate calculatedDate = null;
        switch (addType){
            case "DAYS":
                calculatedDate = date.minusDays(addCount);
                break;
            case "MONTHS":
                calculatedDate = date.minusMonths(addCount);
                break;
            case "YEARS":
                calculatedDate = date.minusYears(addCount);
                break;
        }
        return calculatedDate;
    }

    public Agreement extendAgreement(Long id, String endDate){
        Agreement agreement = getByIdOrThrowException(id);
        LocalDate oldEndDate = agreement.getEndDate();
        LocalDate newEndDate = Utils.deserializeLocalDateStr(endDate);

        agreement.setEndDate(newEndDate);
        if(Objects.nonNull(agreement.getAutoRenewalDateType()) && Objects.nonNull(agreement.getAutoRenewalLength())){
            agreement.setAutoRenewalDate(calculateAutoRenewalDate(endDate, agreement.getAutoRenewalDateType().getId(), agreement.getAutoRenewalLength()));
        }

        Agreement savedAgreement = agreementRepository.save(agreement);

        if(savedAgreement != null){
            AgreementExtension agreementExtension = new AgreementExtension();
            agreementExtension.setAgreementId(agreement.getId());
            agreementExtension.setNewEndDate(newEndDate);
            agreementExtension.setOldEndDate(oldEndDate);
            agreementExtension.setChangedBy(agreement.getLastUpdatedBy());
            eventPublisher.publishEvent(agreementExtension);
            return savedAgreement;
        }
        return agreement;
    }

    public void shouldExportAccount(AgreementJson agreementJson){
        CompanyAndLocation companyAndLocation = findAccountCompanyAndCompanyLocation(agreementJson);
        if(Optional.ofNullable(companyAndLocation.getLocation()).isPresent()){
            if (!doesCompanyLocationHaveMapping(companyAndLocation.getLocation()) && !doesCompanyLocationHaveAtLeastOneImportQueueItem(companyAndLocation.getCompany(), companyAndLocation.getLocation())) {
                // trigger "send to export queue"
                eventPublisher.publishEvent(new ForceLocationExportEventMessage(companyAndLocation.getLocation().getId()));
                // send notification email
                Optional.ofNullable(createCompanyExportNotificationEmail(companyAndLocation.getCompany().getName())).ifPresent(eventPublisher::publishEvent);
            }
        }

    }
    protected boolean doesCompanyLocationHaveMapping(CompanyLocation companyLocation) {
        return Optional.ofNullable(companyLocation.getMappedIds())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .parallel()
                .anyMatch(item->Objects.equals(QUADRO, item.getApplication()) && StringUtils.isNotBlank(item.getApplicationLocationId()));
    }

    protected boolean doesCompanyLocationHaveAtLeastOneImportQueueItem(Company company, CompanyLocation companyLocation) {
        CompanyImportQueue companyImportQueue = kartoteksService.findLastImportQueueItemForLocation(QUADRO, company.getId(), companyLocation.getId(), true);
        return Objects.nonNull(companyImportQueue);
    }

    protected CompanyAndLocation findAccountCompanyAndCompanyLocation(AgreementJson agreementJson) {
        CompanyAndLocation companyAndLocation = new CompanyAndLocation();
        Account account = Optional.ofNullable(accountService.findAccountById(agreementJson.getAccount().getId(), true)).orElseThrow(()->new BadRequestException("Account is deleted."));
        companyAndLocation.setCompany(Optional.ofNullable(kartoteksService.findCompanyById(account.getCompany().getId(), true)).orElseThrow(()->new BadRequestException("Account company is deleted.")));
        companyAndLocation.getCompany().getCompanyLocations().stream().filter(CompanyLocation::isDefault).findAny().ifPresent(companyAndLocation::setLocation);
        return companyAndLocation;
    }

    private EmailMessage createCompanyExportNotificationEmail(String companyName) {

        String countryIso="TR";
        List<String> mailGroup = Objects.equals(countryIso,"TR") ? exportCompanyNotificationMailList.getTr() : exportCompanyNotificationMailList.getOther();
        if(CollectionUtils.isEmpty(mailGroup)){
            return null;
        }
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(mailGroup);
        emailMessage.setSubject("company card notification");
        emailMessage.setBody(companyName + " company card has inserted to task table to create or match on Quadro. Please check as soon as possible!");
        emailMessage.setHtml(Boolean.FALSE);
        return emailMessage;
    }
}
