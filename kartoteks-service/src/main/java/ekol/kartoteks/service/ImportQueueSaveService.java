package ekol.kartoteks.service;

import java.util.*;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.exchange.*;
import ekol.kartoteks.event.CompanyImportQueueEvent;
import ekol.kartoteks.event.CompanyImportQueueEvent.Operation;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.repository.common.CountryRepository;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import ekol.resource.oauth2.SessionOwner;

/**
 * Created by kilimci on 20/04/16.
 */
@Service
public class ImportQueueSaveService {

    @Autowired
    private CompanyImportQueueRepository importQueueRepository;

    @Autowired
    private TaxOfficeRepository taxOfficeRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private PhoneTypeRepository phoneTypeRepository;

    @Autowired
    private UsageTypeRepository usageTypeRepository;

    @Autowired
    private ContactDepartmentRepository contactDepartmentRepository;

    @Autowired
    private ContactTitleRepository contactTitleRepository;

    @Autowired
    private BusinessSegmentTypeService businessSegmentTypeService;

    @Autowired
    private CompanyRelationTypeRepository companyRelationTypeRepository;

    @Autowired
    private CompanyRoleTypeRepository companyRoleTypeRepository;

    @Autowired
    private CompanySaveService companySaveService;

    @Autowired
    private ImportQueueProgressService importQueueProgressService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private SessionOwner sessionOwner;

    private void validateQueue(CompanyImportQueue companyImportQueue){
        if(companyImportQueue.getApplication() == null) {
            throw new BadRequestException("Application can not be empty");
        }
        if( companyImportQueue.getCompany() == null) {
            throw new BadRequestException("Company data can not be empty");
        }
        if(StringUtils.isBlank(companyImportQueue.getUserName())) {
            throw new BadRequestException("Username can not be empty");
        }
    }
    private void validateCountry(String countryCode){
        if(StringUtils.isNotBlank(countryCode) &&
                countryRepository.findByIsoIgnoreCase(countryCode) == null){

            throw new BadRequestException("Country with code ''{0}'' does not exist", countryCode);
        }
    }
    private void validateTaxOffice(CompanyExchangeData companyData){
        if(StringUtils.isNotBlank(companyData.getTaxofficeCode()) &&
                taxOfficeRepository.findByCode(companyData.getTaxofficeCode()) == null){

            throw new BadRequestException("Tax office with code ''{0}'' does not exist", companyData.getTaxofficeCode());
        }
    }
    private void validateCompanyInfo(CompanyExchangeData companyData){
        if(StringUtils.isBlank(companyData.getCompanyId())){
            throw new BadRequestException("Company ID can not be empty (ID of external system)");
        }
        if(StringUtils.isBlank(companyData.getName())){
            throw new BadRequestException("Company name can not be empty");
        }
        if(StringUtils.isNotBlank(companyData.getType()) &&
                companyTypeRepository.findByCode(companyData.getType()) == null) {

            throw new BadRequestException("Company type with code ''{0}'' does not exist", companyData.getType());
        }
    }
    private void validateSectors(CompanyExchangeData companyData){
        if(companyData.getSectors() != null){
            companyData.getSectors().forEach(s -> {
                if(sectorRepository.findByCode(s.getCode()) == null){
                    throw new BadRequestException("Sector with code ''{0}'' does not exist", s.getCode());
                }
            });
        }
    }
    private void validateCompany(CompanyExchangeData companyData){
        validateCompanyInfo(companyData);
        validateCountry(companyData.getCountryCode());
        validateTaxOffice(companyData);
        validateSectors(companyData);
        if(companyData.getLocations() != null){
            companyData.getLocations().forEach(this::validateLocation);
        }
        if(companyData.getContacts() != null){
            companyData.getContacts().forEach(this::validateContact);
        }
        if(companyData.getRoles() != null){
            companyData.getRoles().forEach(this::validateRole);
        }
        if(companyData.getRelations() != null){
            companyData.getRelations().forEach(this::validateRelation);
        }
    }

    private void validateLocationInfo(LocationExchangeData locationData){
        if(StringUtils.isBlank(locationData.getLocationId())){
            throw new BadRequestException("Location ID can not be empty (ID of external system)");
        }
        if(StringUtils.isBlank(locationData.getName())){
            throw new BadRequestException("Location name can not be empty");
        }
        locationData.getTypes().forEach(s -> {
            if(StringUtils.isNotBlank(s) &&
                    locationTypeRepository.findByCode(s) == null){
                throw new BadRequestException("location type with value ''{0}'' does not exist", s);
            }
        });
    }

