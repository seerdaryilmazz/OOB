package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 03/05/16.
 */
@Entity
@Table(name = "CompanyIdMapping")
@SequenceGenerator(name = "SEQ_COMPANYIDMAPPING", sequenceName = "SEQ_COMPANYIDMAPPING")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
public class CompanyIdMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYIDMAPPING")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RemoteApplication application;

    @Column(nullable = false, length = 20)
    private String applicationCompanyId;

    public static CompanyIdMapping withApplication(RemoteApplication application, String id){
        CompanyIdMapping mapping = new CompanyIdMapping();
        mapping.setApplication(application);
        mapping.setApplicationCompanyId(id);
        return mapping;
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

    public RemoteApplication getApplication() {
        return application;
    }

    public void setApplication(RemoteApplication application) {
        this.application = application;
    }

    public String getApplicationCompanyId() {
        return applicationCompanyId;
    }

    public void setApplicationCompanyId(String applicationCompanyId) {
        this.applicationCompanyId = applicationCompanyId;
    }
}
