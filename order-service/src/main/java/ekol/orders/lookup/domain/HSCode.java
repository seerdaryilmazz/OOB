package ekol.orders.lookup.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by burak on 08/08/16.
 */
@Entity
@Table(name = "hscode")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HSCode extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_hscode", sequenceName = "seq_hscode")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hscode")
    private Long id;

    @Column(
            nullable = false
    )
    private Short tier;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Short getTier() {
        return tier;
    }

    public void setTier(Short tier) {
        this.tier = tier;
    }
}
