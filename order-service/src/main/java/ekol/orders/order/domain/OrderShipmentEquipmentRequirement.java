package ekol.orders.order.domain;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.transportOrder.domain.EquipmentType;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "deleted = 0")
@Table(name = "ORDER_SHIPMENT_EQUIPMENT_REQ")
public class OrderShipmentEquipmentRequirement extends BaseEntity{
    @Id
    @SequenceGenerator(name = "seq_order_shipment_equip_req", sequenceName = "seq_order_shipment_equip_req")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_equip_req")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipmentId")
    private EquipmentType equipment;

    @Column
    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderShipment getShipment() {
        return shipment;
    }

    public void setShipment(OrderShipment shipment) {
        this.shipment = shipment;
    }

    public EquipmentType getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentType equipment) {
        this.equipment = equipment;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
