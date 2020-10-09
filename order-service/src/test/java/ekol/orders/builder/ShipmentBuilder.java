package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.AdrClass;
import ekol.orders.order.domain.Status;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.SenderOrReceiver;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.domain.ShipmentUnit;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.elastic.shipment.model.ShipmentSize;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ShipmentBuilder {

    private Long id;
    private TransportOrder transportOrder;
    private String code;
    private AdrClass adrClass;
    private SenderOrReceiver sender;
    private SenderOrReceiver receiver;
    private Status status;
    private IdNamePair collectionWarehouse;
    private IdNamePair collectionWarehouseLocation;
    private IdNamePair distributionWarehouse;
    private IdNamePair distributionWarehouseLocation;
    private FixedZoneDateTime readyAtDate;
    private FixedZoneDateTime requestedDeliveryDate;
    private FixedZoneDateTime collectionArrivalDate;
    private FixedZoneDateTime linehaulArrivalDate;
    private FixedZoneDateTimeWindow pickupAppointment;
    private FixedZoneDateTimeWindow deliveryAppointment;
    private Set<ShipmentUnit> shipmentUnits = new HashSet<>();
    private BigDecimal payWeight;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private String executedRuleResult;

    private ShipmentBuilder() {
    }

    public static ShipmentBuilder aShipment() {
        return new ShipmentBuilder();
    }

    public ShipmentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShipmentBuilder withTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
        return this;
    }

    public ShipmentBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public ShipmentBuilder withAdrClass(AdrClass adrClass) {
        this.adrClass = adrClass;
        return this;
    }

    public ShipmentBuilder withSender(SenderOrReceiver sender) {
        this.sender = sender;
        return this;
    }

    public ShipmentBuilder withReceiver(SenderOrReceiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public ShipmentBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public ShipmentBuilder withCollectionWarehouse(IdNamePair collectionWarehouse) {
        this.collectionWarehouse = collectionWarehouse;
        return this;
    }

    public ShipmentBuilder withCollectionWarehouseLocation(IdNamePair collectionWarehouseLocation) {
        this.collectionWarehouseLocation = collectionWarehouseLocation;
        return this;
    }

    public ShipmentBuilder withDistributionWarehouse(IdNamePair distributionWarehouse) {
        this.distributionWarehouse = distributionWarehouse;
        return this;
    }

    public ShipmentBuilder withDistributionWarehouseLocation(IdNamePair distributionWarehouseLocation) {
        this.distributionWarehouseLocation = distributionWarehouseLocation;
        return this;
    }

    public ShipmentBuilder withReadyAtDate(FixedZoneDateTime readyAtDate) {
        this.readyAtDate = readyAtDate;
        return this;
    }

    public ShipmentBuilder withRequestedDeliveryDate(FixedZoneDateTime requestedDeliveryDate) {
        this.requestedDeliveryDate = requestedDeliveryDate;
        return this;
    }

    public ShipmentBuilder withCollectionArrivalDate(FixedZoneDateTime collectionArrivalDate) {
        this.collectionArrivalDate = collectionArrivalDate;
        return this;
    }

    public ShipmentBuilder withLinehaulArrivalDate(FixedZoneDateTime linehaulArrivalDate) {
        this.linehaulArrivalDate = linehaulArrivalDate;
        return this;
    }

    public ShipmentBuilder withPickupAppointment(FixedZoneDateTimeWindow pickupAppointment) {
        this.pickupAppointment = pickupAppointment;
        return this;
    }

    public ShipmentBuilder withDeliveryAppointment(FixedZoneDateTimeWindow deliveryAppointment) {
        this.deliveryAppointment = deliveryAppointment;
        return this;
    }

    public ShipmentBuilder withShipmentUnits(Set<ShipmentUnit> shipmentUnits) {
        this.shipmentUnits = shipmentUnits;
        return this;
    }

    public ShipmentBuilder withPayWeight(BigDecimal payWeight) {
        this.payWeight = payWeight;
        return this;
    }

    public ShipmentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public ShipmentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ShipmentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ShipmentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ShipmentBuilder withExecutedRuleResult(String executedRuleResult) {
        this.executedRuleResult = executedRuleResult;
        return this;
    }

    public ShipmentBuilder withPackageTypesAndCounts(Map map){
        return this;
    }
    public ShipmentBuilder withReceiverCompanyEqualsLoadingLocationOwner(boolean b){
        return this;
    }
    public ShipmentBuilder withSenderCompanyEqualsLoadingLocationOwner(boolean b){
        return this;
    }
    public ShipmentBuilder withSize(ShipmentSize shipmentSize){
        return this;
    }



    public Shipment build() {
        Shipment shipment = new Shipment();
        shipment.setId(id);
        shipment.setTransportOrder(transportOrder);
        shipment.setCode(code);
        shipment.setAdrClass(adrClass);
        shipment.setSender(sender);
        shipment.setReceiver(receiver);
        shipment.setStatus(status);
        shipment.setCollectionWarehouse(collectionWarehouse);
        shipment.setCollectionWarehouseLocation(collectionWarehouseLocation);
        shipment.setDistributionWarehouse(distributionWarehouse);
        shipment.setDistributionWarehouseLocation(distributionWarehouseLocation);
        shipment.setReadyAtDate(readyAtDate);
        shipment.setRequestedDeliveryDate(requestedDeliveryDate);
        shipment.setCollectionArrivalDate(collectionArrivalDate);
        shipment.setLinehaulArrivalDate(linehaulArrivalDate);
        shipment.setPickupAppointment(pickupAppointment);
        shipment.setDeliveryAppointment(deliveryAppointment);
        shipment.setShipmentUnits(shipmentUnits);
        shipment.setDeleted(deleted);
        shipment.setDeletedAt(deletedAt);
        shipment.setLastUpdated(lastUpdated);
        shipment.setLastUpdatedBy(lastUpdatedBy);
        shipment.setExecutedRuleResult(executedRuleResult);
        return shipment;
    }
}
