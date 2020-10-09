package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.kartoteks.domain.CompanyContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactExchangeData {

    private Long kartoteksId;
    private String contactId;
    private String firstName;
    private String lastName;
    private String gender;
    private String departmentCode;
    private String title;
    private String locationName;
    private String linkedinUrl;
    private boolean isActive;
    private boolean isDefault;
    private List<String> segmentCodes = new ArrayList<>();
    private List<PhoneNumberExchangeData> phoneNumbers = new ArrayList<>();
    private List<EmailExchangeData> emails = new ArrayList<>();


    public static ContactExchangeData fromContact(CompanyContact contact){
        ContactExchangeData exchangeData = new ContactExchangeData();
        exchangeData.setKartoteksId(contact.getId());
        exchangeData.setFirstName(contact.getFirstName());
        exchangeData.setLastName(contact.getLastName());
        exchangeData.setGender(contact.getGender().name());
        exchangeData.setDepartmentCode(contact.getDepartment() != null ? contact.getDepartment().getCode() : null);
        exchangeData.setTitle(contact.getTitle() != null ? contact.getTitle().getCode() : null);
        exchangeData.setLocationName(contact.getCompanyLocation() != null ? contact.getCompanyLocation().getName() : null);
        exchangeData.setLinkedinUrl(contact.getLinkedinUrl());
        exchangeData.setActive(contact.isActive());
        exchangeData.setDefault(contact.isDefault());

        if(contact.getCompanyServiceTypes() != null){
            contact.getCompanyServiceTypes().forEach(serviceType -> exchangeData.getSegmentCodes().add(serviceType.getCode()));
        }
        contact.getPhoneNumbers().forEach(phoneNumberWithType ->
                exchangeData.getPhoneNumbers().add(PhoneNumberExchangeData.fromPhoneNumber(phoneNumberWithType))
        );
        contact.getEmails().forEach(emailWithType -> exchangeData.getEmails().add(EmailExchangeData.fromEmail(emailWithType)));
        return exchangeData;
    }


    public Long getKartoteksId() {
        return kartoteksId;
    }

    public void setKartoteksId(Long kartoteksId) {
        this.kartoteksId = kartoteksId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public List<String> getSegmentCodes() {
        return segmentCodes;
    }

    public void setSegmentCodes(List<String> segmentCodes) {
        this.segmentCodes = segmentCodes;
    }

    public List<PhoneNumberExchangeData> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberExchangeData> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<EmailExchangeData> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailExchangeData> emails) {
        this.emails = emails;
    }
}
