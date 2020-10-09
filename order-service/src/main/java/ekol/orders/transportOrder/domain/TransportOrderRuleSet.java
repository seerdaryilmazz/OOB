package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TransportOrderRuleSet")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSet extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_transport_order_rule_set", sequenceName = "seq_transport_order_rule_set")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transport_order_rule_set")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportOrderId")
    @JsonBackReference
    private TransportOrder transportOrder;

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderRuleSetApprovalRule> approvalRules = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderRuleSetVehicleRule> vehicleRules = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderRuleSetHandlingTimeRule> handlingTimeRules = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderRuleSetRfUsageRule> rfUsageRules = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderRuleSetWarehouseHandlingRule> warehouseHandlingRules = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<TransportOrderRuleSetLoadSpecRule> loadSpecRules = new HashSet<>();

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

    public Set<TransportOrderRuleSetApprovalRule> getApprovalRules() {
        return approvalRules;
    }

    public void setApprovalRules(Set<TransportOrderRuleSetApprovalRule> approvalRules) {
        this.approvalRules = approvalRules;
    }

    public Set<TransportOrderRuleSetVehicleRule> getVehicleRules() {
        return vehicleRules;
    }

    public void setVehicleRules(Set<TransportOrderRuleSetVehicleRule> vehicleRules) {
        this.vehicleRules = vehicleRules;
    }

    public Set<TransportOrderRuleSetHandlingTimeRule> getHandlingTimeRules() {
        return handlingTimeRules;
    }

    public void setHandlingTimeRules(Set<TransportOrderRuleSetHandlingTimeRule> handlingTimeRules) {
        this.handlingTimeRules = handlingTimeRules;
    }

    public Set<TransportOrderRuleSetRfUsageRule> getRfUsageRules() {
        return rfUsageRules;
    }

    public void setRfUsageRules(Set<TransportOrderRuleSetRfUsageRule> rfUsageRules) {
        this.rfUsageRules = rfUsageRules;
    }

    public Set<TransportOrderRuleSetWarehouseHandlingRule> getWarehouseHandlingRules() {
        return warehouseHandlingRules;
    }

    public void setWarehouseHandlingRules(Set<TransportOrderRuleSetWarehouseHandlingRule> warehouseHandlingRules) {
        this.warehouseHandlingRules = warehouseHandlingRules;
    }

    public Set<TransportOrderRuleSetLoadSpecRule> getLoadSpecRules() {
        return loadSpecRules;
    }

    public void setLoadSpecRules(Set<TransportOrderRuleSetLoadSpecRule> loadSpecRules) {
        this.loadSpecRules = loadSpecRules;
    }
}
