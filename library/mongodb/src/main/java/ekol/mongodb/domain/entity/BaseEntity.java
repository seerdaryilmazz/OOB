package ekol.mongodb.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by burak on 30/05/17.
 */
public class BaseEntity implements Persistable<String> {

    @Id
    private String id;

    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    @LastModifiedBy
    private String lastUpdatedBy;

    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

	@Override
	@JsonIgnore
	public boolean isNew() {
		return Objects.isNull(getId());
	}
}
