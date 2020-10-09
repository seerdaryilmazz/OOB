package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.json.DocumentJson;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;

public final class DocumentJsonBuilder {
    private Long id;
    private IdCodeNameTrio type;
    private String path;
    private String savedFileName;
    private String originalFileName;
    private String description;

    private DocumentJsonBuilder() {
    }

    public static DocumentJsonBuilder aDocumentJson() {
        return new DocumentJsonBuilder();
    }

    public DocumentJsonBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DocumentJsonBuilder withType(IdCodeNameTrio type) {
        this.type = type;
        return this;
    }

    public DocumentJsonBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public DocumentJsonBuilder withSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
        return this;
    }

    public DocumentJsonBuilder withOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }
    public DocumentJsonBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DocumentJsonBuilder but() {
        return aDocumentJson().withId(id).withType(type).withPath(path).withSavedFileName(savedFileName).withOriginalFileName(originalFileName).withDescription(description);
    }

    public DocumentJson build() {
        DocumentJson documentJson = new DocumentJson();
        documentJson.setId(id);
        documentJson.setType(type);
        documentJson.setPath(path);
        documentJson.setSavedFileName(savedFileName);
        documentJson.setOriginalFileName(originalFileName);
        documentJson.setDescription(description);
        return documentJson;
    }
}
