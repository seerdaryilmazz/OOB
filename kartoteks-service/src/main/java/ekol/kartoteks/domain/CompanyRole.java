package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kilimci on 13/06/16.
 */

@NamedEntityGraph(name = "CompanyRole.withCompany", attributeNodes = {@NamedAttributeNode("company")})
@Entity
@Table(name = "CompanyRole")
@SequenceGenerator(name = "SEQ_COMPANYROLE", sequenceName = "SEQ_COMPANYROLE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class CompanyRole extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYROLE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    @Embedded
    private DateWindow dateRange;

    @ManyToOne( fetch=FetchType.EAGER)
    @JoinColumn(name = "segment_type_id")
    @NotAudited
    private BusinessSegmentType segmentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_type_id")
    @NotAudited
    private CompanyRoleType roleType;

    @OneToMany(mappedBy="companyRole", fetch = FetchType.EAGER)
    @JsonManagedReference
    @Where(clause = "deleted = 0")
    private Set<CompanyEmployeeRelation> employeeRelations = new HashSet<>();

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
        CompanyRole role = (CompanyRole) other;
        EqualsBuilder builder = new EqualsBuilder();
        if(id != null){
            builder.append(id, role.getId());
        }else{
            if(roleType != null){
                builder.append(roleType.getId(), role.getRoleType() != null ? role.getRoleType().getId() : null);
            }
            if(segmentType != null){
                builder.append(segmentType.getId(), role.getSegmentType() != null ? role.getSegmentType().getId() : null);
            }
            if(dateRange != null && dateRange.getStartDate() != null){
                builder.append(dateRange.getStartDate(), role.getDateRange() != null ? role.getDateRange().getStartDate() : null);
            }
            if(dateRange != null && dateRange.getEndDate() != null){
                builder.append(dateRange.getEndDate(), role.getDateRange() != null ? role.getDateRange().getEndDate() : null);
            }
        }
        return builder.isEquals();
    }
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(13, 19);
        if(id != null){
            builder.append(id);
        }else{
            if(roleType != null){
                builder.append(roleType.getId());
            }
            if(segmentType != null){
                builder.append(segmentType.getId());
            }
            if(dateRange != null && dateRange.getStartDate() != null){
                builder.append(dateRange.getStartDate());
            }
            if(dateRange != null && dateRange.getEndDate() != null){
                builder.append(dateRange.getEndDate());
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public DateWindow getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateWindow dateRange) {
        this.dateRange = dateRange;
    }

    public BusinessSegmentType getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(BusinessSegmentType segmentType) {
        this.segmentType = segmentType;
    }

    public CompanyRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(CompanyRoleType roleType) {
        this.roleType = roleType;
    }

    public Set<CompanyEmployeeRelation> getEmployeeRelations() {
        return employeeRelations;
    }

    public void setEmployeeRelations(Set<CompanyEmployeeRelation> employeeRelations) {
        this.employeeRelations = employeeRelations;
    }
}
