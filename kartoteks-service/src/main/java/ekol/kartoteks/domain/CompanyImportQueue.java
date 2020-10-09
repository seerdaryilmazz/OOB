package ekol.kartoteks.domain;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.exceptions.ApplicationException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.kartoteks.domain.exchange.CompanyExchangeData;

/**
 * Created by fatmaozyildirim on 4/7/16.
 */

@Entity
@Table(name="CompanyImportQueue")
@SequenceGenerator(name="SEQ_COMPANYIMPORTQUEUE",sequenceName = "SEQ_COMPANYIMPORTQUEUE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
public class CompanyImportQueue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_COMPANYIMPORTQUEUE")
    private Long id ;

    @Column(nullable = false, length = 100)
    private String userName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "createDate"))
    })
    @Column(nullable = false)
    private UtcDateTime createDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "completedDate"))
    })
    @Column(nullable = false)
    private UtcDateTime completedDate;

    @Column(nullable= false, length = 100)
    @Enumerated(EnumType.STRING)
    private RemoteApplication application;

    @Enumerated(value=EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ImportQueueStatus status;

    @Basic(fetch=FetchType.LAZY)
    @Column(name="data", nullable = false, columnDefinition = "clob")
    private String data;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "import_queue_company_type",
            joinColumns=@JoinColumn(name = "import_queue_id"))
    @NotAudited
    private Set<String> companyRoleTypes = new HashSet<>();

    @Column
    private Long companyId;

    @Column(length = 50)
    private String applicationCompanyId;

    @Column(nullable = false )
    private String companyName ;

    @Column
    private String customerCompanyCode ;

    @Column
    private String orderCode ;

    @Column
    private String completedBy ;

    private transient CompanyExchangeData company;

    public CompanyImportQueue() {
        setCreateDate(new UtcDateTime(LocalDateTime.now()));
    }

    public void toProperCase(){
        if(StringUtils.isNotBlank(getUserName())){
            setUserName(getUserName().toLowerCase());
        }
    }

    public void copySummaryFromCompany(){
        setCompanyId(getCompany().getKartoteksId());
        setApplicationCompanyId(getCompany().getCompanyId());
        setCompanyName(getCompany().getName());
        setCustomerCompanyCode(getCompany().getOrder().getCompanyId());
        setOrderCode(getCompany().getOrder().getOrderNumber());
        getCompany().getRoles().forEach(role -> {
            if(StringUtils.isNotBlank(role.getRelationCode())){
                getCompanyRoleTypes().add(role.getRelationCode());
            }
        });
    }


    public CompanyExchangeData parseCompanyJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(getData(), CompanyExchangeData.class);
        } catch (IOException e) {
            throw new ApplicationException("JSON parse exception", e);
        }
    }

    public void setStatusSuccess(String username){
        setStatus(ImportQueueStatus.SUCCESS);
        setCompletedDate(new UtcDateTime(LocalDateTime.now()));
        setCompletedBy(username);
    }

    public void refreshData(){
        setData(getCompany().toJSON());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UtcDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(UtcDateTime createDate) {
        this.createDate = createDate;
    }

    public RemoteApplication getApplication() {
        return application;
    }

    public void setApplication(RemoteApplication application) {
        this.application = application;
    }

    public ImportQueueStatus getStatus() {
        return status;
    }

    public void setStatus(ImportQueueStatus status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyExchangeData getCompany() {
        return company;
    }

    public void setCompany(CompanyExchangeData companyExchangeData) {
        this.company = companyExchangeData;
    }

    public String getApplicationCompanyId() {
        return applicationCompanyId;
    }

    public void setApplicationCompanyId(String applicationCompanyId) {
        this.applicationCompanyId = applicationCompanyId;
    }

    public UtcDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(UtcDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public Set<String> getCompanyRoleTypes() {
        return companyRoleTypes;
    }

    public void setCompanyRoleTypes(Set<String> companyRoleTypes) {
        this.companyRoleTypes = companyRoleTypes;
    }

    public String getCustomerCompanyCode() {
        return customerCompanyCode;
    }

    public void setCustomerCompanyCode(String customerCompanyCode) {
        this.customerCompanyCode = customerCompanyCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
