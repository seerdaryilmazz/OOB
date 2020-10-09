package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "AdmRegionPostcode")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdministrativeRegionPostcode extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_adm_region_postcode", sequenceName = "seq_adm_region_postcode")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adm_region_postcode")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
    @JsonBackReference
    private AdministrativeRegion region;

    private String postcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdministrativeRegion getRegion() {
        return region;
    }

    public void setRegion(AdministrativeRegion region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
