package ekol.agreement.domain.model;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class AuditedBaseEntity implements Serializable {
    private boolean deleted;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "dateTime", column = @Column(name = "lastUpdated"))})
    private UtcDateTime lastUpdated;

    @Column(length = 100)
    @LastModifiedBy
    private String lastUpdatedBy;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "dateTime", column = @Column(name = "deletedAt"))})
    private UtcDateTime deletedAt;

    private final void updateLastUpdatedAndDeletedAt() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        if (this.lastUpdated == null) {
            this.lastUpdated = new UtcDateTime(now.toLocalDateTime());
        } else {
            this.lastUpdated.setDateTime(now.toLocalDateTime());
        }

        if (this.deletedAt == null && this.deleted) {
            this.deletedAt = new UtcDateTime(now.toLocalDateTime());
        }

    }

    @PrePersist
    public void prePersist() {
        this.updateLastUpdatedAndDeletedAt();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateLastUpdatedAndDeletedAt();
    }

}
