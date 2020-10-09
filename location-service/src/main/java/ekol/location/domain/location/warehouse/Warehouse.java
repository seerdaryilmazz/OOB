package ekol.location.domain.location.warehouse;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ekol.location.domain.LinehaulRouteLegStop;
import ekol.location.domain.LinehaulRouteLegStoppable;
import ekol.location.domain.LocationType;
import ekol.location.domain.location.comnon.CustomsDetails;
import ekol.location.domain.location.comnon.ExternalSystemId;
import ekol.location.domain.location.comnon.ExternalSystemId.CollectionDeserializer;
import ekol.location.domain.location.comnon.ExternalSystemId.EntityNameValue;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.location.domain.location.comnon.Place;
import ekol.location.domain.location.enumeration.WarehouseOwnerType;
import ekol.location.domain.location.warehouse.dto.WarehouseRampGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by kilimci on 02/05/2017.
 */
@Entity
@Table(name="warehouse")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class Warehouse extends Place implements LinehaulRouteLegStoppable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6724163644831508566L;


	public static final int DEFAULT_FLOOR_NUMBER = 1;


    @Id
    @SequenceGenerator(name = "seq_warehouse", sequenceName = "seq_warehouse")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_warehouse")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "routeLegStopId")
    private LinehaulRouteLegStop routeLegStop;

    private BigDecimal area;
    private BigDecimal storageVolume;
    private Integer numberOfFloors;
    private Integer numberOfRamps;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "companyLocationId")),
            @AttributeOverride(name = "name", column = @Column(name = "companyLocationName"))
    })
    private IdNameEmbeddable companyLocation;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "companyId")),
            @AttributeOverride(name = "name", column = @Column(name = "companyName"))
    })
    private IdNameEmbeddable company;

    @Enumerated(EnumType.STRING)
    private WarehouseOwnerType warehouseOwnerType;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<WarehouseZone> zone = new HashSet<>();

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
    @Where(clause = "deleted = 0")
    @OrderBy("rampNo ASC")
    private Set<WarehouseRamp> ramp = new HashSet<>();

    @Transient
    @JsonManagedReference
    private Set<WarehouseRampGroup> rampGroup = new HashSet<>();

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
    @Where(clause="ENTITY_NAME = 'Warehouse'")
    @EntityNameValue("Warehouse")
    @JsonDeserialize(using=CollectionDeserializer.class)
    private Collection<ExternalSystemId> externalIds;

    @JsonProperty("rampNumbers")
    public String rampNumbers() {
        return getRampGroup() == null ? "" : getRampGroup().stream().map
                (r-> {return (r.retrieveRampNumberIntervalAsString());})
                .collect(Collectors.joining(","));
    }

    @Override
    public LinehaulRouteLegStop getRouteLegStop() {
        return routeLegStop;
    }

    @Override
    public LocationType getType() {
        return LocationType.CROSSDOCK_WAREHOUSE;
    }

    public LinehaulRouteLegStop buildRouteLegStop() {
        if(getRouteLegStop() != null){
            LinehaulRouteLegStop routeLegStop = getRouteLegStop();
            routeLegStop.copyFrom(this);
            return routeLegStop;
        }
        return LinehaulRouteLegStop.withPlace(this);
    }

}
