package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by ozer on 13/12/16.
 */
@Entity
@Table(name = "lcity")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class City extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lcity", sequenceName = "seq_lcity")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lcity")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    @JsonBackReference
    private Country country;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
