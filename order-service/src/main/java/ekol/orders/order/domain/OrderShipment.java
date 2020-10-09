package ekol.orders.order.domain;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.domain.PaymentMethod;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;
import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "OrderShipment.withOrder",
                attributeNodes = {
                        @NamedAttributeNode(value = "order")
                }
        ),
        @NamedEntityGraph(
                name = "OrderShipment.withOrderNumbers",
                attributeNodes = {
                        @NamedAttributeNode(value = "customerOrderNumbers"),
                        @NamedAttributeNode(value = "senderOrderNumbers"),
                        @NamedAttributeNode(value = "consigneeOrderNumbers"),
                        @NamedAttributeNode(value = "loadingOrderNumbers"),
                        @NamedAttributeNode(value = "unloadingOrderNumbers")
                }
        ),
        @NamedEntityGraph(
                name = "OrderShipment.withDocuments",
                attributeNodes = {
                        @NamedAttributeNode(value = "documents", subgraph = "Document.withType")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "Document.withType",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "document")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "OrderShipment.withUnits",
                attributeNodes = {
                        @NamedAttributeNode(value = "units")
                }
        )
})
@Entity
@Where(clause = "deleted = 0")
@Data
@EqualsAndHashCode(callSuper=true)
public class OrderShipment extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8664155050702621878L;

	@Id
    @SequenceGenerator(name = "seq_order_shipment", sequenceName = "seq_order_shipment")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    @Column
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incotermId")
    private Incoterm incoterm;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "readyAtDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "readyAtDateTimezone"))
    })
    private FixedZoneDateTime readyAtDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "deliveryDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "deliveryDateTimezone"))
    })
    private FixedZoneDateTime deliveryDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "company.id", column = @Column(name = "senCompanyId")),
            @AttributeOverride(name = "company.name", column = @Column(name = "senCompanyName")),
            @AttributeOverride(name = "companyLocation.id", column = @Column(name = "senCompanyLocationId")),
            @AttributeOverride(name = "companyLocation.name", column = @Column(name = "senCompanyLocationName")),
            @AttributeOverride(name = "companyContact.id", column = @Column(name = "senCompanyContactId")),
            @AttributeOverride(name = "companyContact.name", column = @Column(name = "senCompanyContactName")),
            @AttributeOverride(name = "handlingCompany.id", column = @Column(name = "senHandlingCompanyId")),
            @AttributeOverride(name = "handlingCompany.name", column = @Column(name = "senHandlingCompanyName")),
            @AttributeOverride(name = "handlingLocation.id", column = @Column(name = "senHandlingLocationId")),
            @AttributeOverride(name = "handlingLocation.name", column = @Column(name = "senHandlingLocationName")),
            @AttributeOverride(name = "handlingContact.id", column = @Column(name = "senHandlingContactId")),
            @AttributeOverride(name = "handlingContact.name", column = @Column(name = "senHandlingContactName")),
            @AttributeOverride(name = "handlingRegion.id", column = @Column(name = "senRegionId")),
            @AttributeOverride(name = "handlingRegion.name", column = @Column(name = "senRegionName")),
            @AttributeOverride(name = "handlingOperationRegion.id", column = @Column(name = "senOperationRegionId")),
            @AttributeOverride(name = "handlingOperationRegion.name", column = @Column(name = "senOperationRegionName")),
            @AttributeOverride(name = "handlingRegionCategory.code", column = @Column(name = "senRegionCategoryCode")),
            @AttributeOverride(name = "handlingRegionCategory.name", column = @Column(name = "senRegionCategoryName")),
            @AttributeOverride(name = "handlingAtCrossDock", column = @Column(name = "senHandlingAtCrossDock")),
            @AttributeOverride(name = "handlingLocationCountryCode", column = @Column(name = "senHandlingCountryCode")),
            @AttributeOverride(name = "handlingLocationPostalCode", column = @Column(name = "senHandlingPostalCode")),
            @AttributeOverride(name = "handlingLocationTimezone", column = @Column(name = "senHandlingTimezone")),
            @AttributeOverride(name = "handlingCompanyType", column = @Column(name = "senHandlingCompanyType"))
    })
    private ShipmentHandlingParty sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "company.id", column = @Column(name = "conCompanyId")),
            @AttributeOverride(name = "company.name", column = @Column(name = "conCompanyName")),
            @AttributeOverride(name = "companyLocation.id", column = @Column(name = "conCompanyLocationId")),
            @AttributeOverride(name = "companyLocation.name", column = @Column(name = "conCompanyLocationName")),
            @AttributeOverride(name = "companyContact.id", column = @Column(name = "conCompanyContactId")),
            @AttributeOverride(name = "companyContact.name", column = @Column(name = "conCompanyContactName")),
            @AttributeOverride(name = "handlingCompany.id", column = @Column(name = "conHandlingCompanyId")),
            @AttributeOverride(name = "handlingCompany.name", column = @Column(name = "conHandlingCompanyName")),
            @AttributeOverride(name = "handlingLocation.id", column = @Column(name = "conHandlingLocationId")),
            @AttributeOverride(name = "handlingLocation.name", column = @Column(name = "conHandlingLocationName")),
            @AttributeOverride(name = "handlingContact.id", column = @Column(name = "conHandlingContactId")),
            @AttributeOverride(name = "handlingContact.name", column = @Column(name = "conHandlingContactName")),
            @AttributeOverride(name = "handlingRegion.id", column = @Column(name = "conRegionId")),
            @AttributeOverride(name = "handlingRegion.name", column = @Column(name = "conRegionName")),
            @AttributeOverride(name = "handlingOperationRegion.id", column = @Column(name = "conOperationRegionId")),
            @AttributeOverride(name = "handlingOperationRegion.name", column = @Column(name = "conOperationRegionName")),
            @AttributeOverride(name = "handlingRegionCategory.code", column = @Column(name = "conRegionCategoryCode")),
            @AttributeOverride(name = "handlingRegionCategory.name", column = @Column(name = "conRegionCategoryName")),
            @AttributeOverride(name = "handlingAtCrossDock", column = @Column(name = "conHandlingAtCrossDock")),
            @AttributeOverride(name = "handlingLocationCountryCode", column = @Column(name = "conHandlingCountryCode")),
            @AttributeOverride(name = "handlingLocationPostalCode", column = @Column(name = "conHandlingPostalCode")),
            @AttributeOverride(name = "handlingLocationTimezone", column = @Column(name = "conHandlingTimezone")),
            @AttributeOverride(name = "handlingCompanyType", column = @Column(name = "conHandlingCompanyType"))
    })
    private ShipmentHandlingParty consignee;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "company.id", column = @Column(name = "mfrCompanyId")),
        @AttributeOverride(name = "company.name", column = @Column(name = "mfrCompanyName")),
        @AttributeOverride(name = "companyLocation.id", column = @Column(name = "mfrCompanyLocationId")),
        @AttributeOverride(name = "companyLocation.name", column = @Column(name = "mfrCompanyLocationName")),
        @AttributeOverride(name = "companyContact.id", column = @Column(name = "mfrCompanyContactId")),
        @AttributeOverride(name = "companyContact.name", column = @Column(name = "mfrCompanyContactName"))
    })
    private ShipmentParty manufacturer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime.dateTime", column = @Column(name = "loadingAppStartDateTime")),
            @AttributeOverride(name = "startDateTime.timeZone", column = @Column(name = "loadingAppStartTimeZone")),
            @AttributeOverride(name = "endDateTime.dateTime", column = @Column(name = "loadingAppEndDateTime")),
            @AttributeOverride(name = "endDateTime.timeZone", column = @Column(name = "loadingAppEndTimeZone"))
    })
    private Appointment loadingAppointment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDateTime.dateTime", column = @Column(name = "unloadingAppStartDateTime")),
            @AttributeOverride(name = "startDateTime.timeZone", column = @Column(name = "unloadingAppStartTimeZone")),
            @AttributeOverride(name = "endDateTime.dateTime", column = @Column(name = "unloadingAppEndDateTime")),
            @AttributeOverride(name = "endDateTime.timeZone", column = @Column(name = "unloadingAppEndTimeZone"))
    })
    private Appointment unloadingAppointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethodId")
    private PaymentMethod paymentMethod;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "valueOfGoodsAmount")),
            @AttributeOverride(name = "currency", column = @Column(name = "valueOfGoodsCurrency"))
    })
    private AmountWithCurrency valueOfGoods;

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private List<OrderShipmentUnit> units = new ArrayList<>();
    
    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private List<OrderShipmentDefinitionOfGoods> definitionOfGoods = new ArrayList<>();

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean hangingLoad;
    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean longLoad;
    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean oversizeLoad;
    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean heavyLoad;
    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean valuableLoad;

    @Column
    private Integer totalQuantity;

    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_UNIT_PACK_TYPE",
    joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<PackageType> packageTypes = new HashSet<>();

    @Column
    private BigDecimal grossWeight;

    @Column
    private BigDecimal netWeight;

    @Column
    private BigDecimal totalVolume;

    @Column
    private BigDecimal totalLdm;

    @Column
    private BigDecimal payWeight;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean insured;

    @Column
    private Integer temperatureMinValue;

    @Column
    private Integer temperatureMaxValue;

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private List<OrderShipmentAdr> adrDetails = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_HEALTH_CERT",
            joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<DocumentType> healthCertificateTypes = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "borderCustomsId")),
            @AttributeOverride(name = "name", column = @Column(name = "borderCustomsName"))
    })
    private IdNameEmbeddable borderCustoms;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean borderCrossingHealthCheck;

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private List<OrderShipmentVehicleRequirement> vehicleRequirements = new ArrayList<>();

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private List<OrderShipmentEquipmentRequirement> equipmentRequirements = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_CUS_ORDER_NO",
            joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<String> customerOrderNumbers = new HashSet<>();
   
    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_SEN_ORDER_NO",
            joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<String> senderOrderNumbers = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_CON_ORDER_NO",
            joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<String> consigneeOrderNumbers = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_LOAD_ORDER_NO",
    		joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<String> loadingOrderNumbers = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "ORDER_SHIPMENT_UNLOAD_ORDER_NO",
    		joinColumns = @JoinColumn(name = "SHIPMENT_ID"))
    private Set<String> unloadingOrderNumbers = new HashSet<>();


    @OneToOne(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private OrderShipmentDepartureCustoms departureCustoms;

    @OneToOne(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private OrderShipmentArrivalCustoms arrivalCustoms;

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    private List<OrderShipmentDocument> documents = new ArrayList<>();
    
    @OneToMany(mappedBy="shipment")
    @Where(clause="deleted=0")
    private List<OrderShipmentIdMapping> mappedIds = new ArrayList<>() ;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @Column(length = 100)
    @CreatedBy
    private String createdBy;

    @Override
    @PrePersist
    public void prePersist() {
    	ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    	if (null == createdAt && null == id) {
    		createdAt = new UtcDateTime(now.toLocalDateTime());
        }
        super.prePersist();
    }

    public FixedZoneDateTime findReadyDate(){
        if(getLoadingAppointment() != null && getLoadingAppointment().getStartDateTime() != null){
            return getLoadingAppointment().getStartDateTime();
        }
        return getReadyAtDate();
    }

    public void clearEmptyOrderNumbers(){
        setCustomerOrderNumbers(getCustomerOrderNumbers().stream().filter(StringUtils::isNotBlank).collect(toSet()));
        setSenderOrderNumbers(getSenderOrderNumbers().stream().filter(StringUtils::isNotBlank).collect(toSet()));
        setConsigneeOrderNumbers(getConsigneeOrderNumbers().stream().filter(StringUtils::isNotBlank).collect(toSet()));
    }

    public List<OrderShipmentVehicleRequirement> filterVehicleRequirements(VehicleFeature vehicleFeature, OrderPlanningOperationType operationType, VehicleRequirementReason reason){
        return getVehicleRequirements().stream()
                .filter(requirement ->
                        (vehicleFeature == null || requirement.getRequirement().equals(vehicleFeature)) &&
                        (operationType == null || requirement.getOperationType().equals(operationType)) &&
                        (reason == null || requirement.getRequirementReason().equals(reason))
                ).collect(toList());
    }
    public boolean hasVehicleRequirement(VehicleFeature vehicleFeature, OrderPlanningOperationType operationType, VehicleRequirementReason reason){
        return !filterVehicleRequirements(vehicleFeature, operationType, reason).isEmpty();
    }

    public void deleteVehicleRequirements(VehicleFeature vehicleFeature, OrderPlanningOperationType operationType, VehicleRequirementReason reason){
        filterVehicleRequirements(vehicleFeature, operationType, reason)
                .forEach(requirement -> requirement.setDeleted(true));
    }

    public boolean canCalculatePayWeight(){
        return getGrossWeight() != null && getTotalVolume() != null && getTotalLdm() != null;
    }

}
