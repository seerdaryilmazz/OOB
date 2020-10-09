package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by kilimci on 06/05/16.
 */
@Entity
@Table(name = "ContactTitle")
@SequenceGenerator(name = "SEQ_CONTACTTITLE", sequenceName = "SEQ_CONTACTTITLE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class ContactTitle extends LookupEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONTACTTITLE")
    private Long id ;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

}
