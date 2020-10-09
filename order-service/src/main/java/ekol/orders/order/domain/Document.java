package ekol.orders.order.domain;

import ekol.orders.lookup.domain.DocumentType;

import javax.persistence.*;

@Embeddable
public class Document {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId")
    private DocumentType type;

    @Column
    private String path;

    @Column
    private String savedFileName;

    @Column
    private String originalFileName;

    @Column
    private String description;

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSavedFileName() {
        return savedFileName;
    }

    public void setSavedFileName(String savedFileName) {
        this.savedFileName = savedFileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
