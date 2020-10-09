package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.Document;
import ekol.orders.lookup.domain.DocumentType;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "transport_order_doc")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportOrderDocument extends Document {

    @Id
    @SequenceGenerator(name = "seq_transport_order_doc", sequenceName = "seq_transport_order_doc")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transport_order_doc")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_order_id")
    @JsonBackReference
    private TransportOrder transportOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId")
    private DocumentType type;

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

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }
}
