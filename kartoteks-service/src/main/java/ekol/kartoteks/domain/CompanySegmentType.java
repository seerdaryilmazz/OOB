package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by kilimci on 09/09/16.
 */
@Entity
@Table(name ="CompanySegmentType")
@SequenceGenerator(name = "SEQ_COMPANYSEGMENTTYPE",sequenceName = "SEQ_COMPANYSEGMENTTYPE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class CompanySegmentType extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_COMPANYSEGMENTTYPE")
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
