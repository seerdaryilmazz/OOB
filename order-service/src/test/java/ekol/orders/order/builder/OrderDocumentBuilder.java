package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderDocument;

public final class OrderDocumentBuilder {
    private Long id;
    private boolean deleted;
    private Order order;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private Document document;
    private UtcDateTime deletedAt;

    private OrderDocumentBuilder() {
    }

    public static OrderDocumentBuilder anOrderDocument() {
        return new OrderDocumentBuilder();
    }

    public OrderDocumentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderDocumentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public OrderDocumentBuilder withOrder(Order order) {
        this.order = order;
        return this;
    }

    public OrderDocumentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public OrderDocumentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public OrderDocumentBuilder withDocument(Document document) {
        this.document = document;
        return this;
    }

    public OrderDocumentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public OrderDocumentBuilder but() {
        return anOrderDocument().withId(id).withDeleted(deleted).withOrder(order).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy).withDocument(document).withDeletedAt(deletedAt);
    }

    public OrderDocument build() {
        OrderDocument orderDocument = new OrderDocument();
        orderDocument.setId(id);
        orderDocument.setDeleted(deleted);
        orderDocument.setOrder(order);
        orderDocument.setLastUpdated(lastUpdated);
        orderDocument.setLastUpdatedBy(lastUpdatedBy);
        orderDocument.setDocument(document);
        orderDocument.setDeletedAt(deletedAt);
        return orderDocument;
    }
}
