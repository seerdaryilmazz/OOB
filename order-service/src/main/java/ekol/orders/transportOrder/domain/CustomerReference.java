package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "customer_reference")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerReference extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_customer_reference", sequenceName = "seq_customer_reference")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customer_reference")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
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
