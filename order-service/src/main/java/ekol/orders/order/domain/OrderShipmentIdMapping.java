package ekol.orders.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Where;

import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
@Where(clause = "deleted = 0")
public class OrderShipmentIdMapping extends BaseEntity  {

    @Id
    @SequenceGenerator(name = "seq_order_shipment_id_mapping", sequenceName = "seq_order_shipment_id_mapping")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_order_shipment_id_mapping")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_shipment_id")
    private OrderShipment shipment;

    @Column(nullable = false, length = 20)
    private String application;

    @Column(nullable = false, length = 20)
    private String applicationOrderShipmentId;

    public static OrderShipmentIdMapping withApplication(String application, String id){
    	OrderShipmentIdMapping mapping = new OrderShipmentIdMapping();
        mapping.setApplication(application);
        mapping.setApplicationOrderShipmentId(id);
        return mapping;
    }
}
