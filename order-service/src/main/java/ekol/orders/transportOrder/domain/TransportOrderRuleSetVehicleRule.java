package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.transportOrder.domain.TransportOrderRuleSet;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TorsVehRule") // Tors: Transport Order Rule Set
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSetVehicleRule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_tors_veh_rule", sequenceName = "seq_tors_veh_rule")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tors_veh_rule")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private TransportOrderRuleSet parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentUnitId")
    private ShipmentUnit shipmentUnit;

    @ElementCollection
    @CollectionTable(name = "TorsVehRuleReqrd", joinColumns = @JoinColumn(name = "parentId"))
    @Column(name = "vehicle")
    private Set<String> requiredVehicles = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "TorsVehRuleNotAllwd", joinColumns = @JoinColumn(name = "parentId"))
    @Column(name = "vehicle")
    private Set<String> notAllowedVehicles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportOrderRuleSet getParent() {
        return parent;
    }

    public void setParent(TransportOrderRuleSet parent) {
        this.parent = parent;
    }

    public ShipmentUnit getShipmentUnit() {
        return shipmentUnit;
    }

    public void setShipmentUnit(ShipmentUnit shipmentUnit) {
        this.shipmentUnit = shipmentUnit;
    }

    public Set<String> getRequiredVehicles() {
        return requiredVehicles;
    }

    public void setRequiredVehicles(Set<String> requiredVehicles) {
        this.requiredVehicles = requiredVehicles;
    }

    public Set<String> getNotAllowedVehicles() {
        return notAllowedVehicles;
    }

    public void setNotAllowedVehicles(Set<String> notAllowedVehicles) {
        this.notAllowedVehicles = notAllowedVehicles;
    }
}
