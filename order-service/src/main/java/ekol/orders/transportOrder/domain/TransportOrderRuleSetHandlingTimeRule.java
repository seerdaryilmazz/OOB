package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.Duration;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "TorsHndlngTimeRule") // Tors: Transport Order Rule Set
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderRuleSetHandlingTimeRule extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_tors_hndlng_time_rule", sequenceName = "seq_tors_hndlng_time_rule")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tors_hndlng_time_rule")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonBackReference
    private TransportOrderRuleSet parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentUnitId")
    private ShipmentUnit shipmentUnit;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "totalSeconds", column = @Column(name = "duration"))
    })
    private Duration duration;

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
