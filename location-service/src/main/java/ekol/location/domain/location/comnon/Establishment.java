package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.Country;
import ekol.location.domain.location.comnon.workinghour.WorkingHour;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by burak on 03/04/17.
 */
@Entity
@Table(name="plEstablishment")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Establishment extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_pl_establishment", sequenceName = "seq_pl_establishment")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pl_establishment")
    private Long id;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="pl_phone",
            joinColumns=@JoinColumn(name = "establishmentId"))
    private Set<PhoneNumberWithType> phoneNumbers = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "establishment")
    @JsonManagedReference
    private Set<WorkingHour> workingHours;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "ownerId")),
            @AttributeOverride(name = "name", column = @Column(name = "ownerName"))
    })
    private IdNameEmbeddable owner;

    @Embedded
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PhoneNumberWithType> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumberWithType> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<WorkingHour> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Set<WorkingHour> workingHours) {
        this.workingHours = workingHours;
    }

    public IdNameEmbeddable getOwner() {
        return owner;
    }

    public void setOwner(IdNameEmbeddable owner) {
        this.owner = owner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonIgnore
    public Country getCountry(){
        return getAddress() != null ? getAddress().getCountry() : null;
    }
    public void setCountry(Country country){
        if(getAddress() != null){
            getAddress().setCountry(country);
        }
    }
    @JsonIgnore
    public String getCountryName(){
        return getCountry() != null ? getCountry().getName() : null;
    }
    @JsonIgnore
    public String getCountryIso(){
        return getCountry() != null ? getCountry().getIso() : null;
    }
}
