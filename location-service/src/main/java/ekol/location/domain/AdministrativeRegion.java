package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AdmRegion")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdministrativeRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_adm_region", sequenceName = "seq_adm_region")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adm_region")
    private Long id;

    private String name;

    @Column(name = "lvl") // 'level' reserved word olduğu için zorluk çıkarıyor.
    private Integer level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    @JsonIgnore
    private AdministrativeRegion parent;

    private String countryIsoAlpha2Code;

    @OneToMany(mappedBy = "region")
    @Where(clause = "deleted = 0")
    @JsonManagedReference
    private Set<AdministrativeRegionPostcode> postcodes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public AdministrativeRegion getParent() {
        return parent;
    }

    public void setParent(AdministrativeRegion parent) {
        this.parent = parent;
    }

    public String getCountryIsoAlpha2Code() {
        return countryIsoAlpha2Code;
    }

    public void setCountryIsoAlpha2Code(String countryIsoAlpha2Code) {
        this.countryIsoAlpha2Code = countryIsoAlpha2Code;
    }

    public Set<AdministrativeRegionPostcode> getPostcodes() {
        return postcodes;
    }

    public void setPostcodes(Set<AdministrativeRegionPostcode> postcodes) {
        this.postcodes = postcodes;
    }
}
