package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDocument;

public final class OrderShipmentDocumentBuilder {
    private Long id;
    private boolean deleted;
    private OrderShipment shipment;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private Document document;
    private UtcDateTime deletedAt;

    private OrderShipmentDocumentBuilder() {
    }

    public static OrderShipmentDocumentBuilder anOrderShipmentDocument() {
        return new OrderShipmentDocumentBuilder();
    }

    public OrderShipmentDocumentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderShipmentDocumentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderShipmentDocumentBuilder withShipment(OrderShipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public OrderShipmentDocumentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderShipmentDocumentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderShipmentDocumentBuilder withDocument(Document document) {
        this.document = document;
        return this;
    }

    public OrderShipmentDocumentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderShipmentDocumentBuilder but() {
        return anOrderShipmentDocument().withId(id).withDeleted(deleted).withShipment(shipment).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withDocument(document).withDeletedAt(deletedAt);
    }

    public OrderShipmentDocument build() {
        OrderShipmentDocument orderShipmentDocument = new OrderShipmentDocument();
        orderShipmentDocument.setId(id);
        orderShipmentDocument.setDeleted(deleted);
        orderShipmentDocument.setShipment(shipment);
        orderShipmentDocument.setLastUpdated(lastUpdated);
        orderShipmentDocument.setLastUpdatedBy(lastUpdatedBy);
        orderShipmentDocument.setDocument(document);
        orderShipmentDocument.setDeletedAt(deletedAt);
        return orderShipmentDocument;
    }
}
