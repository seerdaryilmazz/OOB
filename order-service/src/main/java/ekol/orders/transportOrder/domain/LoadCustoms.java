package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Here, 'customs' is not the plural form of 'custom', it is the actual word.
 * customs: the official department that administers and collects the duties levied by a government on imported goods
 */

@Entity
@Table(name = "load_customs")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadCustoms extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_load_customs", sequenceName = "seq_load_customs")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_load_customs")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id")
    private Load load;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Load getLoad() {
        return load;
    }

    public void setLoad(Load load) {
        this.load = load;
    }
}
