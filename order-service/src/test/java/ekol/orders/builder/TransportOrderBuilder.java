package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.Status;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.*;

import java.util.HashSet;
import java.util.Set;

public final class TransportOrderBuilder {

    private Long id;
    private IdNamePair subsidiary;
    private TransportOrderRequest request;
    private Long customerId;
    private Company customer;
    private ServiceType serviceType;
    private TruckLoadType truckLoadType;
    private Incoterm incoterm;
    private boolean insuredByEkol;
    private Status status;
    private Set<TransportOrderDocument> documents = new HashSet<>();
    private Set<Shipment> shipments = new HashSet<>();
    private Set<RouteRequirement> routeRequirements = new HashSet<>();
    private Set<VehicleRequirement> vehicleRequirements = new HashSet<>();
    private Set<EquipmentRequirement> equipmentRequirements = new HashSet<>();
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private FixedZoneDateTime readyAtDate;
    private TransportOrderRuleSet ruleSet;

    private TransportOrderBuilder() {
    }

    public static TransportOrderBuilder aTransportOrder() {
        return new TransportOrderBuilder();
    }

    public TransportOrderBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TransportOrderBuilder withSubsidiary(IdNamePair subsidiary) {
        this.subsidiary = subsidiary;
        return this;
    }

    public TransportOrderBuilder withRequest(TransportOrderRequest request) {
        this.request = request;
        return this;
    }

    public TransportOrderBuilder withCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public TransportOrderBuilder withCustomer(Company customer) {
        this.customer = customer;
        return this;
    }

    public TransportOrderBuilder withServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public TransportOrderBuilder withTruckLoadType(TruckLoadType truckLoadType) {
        this.truckLoadType = truckLoadType;
        return this;
    }

    public TransportOrderBuilder withIncoterm(Incoterm incoterm) {
        this.incoterm = incoterm;
        return this;
    }

    public TransportOrderBuilder withInsuredByEkol(boolean insuredByEkol) {
        this.insuredByEkol = insuredByEkol;
        return this;
    }

    public TransportOrderBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public TransportOrderBuilder withDocuments(Set<TransportOrderDocument> documents) {
        this.documents = documents;
        return this;
    }

    public TransportOrderBuilder withShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
        return this;
    }

    public TransportOrderBuilder withRouteRequirements(Set<RouteRequirement> routeRequirements) {
        this.routeRequirements = routeRequirements;
        return this;
    }

    public TransportOrderBuilder withVehicleRequirements(Set<VehicleRequirement> vehicleRequirements) {
        this.vehicleRequirements = vehicleRequirements;
        return this;
    }

    public TransportOrderBuilder withEquipmentRequirements(Set<EquipmentRequirement> equipmentRequirements) {
        this.equipmentRequirements = equipmentRequirements;
        return this;
    }

    public TransportOrderBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public TransportOrderBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public TransportOrderBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public TransportOrderBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public TransportOrderBuilder withReadyAtDate(FixedZoneDateTime readyAtDate) {
        this.readyAtDate = readyAtDate;
        return this;
    }

    public TransportOrderBuilder withRuleSet(TransportOrderRuleSet ruleSet) {
        this.ruleSet = ruleSet;
        return this;
    }

    /*public TransportOrderBuilder but() {
        return aTransportOrder().withId(id).withRequest(request).withCustomerId(customerId).withCustomer(customer).withServiceType(serviceType).withTruckLoadType(truckLoadType).withIncoterm(incoterm).withInsuredByEkol(insuredByEkol).withStatus(status).withDocuments(documents).withShipments(shipments).withRouteRequirements(routeRequirements).withVehicleRequirements(vehicleRequirements).withEquipmentRequirements(equipmentRequirements).withDeleted(deleted).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy);
    }*/

    public TransportOrder build() {
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setId(id);
        transportOrder.setSubsidiary(subsidiary);
        transportOrder.setRequest(request);
        transportOrder.setCustomerId(customerId);
        transportOrder.setCustomer(customer);
        transportOrder.setServiceType(serviceType);
        transportOrder.setTruckLoadType(truckLoadType);
        transportOrder.setIncoterm(incoterm);
        transportOrder.setInsuredByEkol(insuredByEkol);
        transportOrder.setStatus(status);
        transportOrder.setDocuments(documents);
        transportOrder.setShipments(shipments);
        transportOrder.setRouteRequirements(routeRequirements);
        transportOrder.setVehicleRequirements(vehicleRequirements);
        transportOrder.setEquipmentRequirements(equipmentRequirements);
        transportOrder.setDeleted(deleted);
        transportOrder.setDeletedAt(deletedAt);
        transportOrder.setLastUpdated(lastUpdated);
        transportOrder.setLastUpdatedBy(lastUpdatedBy);
        transportOrder.setReadyAtDate(readyAtDate);
        transportOrder.setRuleSet(ruleSet);
        return transportOrder;
    }
}
