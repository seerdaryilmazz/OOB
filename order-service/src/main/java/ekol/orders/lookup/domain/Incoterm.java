package ekol.orders.lookup.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;

@Entity
@Table(name = "incoterm")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Incoterm extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_incoterm", sequenceName = "seq_incoterm")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_incoterm")
    private Long id;

    @Column(length = 255)
    private String description;

    @Column
    private boolean active;

    public static Incoterm with(Long id, String description){
        Incoterm incoterm = new Incoterm();
        incoterm.setId(id);
        incoterm.setDescription(description);
        return incoterm;
    }

    public static Incoterm with(IdNamePair idNamePair){
        return Incoterm.with(idNamePair.getId(), idNamePair.getName());
    }

    public static Incoterm with(IdCodeNameTrio idCodeNameTrio){
    	return Incoterm.with(idCodeNameTrio.getId(), idCodeNameTrio.getName());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
