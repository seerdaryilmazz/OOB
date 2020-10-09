package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by kilimci on 08/04/16.
 */
@Entity
@Table(name ="EmployeeCustomerRelation")
@SequenceGenerator(name = "SEQ_EMPLOYEECUSTOMERRELATION",sequenceName = "SEQ_EMPLOYEECUSTOMERRELATION")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class EmployeeCustomerRelation extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_EMPLOYEECUSTOMERRELATION")
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
