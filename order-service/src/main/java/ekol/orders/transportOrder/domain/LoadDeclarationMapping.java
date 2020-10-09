package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "load_declaration_mapping")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadDeclarationMapping extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_load_declaration_mapping", sequenceName = "seq_load_declaration_mapping")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_load_declaration_mapping")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_id")
    private Load load;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "load_declaration_id")
    private LoadDeclaration loadDeclaration;

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

    public LoadDeclaration getLoadDeclaration() {
        return loadDeclaration;
    }

    public void setLoadDeclaration(LoadDeclaration loadDeclaration) {
        this.loadDeclaration = loadDeclaration;
    }
}
