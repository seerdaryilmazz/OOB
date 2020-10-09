package ekol.orders.order.domain;

import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "deleted = 0")
public class OrderDocument extends BaseEntity{

    @Id
    @SequenceGenerator(name = "seq_order_document", sequenceName = "seq_order_document")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_document")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;

    @Embedded
    private Document document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
