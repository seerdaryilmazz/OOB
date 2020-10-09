package ekol.location.domain.location.customerwarehouse;

import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ekol.location.domain.LocationType;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.location.comnon.CustomsDetails;
import ekol.location.domain.location.comnon.ExternalSystemId;
import ekol.location.domain.location.comnon.ExternalSystemId.CollectionDeserializer;
import ekol.location.domain.location.comnon.ExternalSystemId.EntityNameValue;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.location.domain.location.comnon.Place;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by burak on 13/04/17.
 */
@Entity
@Table(name="customer_warehouse")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class CustomerWarehouse extends Place {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7352926130555452547L;

	@Id
    @SequenceGenerator(name = "seq_warehouse", sequenceName = "seq_warehouse")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_warehouse")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "companyId")),
            @AttributeOverride(name = "name", column = @Column(name = "companyName"))
    })
    private IdNameEmbeddable company;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "companyLocationId")),
            @AttributeOverride(name = "name", column = @Column(name = "companyLocationName"))
    })
    private IdNameEmbeddable companyLocation;

    @Enumerated(EnumType.STRING)
    private WarehouseCompanyType companyType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bookingUrl", column = @Column(name = "loadingBookingUrl")),
            @AttributeOverride(name = "bookingContact.id", column = @Column(name = "loadingBookingContactId")),
            @AttributeOverride(name = "bookingContact.name", column = @Column(name = "loadingBookingContactName")),
            @AttributeOverride(name = "bookingType", column = @Column(name = "loadingBookingType")),
            @AttributeOverride(name = "bookingOption", column = @Column(name = "loadingBookingOption")),
            @AttributeOverride(name = "bookingDaysBefore", column = @Column(name = "loadingBookingDaysBefore")),
            @AttributeOverride(name = "bookingTimeUntil", column = @Column(name = "loadingBookingTimeUntil"))
    })
    private CustomerWarehouseBooking bookingLoading;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bookingUrl", column = @Column(name = "unloadingBookingUrl")),
            @AttributeOverride(name = "bookingContact.id", column = @Column(name = "unloadingBookingContactId")),
            @AttributeOverride(name = "bookingContact.name", column = @Column(name = "unloadingBookingContactName")),
            @AttributeOverride(name = "bookingType", column = @Column(name = "unloadingBookingType")),
            @AttributeOverride(name = "bookingOption", column = @Column(name = "unloadingBookingOption")),
            @AttributeOverride(name = "bookingDaysBefore", column = @Column(name = "unloadingBookingDaysBefore")),
            @AttributeOverride(name = "bookingTimeUntil", column = @Column(name = "unloadingBookingTimeUntil"))
    })
    private CustomerWarehouseBooking bookingUnloading;

    private Integer trailerMinRequired;
    private Integer trailerCapacity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "registrationCompanyId")),
            @AttributeOverride(name = "name", column = @Column(name = "registrationCompanyName"))
    })
    private IdNameEmbeddable registrationCompany;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "registrationCompanyLocId")),
            @AttributeOverride(name = "name", column = @Column(name = "registrationCompanyLocName"))
    })
    private IdNameEmbeddable registrationCompanyLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "docClaimCompanyId")),
            @AttributeOverride(name = "name", column = @Column(name = "docClaimCompanyName"))
    })
    private IdNameEmbeddable documentClaimCompany;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "docClaimCompanyLocId")),
            @AttributeOverride(name = "name", column = @Column(name = "docClaimCompanyLocName"))
    })
    private IdNameEmbeddable documentClaimCompanyLocation;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customsDetailsId")
    private CustomsDetails customsDetails;
    
    @Embedded
    @ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ExternalSystemId", joinColumns = {@JoinColumn(name = "parentId")})
    @AttributeOverrides({
    	@AttributeOverride(name="entityName", column=@Column(name="ENTITY_NAME")),
    	@AttributeOverride(name="externalSystem", column=@Column(name="EXTERNAL_SYSTEM")),
    	@AttributeOverride(name="externalId", column=@Column(name="EXTERNAL_ID"))
    })
    @Where(clause="ENTITY_NAME = 'CustomerWarehouse'")
    @EntityNameValue("CustomerWarehouse")
    @JsonDeserialize(using=CollectionDeserializer.class)
    private Collection<ExternalSystemId> externalIds;
    
    @Override
    public LocationType getType() {
        return LocationType.CUSTOMER_WAREHOUSE;
    }

    public enum BookingLatestType {
        CONFIRMATION, ARRIVAL
    };
}