    private void validateLocation(LocationExchangeData locationData){
        validateLocationInfo(locationData);
        validateCountry(locationData.getCountryCode());
        if(locationData.getPhoneNumbers() != null){
            locationData.getPhoneNumbers().forEach(this::validatePhoneNumber);
        }
    }
    private void validateContactDepartment(ContactExchangeData contact){
        if(StringUtils.isNotBlank(contact.getDepartmentCode()) &&
                contactDepartmentRepository.findByCode(contact.getDepartmentCode()) == null) {

            throw new BadRequestException("Department with code ''{0}'' does not exist", contact.getDepartmentCode());
        }
    }
    private void validateContactTitle(ContactExchangeData contact){
        if(StringUtils.isNotBlank(contact.getTitle()) &&
                contactTitleRepository.findByCode(contact.getTitle()) == null) {

            throw new BadRequestException("Title with code ''{0}'' does not exist", contact.getTitle());
        }
    }
    private void validateGender(ContactExchangeData contact){
        if(StringUtils.isNotBlank(contact.getGender()) &&
                !EnumUtils.isValidEnum(Gender.class, contact.getGender())) {

            throw new BadRequestException("Gender with code ''{0}'' does not exist", contact.getGender().toUpperCase());
        }
    }
    private void validateContact(ContactExchangeData contact){
        validateGender(contact);
        validateContactDepartment(contact);
        validateContactTitle(contact);
        if(contact.getPhoneNumbers() != null){
            contact.getPhoneNumbers().forEach(this::validatePhoneNumber);
        }
        if(contact.getEmails() != null){
            contact.getEmails().forEach(this::validateEmail);
        }
    }

    private void validateRole(RoleExchangeData segment){
        if(StringUtils.isNotBlank(segment.getRelationCode()) &&
                companyRoleTypeRepository.findByCode(segment.getRelationCode().toUpperCase()) == null) {

            throw new BadRequestException("Role with code ''{0}'' does not exist", segment.getRelationCode());
        }

        if(StringUtils.isNotBlank(segment.getSegmentCode()) &&
        		businessSegmentTypeService.findByCode(segment.getSegmentCode()) == null) {
            throw new BadRequestException("Segment with code ''{0}'' does not exist", segment.getSegmentCode());
        }
    }
    private void validateRelation(RelationExchangeData relation){
        if(StringUtils.isNotBlank(relation.getRelationTypeCode()) &&
                companyRelationTypeRepository.findByCode(relation.getRelationTypeCode().toUpperCase()) == null) {

            throw new BadRequestException("Relation with code ''{0}'' does not exist", relation.getRelationTypeCode());
        }
    }

    private void validatePhoneNumber(PhoneNumberExchangeData phoneNumber){
        if(StringUtils.isNotBlank(phoneNumber.getNumberType()) &&
                phoneTypeRepository.findByCode(phoneNumber.getNumberType()) == null) {

            throw new BadRequestException("Number type with value ''{0}'' does not exist", phoneNumber.getNumberType());
        }
        if(StringUtils.isNotBlank(phoneNumber.getUsageType()) &&
                usageTypeRepository.findByCode(phoneNumber.getUsageType()) == null) {

            throw new BadRequestException("Usage type with code ''{0}'' does not exist", phoneNumber.getUsageType());
        }
    }

    private void validateEmail(EmailExchangeData email){
        if(StringUtils.isNotBlank(email.getUsageType()) &&
                usageTypeRepository.findByCode(email.getUsageType()) == null) {

            throw new BadRequestException("Usage type with code ''{0}'' does not exist", email.getUsageType());
        }
    }

    private void removePortfolioOwnerRelations(CompanyExchangeData company){
        company.getRoles().forEach(roleExchangeData -> {
            roleExchangeData.getRelations().removeIf(employeeCustomerRelation -> employeeCustomerRelation.getEmployeeRole().equals("PORTFOLIO_OWNER"));
        });
    }
    private void sanitizeQueueData(CompanyImportQueue companyImportQueue){
        if("KKTC".equalsIgnoreCase(companyImportQueue.getCompany().getCountryCode())){
            companyImportQueue.getCompany().setCountryCode("CY");
        }
        companyImportQueue.getCompany().getLocations().forEach(locationExchangeData -> {
            if("KKTC".equalsIgnoreCase(locationExchangeData.getCountryCode())){
                locationExchangeData.setCountryCode("CY");
            }
        });
    }

