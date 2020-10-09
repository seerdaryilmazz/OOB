package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.kartoteks.serializers.IdNameSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

/**
 * Created by kilimci on 24/06/16.
 */
@Entity
@Table(name = "CompanyRelation")
@SequenceGenerator(name = "SEQ_COMPANYRELATION", sequenceName = "SEQ_COMPANYRELATION")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class CompanyRelation extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYRELATION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = IdNameSerializer.class)
    @JoinColumn(name = "active_company_id")
    private Company activeCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = IdNameSerializer.class)
    @JoinColumn(name = "passive_company_id")
    private Company passiveCompany;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_type_id")
    @NotAudited
    private CompanyRelationType relationType;

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
        CompanyRelation relation = (CompanyRelation) other;
        EqualsBuilder builder = new EqualsBuilder();
        if(id != null){
            builder.append(id, relation.getId());
        }else{
            if(activeCompany != null){
                builder.append(activeCompany.getId(), relation.getActiveCompany() != null ? relation.getActiveCompany().getId(): null);
            }
            if(passiveCompany != null){
                builder.append(passiveCompany.getId(), relation.getPassiveCompany() != null ? relation.getPassiveCompany().getId(): null);
            }
            if(relationType != null){
                builder.append(relationType.getId(), relation.getRelationType() != null ? relation.getRelationType().getId(): null);
            }
        }
        return builder.isEquals();

    }
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31);
        if(id != null){
            builder.append(id);
        }else{
            if(activeCompany != null){
                builder.append(activeCompany.getId());
            }
            if(passiveCompany != null){
                builder.append(passiveCompany.getId());
            }
            if(relationType != null){
                builder.append(relationType.getId());
            }
        }
        return builder.toHashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getActiveCompany() {
        return activeCompany;
    }

    public void setActiveCompany(Company activeCompany) {
        this.activeCompany = activeCompany;
    }

    public Company getPassiveCompany() {
        return passiveCompany;
    }

    public void setPassiveCompany(Company passiveCompany) {
        this.passiveCompany = passiveCompany;
    }

    public CompanyRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(CompanyRelationType relationType) {
        this.relationType = relationType;
    }
}
