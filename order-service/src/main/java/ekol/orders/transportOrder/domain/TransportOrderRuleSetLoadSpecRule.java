package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "TorsLoadSpecRule") // Tors: Transport Order Rule Set
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSetLoadSpecRule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_tors_load_spec_rule", sequenceName = "seq_tors_load_spec_rule")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tors_load_spec_rule")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private TransportOrderRuleSet parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentUnitId")
    private ShipmentUnit shipmentUnit;

    private boolean isLongGoods;

    private boolean isOutOfGaugeGoods;

    private boolean isHeavyGoods;

    private boolean isValuableGoods;

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

    public boolean isLongGoods() {
        return isLongGoods;
    }

    public void setLongGoods(boolean longGoods) {
        isLongGoods = longGoods;
    }

    public boolean isOutOfGaugeGoods() {
        return isOutOfGaugeGoods;
    }

    public void setOutOfGaugeGoods(boolean outOfGaugeGoods) {
        isOutOfGaugeGoods = outOfGaugeGoods;
    }

    public boolean isHeavyGoods() {
        return isHeavyGoods;
    }

    public void setHeavyGoods(boolean heavyGoods) {
        isHeavyGoods = heavyGoods;
    }

    public boolean isValuableGoods() {
        return isValuableGoods;
    }

    public void setValuableGoods(boolean valuableGoods) {
        isValuableGoods = valuableGoods;
    }
}
