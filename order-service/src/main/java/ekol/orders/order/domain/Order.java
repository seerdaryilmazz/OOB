package ekol.orders.order.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import lombok.Data;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Order.withShipments",
                attributeNodes = {@NamedAttributeNode(value = "shipments")}
        ),
        @NamedEntityGraph(
                name = "Order.withDocuments",
                attributeNodes = {@NamedAttributeNode(value = "documents")}
        )
})
@Data
@Entity
@Table(name = "orders") // We don't use 'ORDER' as the table name since it is a reserved word.
@Where(clause = "deleted = 0")
public class Order extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_orders", sequenceName = "seq_orders")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orders")
    private Long id;

    @Column(length = 10)
    private String code;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "subsidiaryId")),
            @AttributeOverride(name = "name", column = @Column(name = "subsidiaryName"))
    })
    private IdNameEmbeddable subsidiary;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "customerId")),
            @AttributeOverride(name = "name", column = @Column(name = "customerName"))
    })
    private IdNameEmbeddable customer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "originalCustomerId")),
            @AttributeOverride(name = "name", column = @Column(name = "originalCustomerName"))
    })
    private IdNameEmbeddable originalCustomer;

    @Column
    private String templateId;

    @Column
    private String templateName;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean partnerCustomer;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private TruckLoadType truckLoadType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "order")
    @Where(clause = "deleted = 0")
    private List<OrderShipment> shipments = new ArrayList<>();

    @OneToMany(mappedBy = "order")
    @Where(clause = "deleted = 0")
    private List<OrderDocument> documents = new ArrayList<>();

    @Column(length = 2)
    private String countryCode;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createdAt"))
    })
    private UtcDateTime createdAt;

    @Column(length = 100)
    @CreatedBy
    private String createdBy;
    
    @Transient
    private boolean initial;

    @Override
    @PrePersist
    public void prePersist() {
    	ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    	if (null == createdAt && null == id) {
    		createdAt = new UtcDateTime(now.toLocalDateTime());
        }
        super.prePersist();
    }
}
