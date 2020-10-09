package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by kilimci on 24/06/16.
 */
@Entity
@Table(name ="CompanyRoleType")
@SequenceGenerator(name = "SEQ_COMPANYROLETYPE",sequenceName = "SEQ_COMPANYROLETYPE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class CompanyRoleType extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_COMPANYROLETYPE")
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
