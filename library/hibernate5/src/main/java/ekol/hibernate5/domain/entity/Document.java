package ekol.hibernate5.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document extends BaseEntity {

    /**
     * The name of the document before upload
     */
    private String originalName;

    /**
     * The name of the document that is displayed to the user
     */
    private String displayName;

    /**
     * The file name of the document in the server directory after upload (we generate this name using originalName)
     */
    private String fileName;

    /**
     * The path of the server directory that the document is uploaded to
     */
    @JsonIgnore
    private String directoryPath;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
