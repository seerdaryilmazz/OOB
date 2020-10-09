package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.Status;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "TransportOrder.allDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "request"),
                        @NamedAttributeNode(value = "incoterm"),
                        @NamedAttributeNode(value = "documents", subgraph = "TransportOrderDocument.allDetails"),
                        @NamedAttributeNode(value = "shipments", subgraph = "Shipment.allDetails"),
                        @NamedAttributeNode(value = "routeRequirements", subgraph = "RouteRequirement.allDetails"),
                        @NamedAttributeNode(value = "vehicleRequirements", subgraph = "VehicleRequirement.allDetails"),
                        @NamedAttributeNode(value = "equipmentRequirements", subgraph = "EquipmentRequirement.allDetails")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "TransportOrderDocument.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "type")
                                }
                        ),
                        @NamedSubgraph(
                                name = "Shipment.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "adrClass"),
                                        @NamedAttributeNode(value = "shipmentUnits", subgraph = "ShipmentUnit.allDetails")
                                }
                        ),
                        @NamedSubgraph(
                                name = "ShipmentUnit.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "type", subgraph = "PackageType.allDetails"),
                                        @NamedAttributeNode(value = "shipmentUnitPackages")
                                }
                        ),
                        @NamedSubgraph(
                                name = "PackageType.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "packageGroup")
                                }
                        ),
                        @NamedSubgraph(
                                name = "RouteRequirement.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "transportType"),
                                        @NamedAttributeNode(value = "routes")
                                }
                        ),
                        @NamedSubgraph(
                                name = "EquipmentRequirement.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "equipmentType")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "TransportOrder.ruleSet",
                attributeNodes = {
                        @NamedAttributeNode(value = "ruleSet", subgraph = "TransportOrderRuleSet.allDetails")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "TransportOrderRuleSet.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "approvalRules"),
                                        @NamedAttributeNode(value = "vehicleRules", subgraph = "TransportOrderRuleSetVehicleRule.allDetails"),
                                        @NamedAttributeNode(value = "handlingTimeRules", subgraph = "TransportOrderRuleSetHandlingTimeRule.allDetails"),
                                        @NamedAttributeNode(value = "rfUsageRules"),
                                        @NamedAttributeNode(value = "warehouseHandlingRules", subgraph = "TransportOrderRuleSetWarehouseHandlingRule.allDetails"),
                                        @NamedAttributeNode(value = "loadSpecRules", subgraph = "TransportOrderRuleSetLoadSpecRule.allDetails")
                                }
                        ),
                        @NamedSubgraph(
                                name = "TransportOrderRuleSetVehicleRule.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "shipmentUnit"),
                                        @NamedAttributeNode(value = "requiredVehicles"),
                                        @NamedAttributeNode(value = "notAllowedVehicles")
                                }
                        ),
                        @NamedSubgraph(
                                name = "TransportOrderRuleSetHandlingTimeRule.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "shipmentUnit")
                                }
                        ),
                        @NamedSubgraph(
                                name = "TransportOrderRuleSetWarehouseHandlingRule.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "requiredStaffIfHandlingAllowed"),
                                        @NamedAttributeNode(value = "requiredEquipmentIfHandlingAllowed")
                                }
                        ),
                        @NamedSubgraph(
                                name = "TransportOrderRuleSetLoadSpecRule.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "shipmentUnit")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "TransportOrder.forShipmentAssignments",
                attributeNodes = {
                        @NamedAttributeNode(value = "incoterm"),
                        @NamedAttributeNode(value = "shipments", subgraph = "Shipment.allDetails")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "ShipmentUnit.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "type", subgraph = "PackageType.allDetails"),
                                        @NamedAttributeNode(value = "shipmentUnitPackages")
                                }
                        ),
                        @NamedSubgraph(
                                name = "PackageType.allDetails",
                                attributeNodes = {
                                        @NamedAttributeNode(value = "packageGroup")
                                }
                        )
                }
        ),
})
@Entity
@Table(name = "transport_order")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrder extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_transport_order", sequenceName = "seq_transport_order")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transport_order")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "subsidiaryId")),
            @AttributeOverride(name = "name", column = @Column(name = "subsidiaryName"))
    })
    private IdNamePair subsidiary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestId")
    @JsonBackReference
    private TransportOrderRequest request;

    private Long customerId;

    @Transient
    private Company customer;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private TruckLoadType truckLoadType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incotermId")
    private Incoterm incoterm;

    private boolean insuredByEkol;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "readyAtDate")),
                    @AttributeOverride(name = "timeZone", column = @Column(name = "readyAtDateTimezone"))
    })
    private FixedZoneDateTime readyAtDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruleSetId")
    @JsonManagedReference
    private TransportOrderRuleSet ruleSet;

    @OneToMany(mappedBy = "transportOrder")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderDocument> documents = new HashSet<>();

    @OneToMany(mappedBy = "transportOrder")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<Shipment> shipments = new HashSet<>();

    @OneToMany(mappedBy = "transportOrder")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<RouteRequirement> routeRequirements = new HashSet<>();

    @OneToMany(mappedBy = "transportOrder")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<VehicleRequirement> vehicleRequirements = new HashSet<>();

    @OneToMany(mappedBy = "transportOrder")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<EquipmentRequirement> equipmentRequirements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IdNamePair getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(IdNamePair subsidiary) {
        this.subsidiary = subsidiary;
    }

    public TransportOrderRequest getRequest() {
        return request;
    }

    public void setRequest(TransportOrderRequest request) {
        this.request = request;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Company getCustomer() {
        return customer;
    }

    public void setCustomer(Company customer) {
        this.customer = customer;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public TruckLoadType getTruckLoadType() {
        return truckLoadType;
    }

    public void setTruckLoadType(TruckLoadType truckLoadType) {
        this.truckLoadType = truckLoadType;
    }

    public Incoterm getIncoterm() {
        return incoterm;
    }

    public void setIncoterm(Incoterm incoterm) {
        this.incoterm = incoterm;
    }

    public boolean isInsuredByEkol() {
        return insuredByEkol;
    }

    public void setInsuredByEkol(boolean insuredByEkol) {
        this.insuredByEkol = insuredByEkol;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<TransportOrderDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<TransportOrderDocument> documents) {
        this.documents = documents;
    }

    public Set<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(Set<Shipment> shipments) {
        this.shipments = shipments;
    }

    public Set<RouteRequirement> getRouteRequirements() {
        return routeRequirements;
    }

    public void setRouteRequirements(Set<RouteRequirement> routeRequirements) {
        this.routeRequirements = routeRequirements;
    }

    public Set<VehicleRequirement> getVehicleRequirements() {
        return vehicleRequirements;
    }

    public void setVehicleRequirements(Set<VehicleRequirement> vehicleRequirements) {
        this.vehicleRequirements = vehicleRequirements;
    }

    public Set<EquipmentRequirement> getEquipmentRequirements() {
        return equipmentRequirements;
    }

    public void setEquipmentRequirements(Set<EquipmentRequirement> equipmentRequirements) {
        this.equipmentRequirements = equipmentRequirements;
    }

    public FixedZoneDateTime getReadyAtDate() {
        return readyAtDate;
    }

    public void setReadyAtDate(FixedZoneDateTime readyAtDate) {
        this.readyAtDate = readyAtDate;
    }

    public TransportOrderRuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(TransportOrderRuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }
}
