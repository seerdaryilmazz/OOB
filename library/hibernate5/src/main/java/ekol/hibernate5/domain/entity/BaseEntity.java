package ekol.hibernate5.domain.entity;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import ekol.hibernate5.domain.embeddable.UtcDateTime;

/**
 * Created by kilimci on 16/03/16.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

	@Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean deleted;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "lastUpdated"))
    })
    private UtcDateTime lastUpdated;

    @Column(length = 100)
    @LastModifiedBy
    private String lastUpdatedBy;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "deletedAt"))
    })
    private UtcDateTime deletedAt;

    private final void updateLastUpdatedAndDeletedAt() {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        if (lastUpdated == null) {
            lastUpdated = new UtcDateTime(now.toLocalDateTime());
        } else {
            lastUpdated.setDateTime(now.toLocalDateTime());
        }

        if (deletedAt == null && deleted) {
            deletedAt = new UtcDateTime(now.toLocalDateTime());
        }
    }

    @PrePersist
    public void prePersist() {
        updateLastUpdatedAndDeletedAt();
    }

    @PreUpdate
    public void preUpdate() {
        updateLastUpdatedAndDeletedAt();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public UtcDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public UtcDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
