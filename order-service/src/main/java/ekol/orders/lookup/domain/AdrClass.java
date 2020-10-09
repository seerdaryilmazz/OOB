package ekol.orders.lookup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 05/08/16.
 */
@Entity
@Table(name = "AdrClass")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdrClass extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_adrclass", sequenceName = "seq_adrclass")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adrclass")
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
