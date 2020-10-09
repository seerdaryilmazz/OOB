package ekol.orders.lookup.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.DocumentTypeGroup;

public final class DocumentTypeBuilder {

    private Long id;
    private String code;
    private String name;
    private DocumentTypeGroup documentGroup;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;

    private String lastUpdatedBy;

    private DocumentTypeBuilder() {
    }

    public static DocumentTypeBuilder aDocumentType() {
        return new DocumentTypeBuilder();
    }

    public DocumentTypeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DocumentTypeBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public DocumentTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DocumentTypeBuilder withDocumentGroup(DocumentTypeGroup documentGroup) {
        this.documentGroup = documentGroup;
        return this;
    }

    public DocumentTypeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public DocumentTypeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public DocumentTypeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public DocumentTypeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public DocumentType build() {
        DocumentType documentType = new DocumentType();
        documentType.setId(id);
        documentType.setCode(code);
        documentType.setName(name);
        documentType.setDocumentGroup(documentGroup);
        documentType.setDeleted(deleted);
        documentType.setDeletedAt(deletedAt);
        documentType.setLastUpdated(lastUpdated);
        documentType.setLastUpdatedBy(lastUpdatedBy);
        return documentType;
    }
}
