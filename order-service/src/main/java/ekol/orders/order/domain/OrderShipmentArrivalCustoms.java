package ekol.orders.order.domain;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Data;

@Entity
@Data
@Where(clause = "deleted = 0")
@Table(name = "order_shipment_arr_customs")
public class OrderShipmentArrivalCustoms extends BaseEntity {
    @Id
    @SequenceGenerator(name = "seq_order_shipment_arr_customs", sequenceName = "seq_order_shipment_arr_customs")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_arr_customs")
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "customsLocationId")),
            @AttributeOverride(name = "name", column = @Column(name = "customsLocationName"))
    })
    private IdNameEmbeddable customsLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "code", column = @Column(name = "customsTypeCode")),
            @AttributeOverride(name = "name", column = @Column(name = "customsTypeName"))
    })
    private CodeNameEmbeddable customsType;
    
    @Enumerated(EnumType.STRING)
    private CompanyType customsAgentType;

}
