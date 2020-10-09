package ekol.location.domain.location.customs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.location.comnon.EmailWithType;
import ekol.location.domain.location.comnon.PhoneNumberWithType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomsOfficeContact extends BaseEntity{

    @Id
    @SequenceGenerator(name = "seq_customs_office_contact", sequenceName = "seq_customs_office_contact")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customs_office_contact")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customsOfficeId")
    @JsonBackReference("contacts")
    private CustomsOffice customsOffice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customsOfficeLocationId")
    @JsonSerialize(using = ContactLocationSerializer.class)
    private CustomsOfficeLocation customsLocation;

    @Column
    private String gender;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="customs_office_contact_phone",
            joinColumns=@JoinColumn(name = "customsOfficeContactId"))
    private Set<PhoneNumberWithType> phoneNumbers = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="customs_office_contact_email",
            joinColumns=@JoinColumn(name = "customsOfficeContactId"))
    private Set<EmailWithType> emails = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomsOffice getCustomsOffice() {
        return customsOffice;
    }

    public void setCustomsOffice(CustomsOffice customsOffice) {
        this.customsOffice = customsOffice;
    }

    public CustomsOfficeLocation getCustomsLocation() {
        return customsLocation;
    }

    public void setCustomsLocation(CustomsOfficeLocation customsLocation) {
        this.customsLocation = customsLocation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Set<PhoneNumberWithType> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumberWithType> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<EmailWithType> getEmails() {
        return emails;
    }

    public void setEmails(Set<EmailWithType> emails) {
        this.emails = emails;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullname(){
        return getFirstName() + " " + getLastName();
    }
}
