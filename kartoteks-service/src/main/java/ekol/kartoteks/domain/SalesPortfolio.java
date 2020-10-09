package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by kilimci on 30/06/16.
 */
@Entity
@Table(name ="SalesPortfolio")
@SequenceGenerator(name = "SEQ_SALESPORTFOLIO",sequenceName = "SEQ_SALESPORTFOLIO")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class SalesPortfolio extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_SALESPORTFOLIO")
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
