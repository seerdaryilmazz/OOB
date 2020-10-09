package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 09/02/2017.
 */
@Entity
@Table(name = "CompanyContactIdMapping")
@SequenceGenerator(name = "SEQ_COMPANYCONTACTIDMAPPING", sequenceName = "SEQ_COMPANYCONTACTIDMAPPING")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
public class CompanyContactIdMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYCONTACTIDMAPPING")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_contact_id")
    @JsonBackReference
    private CompanyContact companyContact;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RemoteApplication application;

    @Column(nullable = false, length = 20)
    private String applicationContactId;

    public static CompanyContactIdMapping withApplication(RemoteApplication application, String id){
        CompanyContactIdMapping mapping = new CompanyContactIdMapping();
        mapping.setApplication(application);
        mapping.setApplicationContactId(id);
        return mapping;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyContact getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(CompanyContact companyContact) {
        this.companyContact = companyContact;
    }

    public String getApplicationContactId() {
        return applicationContactId;
    }

    public void setApplicationContactId(String applicationContactId) {
        this.applicationContactId = applicationContactId;
    }

    public RemoteApplication getApplication() {
        return application;
    }

    public void setApplication(RemoteApplication application) {
        this.application = application;
    }


}