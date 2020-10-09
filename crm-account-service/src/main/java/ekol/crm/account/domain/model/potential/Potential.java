package ekol.crm.account.domain.model.potential;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.dto.potential.PotentialJson;
import ekol.crm.account.domain.dto.potential.PotentialStatus;
import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.*;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.IdNamePair;
import lombok.*;

@Entity
@Table(name = "CrmPotential")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="SERVICE_AREA",
        discriminatorType=DiscriminatorType.STRING
)
@SequenceGenerator(name = "SEQ_CRMPOTENTIAL", sequenceName = "SEQ_CRMPOTENTIAL")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@Audited
public abstract class Potential extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPOTENTIAL")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_country_id")
    private Country fromCountry;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "CRM_PT_FR_COUNTRY_POINT",
            joinColumns = @JoinColumn(name = "POTENTIAL_ID"),
            inverseJoinColumns = @JoinColumn(name = "COUNTRY_POINT_ID"))
    private Set<CountryPoint> fromCountryPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_country_id")
    private Country toCountry;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "CRM_PT_TO_COUNTRY_POINT",
            joinColumns = @JoinColumn(name = "POTENTIAL_ID"),
            inverseJoinColumns = @JoinColumn(name = "COUNTRY_POINT_ID"))
    private Set<CountryPoint> toCountryPoint;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "Crm_Pt_Shipment_LT", joinColumns = @JoinColumn(name = "potential_id"))
    @Column(name = "shipmentLoadingType")
    @Audited
    private Set<ShipmentLoadingType> shipmentLoadingTypes = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private CustomsType customsType;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="COMPETITOR_ID")),
            @AttributeOverride(name = "name", column=@Column(name="COMPETITOR_NAME"))})
    private IdNamePair competitor;

    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column
    private Integer frequency;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @Column(name = "SERVICE_AREA", insertable = false, updatable = false)
    private String serviceArea;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @CreatedBy
    private String createdBy;
    
    @Transient
    public PotentialStatus getStatus() {
    	LocalDate now = LocalDate.now();
    	if(now.isAfter(getValidityEndDate()) || now.isBefore(getValidityStartDate())) {
    		return PotentialStatus.INACTIVE;
    	}
    	return PotentialStatus.ACTIVE;
    }

    public abstract PotentialJson toJson();

}
