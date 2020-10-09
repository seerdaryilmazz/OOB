package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TorsWhHndlngRule") // Tors: Transport Order Rule Set
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSetWarehouseHandlingRule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_tors_wh_hndlng_rule", sequenceName = "seq_tors_wh_hndlng_rule")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tors_wh_hndlng_rule")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private TransportOrderRuleSet parent;

    private Long warehouseId;

    private boolean handlingAllowed;

    @ElementCollection
    @CollectionTable(name = "TorsWhHndlngRuleStaff", joinColumns = @JoinColumn(name = "parentId"))
    @Column(name = "staff")
    private Set<String> requiredStaffIfHandlingAllowed = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "TorsWhHndlngRuleEquip", joinColumns = @JoinColumn(name = "parentId"))
    @Column(name = "equipment")
    private Set<String> requiredEquipmentIfHandlingAllowed = new HashSet<>();

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

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public boolean isHandlingAllowed() {
        return handlingAllowed;
    }

    public void setHandlingAllowed(boolean handlingAllowed) {
        this.handlingAllowed = handlingAllowed;
    }

    public Set<String> getRequiredStaffIfHandlingAllowed() {
        return requiredStaffIfHandlingAllowed;
    }

    public void setRequiredStaffIfHandlingAllowed(Set<String> requiredStaffIfHandlingAllowed) {
        this.requiredStaffIfHandlingAllowed = requiredStaffIfHandlingAllowed;
    }

    public Set<String> getRequiredEquipmentIfHandlingAllowed() {
        return requiredEquipmentIfHandlingAllowed;
    }

    public void setRequiredEquipmentIfHandlingAllowed(Set<String> requiredEquipmentIfHandlingAllowed) {
        this.requiredEquipmentIfHandlingAllowed = requiredEquipmentIfHandlingAllowed;
    }
}
