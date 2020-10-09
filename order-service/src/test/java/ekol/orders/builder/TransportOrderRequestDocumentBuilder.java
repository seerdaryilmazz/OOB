package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.domain.TransportOrderRequestDocument;


public final class TransportOrderRequestDocumentBuilder {

    private Long id;
    private TransportOrderRequest request;
    private DocumentType type;
    private String originalName;
    private String displayName;
    private String fileName;
    private String directoryPath;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private TransportOrderRequestDocumentBuilder() {
    }

    public static TransportOrderRequestDocumentBuilder aTransportOrderRequestDocument() {
        return new TransportOrderRequestDocumentBuilder();
    }

    public TransportOrderRequestDocumentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withRequest(TransportOrderRequest request) {
        this.request = request;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withType(DocumentType type) {
        this.type = type;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public TransportOrderRequestDocumentBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public TransportOrderRequestDocument build() {
        TransportOrderRequestDocument transportOrderRequestDocument = new TransportOrderRequestDocument();
        transportOrderRequestDocument.setId(id);
        transportOrderRequestDocument.setRequest(request);
        transportOrderRequestDocument.setType(type);
        transportOrderRequestDocument.setOriginalName(originalName);
        transportOrderRequestDocument.setDisplayName(displayName);
        transportOrderRequestDocument.setFileName(fileName);
        transportOrderRequestDocument.setDirectoryPath(directoryPath);
        transportOrderRequestDocument.setDeleted(deleted);
        transportOrderRequestDocument.setDeletedAt(deletedAt);
        transportOrderRequestDocument.setLastUpdated(lastUpdated);
        transportOrderRequestDocument.setLastUpdatedBy(lastUpdatedBy);
        return transportOrderRequestDocument;
    }
}
