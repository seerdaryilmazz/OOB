package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.AdrClass;
import ekol.orders.order.domain.Status;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.elastic.shipment.model.ShipmentSize;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Shipment.allDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "shipmentUnits", subgraph = "ShipmentUnit.allDetails"),
                        @NamedAttributeNode("transportOrder")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "ShipmentUnit.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "type", subgraph = "PackageType.allDetails"),
                                        @NamedAttributeNode("shipmentUnitPackages")
                                }
                        ),
                        @NamedSubgraph(
                                name = "PackageType.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode("packageGroup")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "Shipment.withTransportOrder",
                attributeNodes = {
                        @NamedAttributeNode(value = "transportOrder")
                }
        )
})
@Entity
@Table(name = "Shipment")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shipment extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_shipment", sequenceName = "seq_shipment")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shipment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportOrderId")
    @JsonBackReference
    private TransportOrder transportOrder;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adrClassId")
    private AdrClass adrClass;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "companyId", column = @Column(name = "senderCompanyId")),
            @AttributeOverride(name = "companyContactId", column = @Column(name = "senderCompanyContactId")),
            @AttributeOverride(name = "locationOwnerCompanyId", column = @Column(name = "pickupLocOwnerCompanyId")),
            @AttributeOverride(name = "locationId", column = @Column(name = "pickupLocId")),
            @AttributeOverride(name = "locationContactId", column = @Column(name = "pickupLocContactId")),
            @AttributeOverride(name = "locationRegionId", column = @Column(name = "pickupLocRegionId")),
            @AttributeOverride(name = "locationRegionCategoryId", column = @Column(name = "pickupLocRegionCatId")),
            @AttributeOverride(name = "locationOperationRegionId", column = @Column(name = "pickupLocOpRegionId")),
            @AttributeOverride(name = "locationIsAWarehouse", column = @Column(name = "pickupLocIs_A_Warehouse")),
            @AttributeOverride(name = "warehouse.id", column = @Column(name = "pickupLocWarehouseId")),
            @AttributeOverride(name = "warehouse.name", column = @Column(name = "pickupLocWarehouseName"))
    })
    private SenderOrReceiver sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "companyId", column = @Column(name = "receiverCompanyId")),
            @AttributeOverride(name = "companyContactId", column = @Column(name = "receiverCompanyContactId")),
            @AttributeOverride(name = "locationOwnerCompanyId", column = @Column(name = "deliveryLocOwnerCompanyId")),
            @AttributeOverride(name = "locationId", column = @Column(name = "deliveryLocId")),
            @AttributeOverride(name = "locationContactId", column = @Column(name = "deliveryLocContactId")),
            @AttributeOverride(name = "locationRegionId", column = @Column(name = "deliveryLocRegionId")),
            @AttributeOverride(name = "locationRegionCategoryId", column = @Column(name = "deliveryLocRegionCatId")),
            @AttributeOverride(name = "locationOperationRegionId", column = @Column(name = "deliveryLocOpRegionId")),
            @AttributeOverride(name = "locationIsAWarehouse", column = @Column(name = "deliveryLocIs_A_Warehouse")),
            @AttributeOverride(name = "warehouse.id", column = @Column(name = "deliveryLocWarehouseId")),
            @AttributeOverride(name = "warehouse.name", column = @Column(name = "deliveryLocWarehouseName"))
    })
    private SenderOrReceiver receiver;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "collectionWarehouseId")),
            @AttributeOverride(name = "name", column = @Column(name = "collectionWarehouseName"))
    })
    private IdNamePair collectionWarehouse;

    @Transient
    private IdNamePair collectionWarehouseLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "distributionWarehouseId")),
            @AttributeOverride(name = "name", column = @Column(name = "distributionWarehouseName"))
    })
    private IdNamePair distributionWarehouse;

    @Transient
    private IdNamePair distributionWarehouseLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "readyAtDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "readyAtDateTz"))
    })
    private FixedZoneDateTime readyAtDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "requestedDeliveryDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "requestedDeliveryDateTz"))
    })
    private FixedZoneDateTime requestedDeliveryDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "collectionArrivalDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "collectionArrivalDateTz"))
    })
    private FixedZoneDateTime collectionArrivalDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "linehaulArrivalDate")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "linehaulArrivalDateTz"))
    })
    private FixedZoneDateTime linehaulArrivalDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "pickupAppointmentStart")),
            @AttributeOverride(name = "end", column = @Column(name = "pickupAppointmentEnd")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "pickupAppointmentTz"))
    })
    private FixedZoneDateTimeWindow pickupAppointment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "start", column = @Column(name = "deliveryAppointmentStart")),
            @AttributeOverride(name = "end", column = @Column(name = "deliveryAppointmentEnd")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "deliveryAppointmentTz"))
    })
    private FixedZoneDateTimeWindow deliveryAppointment;

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<ShipmentUnit> shipmentUnits = new HashSet<>();

    private BigDecimal payWeight;

    @OneToMany(mappedBy = "shipment")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<ShipmentBarcode> barcodes = new HashSet<>();

    //Shipment result, not the whole transort order
    @Column(columnDefinition = "clob")
    private String executedRuleResult;

    @Enumerated(EnumType.STRING)
    private Status status;

    public ShipmentSize getSize() {
        ShipmentSize size = new ShipmentSize();
        if (getShipmentUnits() == null || getShipmentUnits().isEmpty()) {
            return size;
        }

        getShipmentUnits().forEach(shipmentUnit -> {
            if (shipmentUnit.getTotalGrossWeightInKilograms() != null) {
                size.addGrossWeight(shipmentUnit.getTotalGrossWeightInKilograms());
            }

            if (shipmentUnit.getTotalVolumeInCubicMeters() != null) {
                size.addVolume(shipmentUnit.getTotalVolumeInCubicMeters());
            }

            if (shipmentUnit.getTotalLdm() != null) {
                size.addLdm(shipmentUnit.getTotalLdm());
            }
            if (shipmentUnit.getShipmentUnitPackages() != null) {
                shipmentUnit.getShipmentUnitPackages().forEach(shipmentUnitPackage -> size.addCount(shipmentUnitPackage.getCount()));
            }
        });
        return size;
    }

    public Map<String, Integer> getPackageTypesAndCounts(){
        Map<String, Integer> typesAndCounts = new HashMap<>();
        if (getShipmentUnits() == null || getShipmentUnits().isEmpty()) {
            return typesAndCounts;
        }

        getShipmentUnits().forEach(shipmentUnit -> {
            if (shipmentUnit.getType() != null && StringUtils.isNotBlank(shipmentUnit.getType().getName())) {
                String key = shipmentUnit.getType().getName();
                if (shipmentUnit.getShipmentUnitPackages() != null && !shipmentUnit.getShipmentUnitPackages().isEmpty()) {
                    shipmentUnit.getShipmentUnitPackages().forEach(shipmentUnitPackage -> {
                        if (shipmentUnitPackage.getCount() != null && shipmentUnitPackage.getCount() > 0) {
                            int value = shipmentUnitPackage.getCount();
                            if (typesAndCounts.containsKey(key)) {
                                typesAndCounts.put(key, typesAndCounts.get(key) + value);
                            } else {
                                typesAndCounts.put(key, value);
                            }
                        }
                    });
                }
            }
        });

        return typesAndCounts;
    }

    public boolean isSenderCompanyEqualsLoadingLocationOwner(){
        return getSender().isCompanyEqualsLoadingLocationOwner();
    }

    public boolean isReceiverCompanyEqualsLoadingLocationOwner(){
        return getReceiver().isCompanyEqualsLoadingLocationOwner();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AdrClass getAdrClass() {
        return adrClass;
    }

    public void setAdrClass(AdrClass adrClass) {
        this.adrClass = adrClass;
    }

    public SenderOrReceiver getSender() {
        return sender;
    }

    public void setSender(SenderOrReceiver sender) {
        this.sender = sender;
    }

    public SenderOrReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(SenderOrReceiver receiver) {
        this.receiver = receiver;
    }

    public IdNamePair getCollectionWarehouse() {
        return collectionWarehouse;
    }

    public void setCollectionWarehouse(IdNamePair collectionWarehouse) {
        this.collectionWarehouse = collectionWarehouse;
    }

    public IdNamePair getDistributionWarehouse() {
        return distributionWarehouse;
    }

    public void setDistributionWarehouse(IdNamePair distributionWarehouse) {
        this.distributionWarehouse = distributionWarehouse;
    }

    public FixedZoneDateTime getReadyAtDate() {
        return readyAtDate;
    }

    public void setReadyAtDate(FixedZoneDateTime readyAtDate) {
        this.readyAtDate = readyAtDate;
    }

    public FixedZoneDateTime getRequestedDeliveryDate() {
        return requestedDeliveryDate;
    }

    public void setRequestedDeliveryDate(FixedZoneDateTime requestedDeliveryDate) {
        this.requestedDeliveryDate = requestedDeliveryDate;
    }

    public FixedZoneDateTime getCollectionArrivalDate() {
        return collectionArrivalDate;
    }

    public void setCollectionArrivalDate(FixedZoneDateTime collectionArrivalDate) {
        this.collectionArrivalDate = collectionArrivalDate;
    }

    public FixedZoneDateTime getLinehaulArrivalDate() {
        return linehaulArrivalDate;
    }

    public void setLinehaulArrivalDate(FixedZoneDateTime linehaulArrivalDate) {
        this.linehaulArrivalDate = linehaulArrivalDate;
    }

    public FixedZoneDateTimeWindow getPickupAppointment() {
        return pickupAppointment;
    }

    public void setPickupAppointment(FixedZoneDateTimeWindow pickupAppointment) {
        this.pickupAppointment = pickupAppointment;
    }

    public FixedZoneDateTimeWindow getDeliveryAppointment() {
        return deliveryAppointment;
    }

    public void setDeliveryAppointment(FixedZoneDateTimeWindow deliveryAppointment) {
        this.deliveryAppointment = deliveryAppointment;
    }

    public Set<ShipmentUnit> getShipmentUnits() {
        return shipmentUnits;
    }

    public void setShipmentUnits(Set<ShipmentUnit> shipmentUnits) {
        this.shipmentUnits = shipmentUnits;
    }

    public BigDecimal getPayWeight() {
        return payWeight;
    }

    public void setPayWeight(BigDecimal payWeight) {
        this.payWeight = payWeight;
    }

    public IdNamePair getCollectionWarehouseLocation() {
        return collectionWarehouseLocation;
    }

    public void setCollectionWarehouseLocation(IdNamePair collectionWarehouseLocation) {
        this.collectionWarehouseLocation = collectionWarehouseLocation;
    }

    public IdNamePair getDistributionWarehouseLocation() {
        return distributionWarehouseLocation;
    }

    public void setDistributionWarehouseLocation(IdNamePair distributionWarehouseLocation) {
        this.distributionWarehouseLocation = distributionWarehouseLocation;
    }

    public String getExecutedRuleResult() {
        return executedRuleResult;
    }

    public void setExecutedRuleResult(String executedRuleResult) {
        this.executedRuleResult = executedRuleResult;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
