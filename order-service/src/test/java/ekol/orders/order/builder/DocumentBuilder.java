package ekol.orders.order.builder;

import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.order.domain.Document;

public final class DocumentBuilder {
    private DocumentType type;
    private String path;
    private String savedFileName;
    private String originalFileName;
    private String description;

    private DocumentBuilder() {
    }

    public static DocumentBuilder aDocument() {
        return new DocumentBuilder();
    }

    public DocumentBuilder withType(DocumentType type) {
        this.type = type;
        return this;
    }

    public DocumentBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public DocumentBuilder withSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
        return this;
    }

    public DocumentBuilder withOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public DocumentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DocumentBuilder but() {
        return aDocument().withType(type).withPath(path)
                .withSavedFileName(savedFileName).withOriginalFileName(originalFileName).withDescription(description);
    }

    public Document build() {
        Document document = new Document();
        document.setType(type);
        document.setPath(path);
        document.setSavedFileName(savedFileName);
        document.setOriginalFileName(originalFileName);
        document.setDescription(description);
        return document;
    }
}
