package ekol.kartoteks.service;

import java.util.Objects;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.common.Country;
import ekol.kartoteks.domain.exchange.*;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.repository.common.*;

/**
 * Created by kilimci on 05/05/16.
 */
@Component
public class CompanyExchangeDataConverter {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private TaxOfficeRepository taxOfficeRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private CompanyTypeRepository companyTypeRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private ContactDepartmentRepository contactDepartmentRepository;
    @Autowired
    private ContactTitleRepository contactTitleRepository;
    @Autowired
    private PhoneTypeRepository phoneTypeRepository;
    @Autowired
    private UsageTypeRepository usageTypeRepository;
    @Autowired
    private BusinessSegmentTypeService businessSegmentTypeService;
    @Autowired
    private EmployeeCustomerRelationRepository employeeCustomerRelationRepository;
    @Autowired
    private CompanyRoleTypeRepository companyRoleTypeRepository;
    @Autowired
    private CompanyRelationTypeRepository companyRelationTypeRepository;
    @Autowired
    private SalesPortfolioRepository salesPortfolioRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    public Company convertCompany(RemoteApplication application, CompanyExchangeData exchangeData){
        Company company = new Company();
        company.setName(exchangeData.getName());
        company.setCountry(countryRepository.findByIsoIgnoreCase(exchangeData.getCountryCode()));
        company.setId(exchangeData.getKartoteksId());
        company.setEmailDomain(exchangeData.getEmailDomain());
        company.setWebsite(exchangeData.getWebsite());
        company.setLocalName(exchangeData.getName());
        company.setLogoUrl(exchangeData.getLogoUrl());
        company.setTaxId(exchangeData.getTaxId());
        company.setTaxOfficeCode(exchangeData.getTaxofficeCode());
        company.setTaxOffice(taxOfficeRepository.findByCode(exchangeData.getTaxofficeCode()));
        company.setTckn(exchangeData.getTckn());
        company.setType(companyTypeRepository.findByCode(exchangeData.getType()));
        company.setOwnedByEkol(exchangeData.isOwnedByEkol());
        company.setEoriNumber(exchangeData.getEoriNumber());
        company.getMappedIds().add(CompanyIdMapping.withApplication(application, exchangeData.getCompanyId()));
        if(StringUtils.isNotBlank(exchangeData.getSalesforceId())){
            company.getMappedIds().add(CompanyIdMapping.withApplication(RemoteApplication.SALESFORCE, exchangeData.getSalesforceId()));
        }
        if(exchangeData.getLocations() != null){
            exchangeData.getLocations().forEach(location -> company.getCompanyLocations().add(convertCompanyLocation(application, company, location)));
        }
        if(exchangeData.getContacts() != null){
            exchangeData.getContacts().forEach(contact -> company.getCompanyContacts().add(convertCompanyContact(application, company, contact)));
        }
        if(exchangeData.getSectors() != null){
            exchangeData.getSectors().forEach(sector -> company.getSectors().add(convertSector(company, sector)));
        }
        if(exchangeData.getRoles() != null){
            exchangeData.getRoles().forEach(role -> company.getRoles().add(convertCompanyRole(company, role)));
        }
        if(exchangeData.getRelations() != null){
            exchangeData.getRelations().forEach(relation -> company.getActiveRelations().add(convertCompanyRelation(company, relation)));
        }
        return company;
    }
    public CompanyLocation convertCompanyLocation(RemoteApplication application, Company company, LocationExchangeData exchangeData){
        CompanyLocation location = new CompanyLocation();
        location.setCompany(company);
        location.setId(exchangeData.getKartoteksId());
        location.setActive(exchangeData.isActive());
        location.setDefault(exchangeData.isDefault());
        location.setName(exchangeData.getName());
        location.setPostaladdress(convertPostalAddress(exchangeData));
        location.setCustomsCode(exchangeData.getCustomsCode());
        location.getMappedIds().add(CompanyLocationIdMapping.withApplication(application, exchangeData.getLocationId()));
        if(StringUtils.isNotBlank(exchangeData.getSalesforceId())){
            location.getMappedIds().add(CompanyLocationIdMapping.withApplication(RemoteApplication.SALESFORCE, exchangeData.getSalesforceId()));
        }
        if(exchangeData.getTypes() != null){
            exchangeData.getTypes().forEach(type -> location.getLocationTypes().add(locationTypeRepository.findByCode(type)));
        }
        if(exchangeData.getPhoneNumbers() != null){
            exchangeData.getPhoneNumbers().forEach(phoneNumber -> location.getPhoneNumbers().add(convertPhoneNumber(company.getCountry(), phoneNumber)));
        }

        return location;
    }
    public CompanyContact convertCompanyContact(RemoteApplication application, Company company, ContactExchangeData exchangeData){
        CompanyContact contact = new CompanyContact();
        contact.setCompany(company);
        contact.setId(exchangeData.getKartoteksId());
        contact.setFirstName(exchangeData.getFirstName());
        contact.setLastName(exchangeData.getLastName());
        contact.setGender(EnumUtils.getEnum(Gender.class, exchangeData.getGender()));
        contact.setActive(exchangeData.isActive());
        contact.setDefault(exchangeData.isDefault());
        contact.setTitle(contactTitleRepository.findByCode(exchangeData.getTitle()));
        contact.setLinkedinUrl(exchangeData.getLinkedinUrl());
        contact.setCompanyLocation(CompanyLocation.withName(exchangeData.getLocationName()));
        contact.setDepartment(contactDepartmentRepository.findByCode(exchangeData.getDepartmentCode()));
        contact.getMappedIds().add(CompanyContactIdMapping.withApplication(application, exchangeData.getContactId()));
        if(exchangeData.getPhoneNumbers() != null){
            exchangeData.getPhoneNumbers().forEach(phoneNumber -> contact.getPhoneNumbers().add(convertPhoneNumber(company.getCountry(), phoneNumber)));
        }
        if(exchangeData.getEmails() != null){
            exchangeData.getEmails().forEach(email -> contact.getEmails().add(convertEmail(email)));
        }
        if(exchangeData.getSegmentCodes() != null){
            exchangeData.getSegmentCodes().stream().map(businessSegmentTypeService::findByCode).filter(Objects::nonNull).forEach(contact.getCompanyServiceTypes()::add);
        }
        return contact;
    }
    public CompanyRole convertCompanyRole(Company company, RoleExchangeData roleExchangeData){
        CompanyRole role = new CompanyRole();
        role.setCompany(company);
        role.setDateRange(new DateWindow(roleExchangeData.getStartDate(), roleExchangeData.getEndDate()));
        role.setRoleType(companyRoleTypeRepository.findByCode(roleExchangeData.getRelationCode()));
        role.setSegmentType(businessSegmentTypeService.findByCode(roleExchangeData.getSegmentCode()));
        roleExchangeData.getRelations().forEach(employeeCustomerRelationExchangeData -> {
            CompanyEmployeeRelation relation = new CompanyEmployeeRelation();
            relation.setCompanyRole(role);
            relation.setEmployeeAccount(employeeCustomerRelationExchangeData.getEmployeeAccount());
            relation.setRelation(employeeCustomerRelationRepository.findByCode(employeeCustomerRelationExchangeData.getEmployeeRole()));
            role.getEmployeeRelations().add(relation);
        });

        return role;
    }
    private CompanySector convertSector(Company company, SectorExchangeData sectorData){
        CompanySector sector = new CompanySector();
        sector.setCompany(company);
        sector.setSector(sectorRepository.findByCode(sectorData.getCode()));
        sector.setDefault(sectorData.isDefault());
        return sector;
    }
    public CompanyRelation convertCompanyRelation(Company company, RelationExchangeData relationExchangeData){
        CompanyRelation relation = new CompanyRelation();
        relation.setActiveCompany(company);
        relation.setPassiveCompany(companyRepository.findNoDetailsById(relationExchangeData.getKartoteksId()));
        relation.setRelationType(companyRelationTypeRepository.findByCode(relationExchangeData.getRelationTypeCode()));
        return relation;
    }
    private PostalAddress convertPostalAddress(LocationExchangeData exchangeData){
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setCity(exchangeData.getCity());
        postalAddress.setCountry(countryRepository.findByIsoIgnoreCase(exchangeData.getCountryCode()));
        postalAddress.setDistrict(exchangeData.getDistrict());
        postalAddress.setLocality(exchangeData.getLocality());
        postalAddress.setPointOnMap(new Point(exchangeData.getLatitude(), exchangeData.getLongitude()));
        postalAddress.setPostalCode(exchangeData.getPostalCode());
        postalAddress.setRegion(exchangeData.getRegion());
        postalAddress.setStreetName(exchangeData.getStreetName());
        postalAddress.setSuburb(exchangeData.getSuburb());
        postalAddress.generateFormattedAddress();
        return postalAddress;
    }
    private PhoneNumberWithType convertPhoneNumber(Country country, PhoneNumberExchangeData exchangeData){
        PhoneNumberWithType phoneNumberWithType = new PhoneNumberWithType();
        phoneNumberWithType.setDefault(exchangeData.isDefault());
        phoneNumberWithType.setUsageType(usageTypeRepository.findByCode(exchangeData.getUsageType()));
        phoneNumberWithType.setNumberType(phoneTypeRepository.findByCode(exchangeData.getNumberType()));
        if(StringUtils.isNotBlank(exchangeData.getCountryCode()) && StringUtils.isNotBlank(exchangeData.getRegionCode())){
            phoneNumberWithType.setPhoneNumber(new PhoneNumber(exchangeData.getCountryCode(), exchangeData.getRegionCode(),
                    exchangeData.getPhone(), exchangeData.getExtension()));
        }else{
            phoneNumberWithType.setPhoneNumber(tryToParsePhoneNumber(country, exchangeData));
        }

        return phoneNumberWithType;
    }
    private EmailWithType convertEmail(EmailExchangeData exchangeData){
        EmailWithType emailWithType = new EmailWithType();
        emailWithType.setUsageType(usageTypeRepository.findByCode(exchangeData.getUsageType()));
        emailWithType.setEmail(new Email(exchangeData.getEmail()));
        return emailWithType;
    }

