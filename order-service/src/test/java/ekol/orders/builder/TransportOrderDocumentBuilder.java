package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.TransportOrderDocument;


public final class TransportOrderDocumentBuilder {

    private Long id;
    private TransportOrder transportOrder;
    private String originalName;
    private String displayName;
    private String fileName;
    private String directoryPath;
    private DocumentType type;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private TransportOrderDocumentBuilder() {
    }

    public static TransportOrderDocumentBuilder aTransportOrderDocument() {
        return new TransportOrderDocumentBuilder();
    }

    public TransportOrderDocumentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TransportOrderDocumentBuilder withTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
        return this;
    }

    public TransportOrderDocumentBuilder withOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public TransportOrderDocumentBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public TransportOrderDocumentBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TransportOrderDocumentBuilder withDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public TransportOrderDocumentBuilder withType(DocumentType type) {
        this.type = type;
        return this;
    }

    public TransportOrderDocumentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public TransportOrderDocumentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public TransportOrderDocumentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public TransportOrderDocumentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public TransportOrderDocument build() {
        TransportOrderDocument transportOrderDocument = new TransportOrderDocument();
        transportOrderDocument.setId(id);
        transportOrderDocument.setTransportOrder(transportOrder);
        transportOrderDocument.setOriginalName(originalName);
        transportOrderDocument.setDisplayName(displayName);
        transportOrderDocument.setFileName(fileName);
        transportOrderDocument.setDirectoryPath(directoryPath);
        transportOrderDocument.setType(type);
        transportOrderDocument.setDeleted(deleted);
        transportOrderDocument.setDeletedAt(deletedAt);
        transportOrderDocument.setLastUpdated(lastUpdated);
        transportOrderDocument.setLastUpdatedBy(lastUpdatedBy);
        return transportOrderDocument;
    }
}
