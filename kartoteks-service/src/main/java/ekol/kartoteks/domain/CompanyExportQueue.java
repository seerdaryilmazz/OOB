package ekol.kartoteks.domain;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.kartoteks.serializers.IdNameSerializer;

/**
 * Created by kilimci on 01/08/16.
 */
@Entity
@Table(name="CompanyExportQueue")
@SequenceGenerator(name="SEQ_COMPANYEXPORTQUEUE",sequenceName = "SEQ_COMPANYEXPORTQUEUE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
public class CompanyExportQueue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_COMPANYEXPORTQUEUE")
    private Long id ;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createDate"))
    })
    @Column(nullable = false)
    private UtcDateTime createDate;

    @Column(nullable= false, length = 100)
    @Enumerated(EnumType.STRING)
    private RemoteApplication application;

    @Enumerated(value=EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExportQueueStatus status;

    @Basic(fetch=FetchType.LAZY)
    @Column(name="data", nullable = false, columnDefinition = "clob")
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonSerialize(using = IdNameSerializer.class)
    private Company company;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "latestExecuteDate"))
    })
    @Column(nullable = false)
    private UtcDateTime latestExecuteDate;

    @Column(length = 50)
    private String applicationCompanyId;

    private int retryCount;

    public void increaseRetryCount(){
        setRetryCount(getRetryCount()+1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RemoteApplication getApplication() {
        return application;
    }

    public void setApplication(RemoteApplication application) {
        this.application = application;
    }

    public ExportQueueStatus getStatus() {
        return status;
    }

    public void setStatus(ExportQueueStatus status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getApplicationCompanyId() {
        return applicationCompanyId;
    }

    public void setApplicationCompanyId(String applicationCompanyId) {
        this.applicationCompanyId = applicationCompanyId;
    }


    public UtcDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(UtcDateTime createDate) {
        this.createDate = createDate;
    }

    public UtcDateTime getLatestExecuteDate() {
        return latestExecuteDate;
    }

    public void setLatestExecuteDate(UtcDateTime latestExecuteDate) {
        this.latestExecuteDate = latestExecuteDate;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }


}
