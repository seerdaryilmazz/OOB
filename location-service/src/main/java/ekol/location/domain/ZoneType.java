package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by ozer on 13/12/16.
 */
@Entity
@Table(name = "Zone_Type")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneType extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_zone_type", sequenceName = "seq_zone_type")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_zone_type")
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
