package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "TorsRfUsageRule") // Tors: Transport Order Rule Set
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSetRfUsageRule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_tors_rf_usage_rule", sequenceName = "seq_tors_rf_usage_rule")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tors_rf_usage_rule")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private TransportOrderRuleSet parent;

    private Long warehouseId;

    private boolean requiredWhenLoading;

    private boolean requiredWhenUnloading;

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

    public boolean isRequiredWhenLoading() {
        return requiredWhenLoading;
    }

    public void setRequiredWhenLoading(boolean requiredWhenLoading) {
        this.requiredWhenLoading = requiredWhenLoading;
    }

    public boolean isRequiredWhenUnloading() {
        return requiredWhenUnloading;
    }

    public void setRequiredWhenUnloading(boolean requiredWhenUnloading) {
        this.requiredWhenUnloading = requiredWhenUnloading;
    }
}
