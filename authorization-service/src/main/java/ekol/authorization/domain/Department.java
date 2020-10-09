package ekol.authorization.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.LookupEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "Department")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_department", sequenceName = "seq_department")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_department")
    private Long id;

    @Column
    private String homepage;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
}
