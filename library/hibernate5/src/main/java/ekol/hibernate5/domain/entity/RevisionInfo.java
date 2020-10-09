package ekol.hibernate5.domain.entity;

import ekol.hibernate5.domain.listener.RevisionInfoListener;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by kilimci on 29/04/16.
 */
@Entity
@RevisionEntity(RevisionInfoListener.class)
@Table(name = "RevisionInfo")
@SequenceGenerator(name = "SEQ_REVISIONINFO", sequenceName = "SEQ_REVISIONINFO")
public class RevisionInfo implements Serializable {

    @RevisionNumber
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_REVISIONINFO")
    private Long id;

    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;

    @Column(nullable = false, length=100)
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(!(o instanceof RevisionInfo)) {
            return false;
        } else {
            RevisionInfo that = (RevisionInfo)o;
            return this.id == that.id && this.updatedTime == that.updatedTime && this.updatedBy == that.updatedBy;
        }
    }

    @Override
    public int hashCode() {
        int result = this.id.intValue();
        result = 31 * result + (int)(this.updatedTime.getTime() ^ this.updatedTime.getTime() >>> 32);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
