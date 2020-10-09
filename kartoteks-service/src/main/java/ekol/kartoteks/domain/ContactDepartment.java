package ekol.kartoteks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;

import javax.persistence.*;

/**
 * Created by kilimci on 06/05/16.
 */
@Entity
@Table(name = "ContactDepartment")
@SequenceGenerator(name = "SEQ_CONTACTDEPARTMENT", sequenceName = "SEQ_CONTACTDEPARTMENT")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
public class ContactDepartment extends LookupEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONTACTDEPARTMENT")
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
