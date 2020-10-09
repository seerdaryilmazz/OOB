package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 04/05/16.
 */
@Entity
@Table(name = "CompanyLocationIdMapping")
@SequenceGenerator(name = "SEQ_COMPANYLOCATIONIDMAPPING", sequenceName = "SEQ_COMPANYLOCATIONIDMAPPING")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
public class CompanyLocationIdMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYLOCATIONIDMAPPING")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_location_id")
    @JsonBackReference(value = "mappedIds")
    private CompanyLocation companyLocation;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RemoteApplication application;

    @Column(nullable = false, length = 20)
    private String applicationLocationId;

    public static CompanyLocationIdMapping withApplication(RemoteApplication application, String id){
        CompanyLocationIdMapping mapping = new CompanyLocationIdMapping();
        mapping.setApplication(application);
        mapping.setApplicationLocationId(id);
        return mapping;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyLocation getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(CompanyLocation companyLocation) {
        this.companyLocation = companyLocation;
    }

    public RemoteApplication getApplication() {
        return application;
    }

    public void setApplication(RemoteApplication application) {
        this.application = application;
    }

    public String getApplicationLocationId() {
        return applicationLocationId;
    }

    public void setApplicationLocationId(String applicationLocationId) {
        this.applicationLocationId = applicationLocationId;
    }
}
