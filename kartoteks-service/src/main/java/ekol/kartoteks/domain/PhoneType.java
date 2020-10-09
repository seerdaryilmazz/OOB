package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by fatmaozyildirim on 5/4/16.
 */

@Entity
@Table(name ="PhoneType")
@SequenceGenerator(name = "SEQ_PHONETYPE",sequenceName = "SEQ_PHONETYPE")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class PhoneType extends LookupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_PHONETYPE")
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