    private PhoneNumber tryToParsePhoneNumber(Country country, PhoneNumberExchangeData exchangeData){
        String countryCode = String.valueOf(country.getPhoneCode());
        String phoneWithoutCountry = exchangeData.getPhone().trim();
        if(phoneWithoutCountry.startsWith("00")){
            phoneWithoutCountry = phoneWithoutCountry.substring(2).trim();
        }
        if(phoneWithoutCountry.startsWith("+")){
            phoneWithoutCountry = phoneWithoutCountry.substring(1).trim();
        }
        if(phoneWithoutCountry.startsWith(countryCode)){
            phoneWithoutCountry = phoneWithoutCountry.substring(countryCode.length()).trim();
        }
        String[] regionAndPhone = StringUtils.split(phoneWithoutCountry, " -", 2);
        String regionCode = "";
        String phone = "";
        if(regionAndPhone.length == 2){
            regionCode = regionAndPhone[0].trim();
            phone = StringUtils.replace(StringUtils.deleteWhitespace(regionAndPhone[1]), "-", "");
        }
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(countryCode);
        phoneNumber.setRegionCode(regionCode);

        if(StringUtils.isNotBlank(phone)){
            phoneNumber.setPhone(phone);
        }else{
            phoneNumber.setPhone(phoneWithoutCountry);
        }
        phoneNumber.setExtension(exchangeData.getExtension());

        return phoneNumber;
    }
}
