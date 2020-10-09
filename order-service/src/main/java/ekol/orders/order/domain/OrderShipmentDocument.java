package ekol.orders.order.domain;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "OrderShipmentDocument.withShipment",
                attributeNodes = {
                        @NamedAttributeNode(value = "shipment")
                }
        )
})
@Entity
@Where(clause = "deleted = 0")
public class OrderShipmentDocument extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_order_shipment_document", sequenceName = "seq_order_shipment_document")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_document")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;

    @Embedded
    private Document document;

    public boolean isHealthCertificateDocument(){
        return getDocument().getType().getDocumentGroup().equals(DocumentTypeGroup.HEALTH_CERTIFICATE);
    }

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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
