package ekol.file.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "FileEntry")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileEntry {

    /**
     * UUID
     */
    @Id
    private String id;

    /**
     * The name of the file at the time of upload
     */
    private String name;

    /**
     * The name of the file in the server directory (we generate this name)
     */
    @JsonIgnore
    private String physicalName;

    /**
     * UTC date time
     */
    @JsonIgnore
    private LocalDateTime uploadDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public LocalDateTime getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(LocalDateTime uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }
}
