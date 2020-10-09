package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by fatmaozyildirim on 4/5/16.
 */
@Entity
@Table(name ="CompanyRelationType")
@SequenceGenerator(name = "SEQ_COMPANYRELATIONTYPE",sequenceName = "SEQ_COMPANYRELATIONTYPE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class CompanyRelationType extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_COMPANYRELATIONTYPE")
    private Long id;

    @Column(nullable = false)
    private String altName;

    private static final String AGENT_CODE = "ZCPG01";
    private static final String LOGISTICS_PARTNER_CODE = "ZCRME2";

    public boolean isAgentRelation(){
        return getCode().equalsIgnoreCase(AGENT_CODE);
    }
    public boolean isLogisticsPartnerRelation(){
        return getCode().equalsIgnoreCase(LOGISTICS_PARTNER_CODE);
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }
}


