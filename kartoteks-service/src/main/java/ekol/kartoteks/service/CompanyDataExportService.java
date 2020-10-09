package ekol.kartoteks.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.dto.AccountJson;
import ekol.kartoteks.domain.dto.UserJson;
import ekol.kartoteks.domain.exchange.CompanyExchangeData;
import ekol.kartoteks.domain.exchange.ContactExchangeData;
import ekol.kartoteks.domain.exchange.LocationExchangeData;
import ekol.kartoteks.domain.exchange.QueueExchangeData;
import ekol.kartoteks.domain.export.ExternalSystemDataExporter;
import ekol.kartoteks.event.CompanyExportEventProducer;
import ekol.kartoteks.repository.CompanyExportQueueRepository;
import ekol.kartoteks.repository.CompanyIdMappingRepository;
import ekol.kartoteks.repository.CompanyLocationIdMappingRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by kilimci on 13/07/16.
 */
@Service
public class CompanyDataExportService implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(CompanyDataExportService.class);

    private ApplicationContext applicationContext;

    @Autowired
    private CompanyIdMappingRepository companyIdMappingRepository;

    @Autowired
    private CompanyLocationIdMappingRepository locationIdMappingRepository;

    @Autowired
    private CompanyExportQueueRepository companyExportQueueRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private CompanyExportEventProducer companyExportEventProducer;

    @Autowired
    private CrmAccountService crmAccountService;

    @Autowired
    private UserService userService;

    @Value("${oneorder.dataExportEnabled:true}")
    private boolean dataExportEnabled;

    private static final int CONSECUTIVE_ERROR_THRESHOLD = 10;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    private void executeQueueItem(CompanyExportQueue companyExportQueue){
        try {
            ExternalSystemDataExporter exporter =
                    (ExternalSystemDataExporter) applicationContext.getBean(companyExportQueue.getApplication().getDataExporterBean());
            exporter.exportCompany(companyExportQueue);
            companyExportQueue.setLatestExecuteDate(new UtcDateTime(LocalDateTime.now()));
            companyExportQueue.setStatus(ExportQueueStatus.SUCCESSFUL);
            companyExportQueueRepository.save(companyExportQueue);
        }catch(Exception e){
            handleExportCompanyError(companyExportQueue);
            String message = MessageFormat.format("Error exporting queue item id: {0}, application company id: {1}", companyExportQueue.getId(), companyExportQueue.getApplicationCompanyId());
            LoggerFactory.getLogger(CompanyDataExportService.class).error(message, e);
        }
    }
    private void handleExportCompanyError(CompanyExportQueue companyExportQueue){
        companyExportQueue.setLatestExecuteDate(new UtcDateTime(LocalDateTime.now()));
        companyExportQueue.increaseRetryCount();
        companyExportQueue.setStatus(
                companyExportQueue.getRetryCount() >= CONSECUTIVE_ERROR_THRESHOLD ?
                        ExportQueueStatus.CONSECUTIVE_FAILURE : ExportQueueStatus.FAILED);
        companyExportQueueRepository.save(companyExportQueue);

        if(companyExportQueue.getStatus().equals(ExportQueueStatus.FAILED)){
            companyExportEventProducer.produceDelayed(companyExportQueue);
        }
    }
    private CompanyExportQueue skipQueueItem(CompanyExportQueue companyExportQueue){
        companyExportQueue.setLatestExecuteDate(new UtcDateTime(LocalDateTime.now()));
        companyExportQueue.setStatus(ExportQueueStatus.SKIPPED);
        return companyExportQueueRepository.save(companyExportQueue);
    }
    public void processQueueItem(Long exportQueueId){
        CompanyExportQueue companyExportQueue = companyExportQueueRepository.findOne(exportQueueId);
        if(companyExportQueue != null){
            if(!dataExportEnabled){
                skipQueueItem(companyExportQueue);
            }else{
                executeQueueItem(companyExportQueue);
            }
        }
    }

    private QueueExchangeData createQueueExchange(CompanyExchangeData companyData, String username, RemoteApplication application){
        QueueExchangeData queueData = new QueueExchangeData();
        queueData.setCompany(companyData);
        queueData.setUserName(username);
        queueData.setApplication(application);
        return queueData;
    }

    private CompanyExportQueue createExportQueue(Company company, RemoteApplication application,
                                                 QueueExchangeData queueExchangeData, String applicationCompanyId){
        CompanyExportQueue exportQueue = new CompanyExportQueue();
        exportQueue.setApplication(application);
        exportQueue.setApplicationCompanyId(applicationCompanyId);
        exportQueue.setCompany(company);
        exportQueue.setCreateDate(new UtcDateTime(LocalDateTime.now()));
        exportQueue.setStatus(ExportQueueStatus.PENDING);
        exportQueue.setData(queueExchangeData.toJSON());
        return exportQueue;
    }

    public void export(String applicationCompanyId){
        List<CompanyLocationIdMapping> mappings = locationIdMappingRepository.findByApplicationLocationId(applicationCompanyId);
        mappings.forEach(locationIdMapping ->
            export(locationIdMapping.getCompanyLocation().getCompany(), locationIdMapping.getCompanyLocation(), false)
        );
    }

    public void export(Company company, boolean forceExport){
        company.getCompanyLocations().forEach(companyLocation ->
            export(company, companyLocation, forceExport)
        );
    }
    public void export(Company company, CompanyLocation companyLocation, boolean forceExport){
        if (forceExport || doesCompanyLocationHaveMapping(companyLocation, RemoteApplication.QUADRO)) {
            exportToQuadro(company, companyLocation);
        }
        if (forceExport || doesCompanyLocationHaveMapping(companyLocation, RemoteApplication.SALESFORCE)) {
            exportToSalesforce(company, companyLocation);
        }
    }

    public boolean doesCompanyLocationHaveMapping(CompanyLocation companyLocation, RemoteApplication application) {
        boolean result = false;
        if (!CollectionUtils.isEmpty(companyLocation.getMappedIds())) {
            for (CompanyLocationIdMapping mapping : companyLocation.getMappedIds()) {
                if (mapping.getApplication().equals(application) && StringUtils.isNotBlank(mapping.getApplicationLocationId())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public void execute(Long exportQueueId){
        CompanyExportQueue queue = companyExportQueueRepository.findOne(exportQueueId);
        if(queue == null){
            throw new ResourceNotFoundException("Company Export Queue item with id {0} not found", exportQueueId);
        }
        publisher.publishEvent(queue);
    }
    private List<ContactExchangeData> createContactExchangeData(Company company, CompanyLocation companyLocation, RemoteApplication application){
        List<ContactExchangeData> contactExchanges = new ArrayList<>();

//        company.getCompanyContacts().forEach(companyContact -> {
//            List<CompanyContactIdMapping> contactMappings = companyContact.getMappedIds().stream()
//                    .filter(companyContactIdMapping -> companyContactIdMapping.getApplication().equals(application)).collect(Collectors.toList());
//            if(contactMappings.isEmpty()){
//                ContactExchangeData exchangeData = ContactExchangeData.fromContact(companyContact);
//                contactExchanges.add(exchangeData);
//            }else{
//                contactMappings.forEach(companyContactIdMapping -> {
//                    ContactExchangeData exchangeData = ContactExchangeData.fromContact(companyContact);
//                    exchangeData.setContactId(companyContactIdMapping.getApplicationContactId());
//                    contactExchanges.add(exchangeData);
//                });
//            }
//        });

        // TODO: Yukarıda kapatılmış durumda olan kodun lambda'sız versiyonu..
        // Biryerde hata alıyoruz, hatanın tam olarak yerini anlamak için biraz değiştirdik.
        for (CompanyContact companyContact : company.getCompanyContacts()) {

            if (companyContact.getCompanyLocation() != null && companyContact.getCompanyLocation().getId().equals(companyLocation.getId())) {

                List<CompanyContactIdMapping> contactMappings = new ArrayList<>();

                for (CompanyContactIdMapping companyContactIdMapping : companyContact.getMappedIds()) {

                    if (companyContactIdMapping.getApplication() == null) {
                        logger.error("companyContact.getId(): " + companyContact.getId());
                        logger.error("companyContactIdMapping.getApplicationContactId(): " + companyContactIdMapping.getApplicationContactId());
                    }

                    if (companyContactIdMapping.getApplication().equals(application)) {
                        contactMappings.add(companyContactIdMapping);
                    }
                }

                if (contactMappings.isEmpty()) {
                    ContactExchangeData exchangeData = ContactExchangeData.fromContact(companyContact);
                    contactExchanges.add(exchangeData);
                } else {
                    for (CompanyContactIdMapping companyContactIdMapping : contactMappings) {
                        ContactExchangeData exchangeData = ContactExchangeData.fromContact(companyContact);
                        exchangeData.setContactId(companyContactIdMapping.getApplicationContactId());
                        contactExchanges.add(exchangeData);
                    }
                }
            }
        }

        return contactExchanges;
    }
    public void exportToQuadro(Company company, CompanyLocation companyLocation){
        List<CompanyLocationIdMapping> locationMappings =
                locationIdMappingRepository.findByCompanyLocationAndApplication(companyLocation, RemoteApplication.QUADRO);

        if(CollectionUtils.isEmpty(locationMappings)){
            CompanyExchangeData companyExchangeData = CompanyExchangeData.fromCompany(company);
            LocationExchangeData locationExchangeData = LocationExchangeData.fromLocation(companyLocation);
            companyExchangeData.setLocations(Arrays.asList(locationExchangeData));
            companyExchangeData.setContacts(this.createContactExchangeData(company, companyLocation, RemoteApplication.QUADRO));
            companyExchangeData.setAccountOwner(getUserByCompanyId(company.getId()));
            QueueExchangeData queueExchangeData = createQueueExchange(companyExchangeData, company.getLastUpdatedBy(), RemoteApplication.QUADRO);
            CompanyExportQueue queue = companyExportQueueRepository.save(
                    createExportQueue(company, RemoteApplication.QUADRO, queueExchangeData, null));

            publisher.publishEvent(queue);
        }else{
            locationMappings.forEach(companyLocationIdMapping -> {
                CompanyExchangeData companyExchangeData = CompanyExchangeData.fromCompany(company);
                LocationExchangeData locationExchangeData = LocationExchangeData.fromLocation(companyLocation);
                companyExchangeData.setCompanyId(companyLocationIdMapping.getApplicationLocationId());
                locationExchangeData.setLocationId(companyLocationIdMapping.getApplicationLocationId());
                companyExchangeData.setLocations(Arrays.asList(locationExchangeData));
                companyExchangeData.setContacts(this.createContactExchangeData(company, companyLocation, RemoteApplication.QUADRO));
                companyExchangeData.setAccountOwner(getUserByCompanyId(company.getId()));
                QueueExchangeData queueExchangeData = createQueueExchange(companyExchangeData, company.getLastUpdatedBy(), RemoteApplication.QUADRO);
                CompanyExportQueue queue = companyExportQueueRepository.save(
                        createExportQueue(company, RemoteApplication.QUADRO, queueExchangeData, companyLocationIdMapping.getApplicationLocationId()));

                publisher.publishEvent(queue);
            });
        }
    }
    public void exportToSalesforce(Company company, CompanyLocation companyLocation){
        List<CompanyIdMapping> companyMappings =
                companyIdMappingRepository.findByCompanyAndApplication(company, RemoteApplication.SALESFORCE);
        List<CompanyLocationIdMapping> locationMappings =
                locationIdMappingRepository.findByCompanyLocationAndApplication(companyLocation, RemoteApplication.SALESFORCE);
        if(!CollectionUtils.isEmpty(locationMappings)){
            locationMappings.forEach(companyLocationIdMapping -> {
                String applicationCompanyId = companyMappings.stream().filter(
                        mapping -> mapping.getApplication().equals(companyLocationIdMapping.getApplication())).map(
                        mapping -> mapping.getApplicationCompanyId())
                        .findFirst().orElse("");
                CompanyExchangeData companyExchangeData = CompanyExchangeData.fromCompany(company);
                LocationExchangeData locationExchangeData = LocationExchangeData.fromLocation(companyLocation);
                companyExchangeData.setSalesforceId(applicationCompanyId);
                locationExchangeData.setSalesforceId(companyLocationIdMapping.getApplicationLocationId());
                companyExchangeData.setLocations(Arrays.asList(locationExchangeData));
                companyExchangeData.setContacts(this.createContactExchangeData(company, companyLocation, RemoteApplication.SALESFORCE));
                QueueExchangeData queueExchangeData = createQueueExchange(companyExchangeData, company.getLastUpdatedBy(), RemoteApplication.SALESFORCE);
                CompanyExportQueue queue = companyExportQueueRepository.save(
                        createExportQueue(company, RemoteApplication.SALESFORCE, queueExchangeData, applicationCompanyId));

                publisher.publishEvent(queue);
            });
        }
    }

    public boolean isCompanyUpdateNecessary(Company existing, Company updated){
        return existing.hasUpdatesToExport(updated);
    }
    public void exportAllLocations(Company company){
        company.getCompanyLocations().forEach(companyLocation -> {
            if (companyLocation.getId() != null) {
                export(company, companyLocation, false);
            }
        });
    }
    public List<CompanyLocation> findLocationsNeedsToBeExported(Company existing, Company updated){
        List<CompanyLocation> needsToBeExported = new ArrayList<>();
        existing.getCompanyLocations().forEach(existingLocation ->
                updated.getCompanyLocations().stream().filter(
                        updatedLocation -> Objects.equals(existingLocation.getId(), updatedLocation.getId())).findFirst().ifPresent(
                        matchingLocation -> {
                            Set<CompanyContact> existingContacts = filterContactsOfLocation(existingLocation, existing.getCompanyContacts());
                            Set<CompanyContact> updatedContacts = filterContactsOfLocation(existingLocation, updated.getCompanyContacts());
                            if(hasUpdatesToExport(existingLocation, matchingLocation, true, existingContacts, updatedContacts)){
                                needsToBeExported.add(matchingLocation);
                            }
                        }
                )
        );
        updated.getCompanyLocations().stream().filter(updatedLocation ->
                existing.getCompanyLocations().stream()
                        .noneMatch(existingLocation -> Objects.equals(existingLocation.getId(), updatedLocation.getId()))
        ).forEach(needsToBeExported::add);

        return needsToBeExported;
    }

    public void checkDataUpdateAndExport(Company existing, Company updated){
        if(isCompanyUpdateNecessary(existing, updated)) {
            exportAllLocations(updated);
        }else{
            findLocationsNeedsToBeExported(existing, updated).forEach(companyLocation -> export(updated, companyLocation, false));
        }
    }
    public void checkDataUpdateAndExport(CompanyLocation existing, CompanyLocation updated){
        if(existing.hasUpdatesToExport(updated)){
            export(updated.getCompany(), updated, false);
        }
    }

    public Set<CompanyContact> filterContactsOfLocation(CompanyLocation location, Set<CompanyContact> allContactsOfCompany) {
        Set<CompanyContact> contactsOfLocation = new HashSet<>();
        if (!CollectionUtils.isEmpty(allContactsOfCompany)) {
            for (CompanyContact contact : allContactsOfCompany) {
                if (contact.getCompanyLocation() != null &&
                        contact.getCompanyLocation().getId() != null &&
                        contact.getCompanyLocation().getId().equals(location.getId())) {
                    contactsOfLocation.add(contact);
                }
            }
        }
        return contactsOfLocation;
    }

    public boolean hasUpdatesToExport(CompanyLocation existingLocation, CompanyLocation updatedLocation,
                                      boolean checkContacts, Set<CompanyContact> existingContacts, Set<CompanyContact> updatedContacts) {

        boolean hasUpdatesToExport = existingLocation.hasUpdatesToExport(updatedLocation);

        if (!hasUpdatesToExport && checkContacts) {

            boolean isEmptyExisting = CollectionUtils.isEmpty(existingContacts);
            boolean isEmptyUpdated = CollectionUtils.isEmpty(updatedContacts);

            if (!isEmptyExisting && !isEmptyUpdated) {
                if (existingContacts.size() != updatedContacts.size()) {
                    hasUpdatesToExport = true;
                } else {
                    for (CompanyContact c1 : existingContacts) {
                        CompanyContact match = null;
                        for (CompanyContact c2 : updatedContacts) {
                            if (c1.equals(c2)) {
                                match = c2;
                                break;
                            }
                        }
                        if (match != null) {
                            if (c1.hasUpdatesToExport(match)) {
                                hasUpdatesToExport = true;
                                break;
                            }
                        } else {
                            hasUpdatesToExport = true;
                            break;
                        }
                    }
                }
            } else if ((!isEmptyExisting && isEmptyUpdated) || (isEmptyExisting && !isEmptyUpdated)) {
                hasUpdatesToExport = true;
            }
        }

        return hasUpdatesToExport;
    }

    private UserJson getUserByCompanyId(Long companyId) {
        if (!Optional.ofNullable(companyId).isPresent()) {
            return null;
        }

        AccountJson accountJson = crmAccountService.findAccountByCompanyId(companyId);
        UserJson accountOwner = Optional.ofNullable(accountJson).map(acc -> userService.findUser(acc.getAccountOwner())).orElse(new UserJson());

        return accountOwner;
    }
}
