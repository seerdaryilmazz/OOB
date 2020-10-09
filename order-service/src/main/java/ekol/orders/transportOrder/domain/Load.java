package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "load")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Load extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_load", sequenceName = "seq_load")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_load")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_order_id")
    private TransportOrder transportOrder;

    @OneToMany(mappedBy="load")
    private Set<LoadDimension> loadDimensions = new HashSet<>();

    @Column
    private Long pickupPointId;

    @Transient
    private Location pickupPoint;

    @Column
    private Long deliveryPointId;

    @Transient
    private Location deliveryPoint;

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

    public Set<LoadDimension> getLoadDimensions() {
        return loadDimensions;
    }

    public void setLoadDimensions(Set<LoadDimension> loadDimensions) {
        this.loadDimensions = loadDimensions;
    }

    public Long getPickupPointId() {
        return pickupPointId;
    }

    public void setPickupPointId(Long pickupPointId) {
        this.pickupPointId = pickupPointId;
    }

    public Location getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(Location pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public Long getDeliveryPointId() {
        return deliveryPointId;
    }

    public void setDeliveryPointId(Long deliveryPointId) {
        this.deliveryPointId = deliveryPointId;
    }

    public Location getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(Location deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }
}
