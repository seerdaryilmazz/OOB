package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "TransportType")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportType extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_transport_type", sequenceName = "seq_transport_type")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transport_type")
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
