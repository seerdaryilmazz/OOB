package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.DateWindow;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

/**
 * Created by kilimci on 13/06/16.
 */
@Entity
@NamedEntityGraph(name = "CompanyEmployeeRelation.company",
        attributeNodes = @NamedAttributeNode(value = "companyRole", subgraph = "companyRole"),
        subgraphs = @NamedSubgraph(name = "companyRole", attributeNodes = @NamedAttributeNode("company")))
@Table(name = "CompanyEmployeeRelation")
@SequenceGenerator(name = "SEQ_COMPANYEMPLOYEERELATION", sequenceName = "SEQ_COMPANYEMPLOYEERELATION")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class CompanyEmployeeRelation extends BaseEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYEMPLOYEERELATION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "company_role_id")
    private CompanyRole companyRole;

    @Column(nullable = false, length = 100)
    private String employeeAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_id")
    @NotAudited
    private EmployeeCustomerRelation relation;

    @Embedded
    private DateWindow validDates;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyRole getCompanyRole() {
        return companyRole;
    }

    public void setCompanyRole(CompanyRole companyRole) {
        this.companyRole = companyRole;
    }

    public String getEmployeeAccount() {
        return employeeAccount;
    }

    public void setEmployeeAccount(String employeeAccount) {
        this.employeeAccount = employeeAccount;
    }

    public EmployeeCustomerRelation getRelation() {
        return relation;
    }

    public void setRelation(EmployeeCustomerRelation relation) {
        this.relation = relation;
    }

    public DateWindow getValidDates() {
        return validDates;
    }

    public void setValidDates(DateWindow validDates) {
        this.validDates = validDates;
    }
}
