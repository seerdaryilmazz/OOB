package ekol.orders.order.domain;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Data;

@Entity
@Data
@Where(clause = "deleted = 0")
@Table(name = "order_shipment_dep_customs")
public class OrderShipmentDepartureCustoms extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_order_shipment_dep_customs", sequenceName = "seq_order_shipment_dep_customs")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_dep_customs")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "customsAgentId")),
            @AttributeOverride(name = "name", column = @Column(name = "customsAgentName"))
    })
    private IdNameEmbeddable customsAgent;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "customsAgentLocationId")),
            @AttributeOverride(name = "name", column = @Column(name = "customsAgentLocationName"))
    })
    private IdNameEmbeddable customsAgentLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "customsOfficeId")),
            @AttributeOverride(name = "name", column = @Column(name = "customsOfficeName"))
    })
    private IdNameEmbeddable customsOffice;
    
    @Enumerated(EnumType.STRING)
    private CompanyType customsAgentType;
}
