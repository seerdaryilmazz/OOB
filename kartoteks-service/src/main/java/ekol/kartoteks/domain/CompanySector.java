package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

/**
 * Created by kilimci on 01/04/16.
 */
@Entity
@Table(name = "CompanySector")
@SequenceGenerator(name = "SEQ_COMPANYSECTOR", sequenceName = "SEQ_COMPANYSECTOR")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class CompanySector extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_COMPANYSECTOR")
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Sector sector;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isDefault;

    @Override
    public boolean equals(Object other){
        if(other == null){
            return true;
        }
        if(this == other){
            return true;
        }
        if (getClass() != other.getClass()){
            return false;
        }
        CompanySector otherSector = (CompanySector) other;
        EqualsBuilder builder = new EqualsBuilder();
        if(id != null){
            builder.append(id, otherSector.getId());
        }else{
            if(sector != null){
                builder.append(getSector().getId(), otherSector.getSector() != null ? otherSector.getSector().getId() : null);
            }
        }
        return builder.isEquals();
    }
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(43, 47);
        if(id != null){
            builder.append(id);
        }else{
            if(sector != null){
                builder.append(getSector().getId());
            }
        }
        return builder.toHashCode();
    }

    
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