    @Transactional
    public CompanyImportQueue save(CompanyImportQueue companyImportQueue){
        sanitizeQueueData(companyImportQueue);
        validateQueue(companyImportQueue);
        validateCompany(companyImportQueue.getCompany());
        removePortfolioOwnerRelations(companyImportQueue.getCompany());
        companyImportQueue.refreshData();
        companyImportQueue.toProperCase();
        companyImportQueue.copySummaryFromCompany();
        companyImportQueue.setStatus(ImportQueueStatus.PENDING);
        String applicationCompanyId = companyImportQueue.getApplicationCompanyId();
        markExistingQueueItemsAsOutOfDate(applicationCompanyId);
        CompanyImportQueue savedCompanyImportQueue = importQueueRepository.save(companyImportQueue);
        publisher.publishEvent(CompanyImportQueueEvent.with(savedCompanyImportQueue, CompanyImportQueueEvent.Operation.IMPORT));
        return savedCompanyImportQueue;
    }

    private void markExistingQueueItemsAsOutOfDate(String applicationCompanyId){
        List<CompanyImportQueue> companyImportQueues = importQueueRepository.findByApplicationCompanyIdAndStatus(applicationCompanyId, ImportQueueStatus.PENDING);
        for (CompanyImportQueue queue:companyImportQueues) {
            queue.setStatus(ImportQueueStatus.OUT_OF_DATE);
        }
        importQueueRepository.save(companyImportQueues);
    }

    @Transactional
    public Company completeQueueAndSaveCompany(CompanyImportQueue companyImportQueue, Company company){
        setSuccessful(companyImportQueue);
        Company saved = companySaveService.save(company);
        publisher.publishEvent(CompanyImportQueueEvent.with(companyImportQueue, Operation.IMPORT_COMPLETED));
        return saved;
    }

    @Transactional
    public CompanyImportQueue setSuccessful(CompanyImportQueue companyImportQueue){
        importQueueProgressService.finishImport(companyImportQueue.getId());
        companyImportQueue.setStatusSuccess(sessionOwner.getCurrentUser().getUsername());
        return importQueueRepository.save(companyImportQueue);
    }

    public CompanyImportQueue findLastImportQueueItemForLocation(RemoteApplication application, Long companyId, Long locationId) {

        CompanyImportQueue result = null;
        int page = 0;
        int pageSize = 10;
        boolean goon = true;

        while (goon) {
            Pageable pageable = new PageRequest(page, pageSize, Sort.Direction.DESC, "createDate.dateTime");
            Page<CompanyImportQueue> pageData = importQueueRepository.findByApplicationAndCompanyId(application, companyId, pageable);
            if (CollectionUtils.isEmpty(pageData.getContent())) {
                goon = false;
            } else {
                for (CompanyImportQueue companyImportQueue : pageData.getContent()) {
                    Set<Long> locationIds = new HashSet<>(extractLocationIds(companyImportQueue));
                    if (locationIds.contains(locationId)) {
                        result = companyImportQueue;
                        goon = false;
                        break;
                    }
                }
                page++;
            }
        }

        return result;
    }

    private List<Long> extractLocationIds(CompanyImportQueue companyImportQueue) {

        List<Long> locationIds = new ArrayList<>();

        if (StringUtils.isNotBlank(companyImportQueue.getData())) {

            JSONObject rootObject = toJsonObject(companyImportQueue.getData(), true);

            if (rootObject != null) {
                JSONArray locationArray = getJsonArray(rootObject, "locations", true);
                if (locationArray != null) {
                    for (int i = 0; i < locationArray.length(); i++) {
                        JSONObject locationObject = getJsonObjectAtIndex(locationArray, i, true);
                        if (locationObject != null) {
                            Long kartoteksId = getPropertyAsLong(locationObject, "kartoteksId", true);
                            if (kartoteksId != null) {
                                locationIds.add(kartoteksId);
                            }
                        }
                    }
                }
            }
        }

        return locationIds;
    }

    private JSONObject toJsonObject(String json, boolean ignoreException) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return jsonObject;
    }

    private JSONArray getJsonArray(JSONObject jsonObject, String key, boolean ignoreException) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return jsonArray;
    }

    private JSONObject getJsonObjectAtIndex(JSONArray jsonArray, int index, boolean ignoreException) {
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return jsonObject;
    }

    private Long getPropertyAsLong(JSONObject jsonObject, String key, boolean ignoreException) {
        Long value = null;
        try {
            value = jsonObject.getLong(key);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return value;
    }
}
