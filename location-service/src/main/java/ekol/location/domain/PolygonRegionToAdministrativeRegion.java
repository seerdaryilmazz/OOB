package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "PolygonRegionToAdmRegion")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolygonRegionToAdministrativeRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_polygonregion_to_admregion", sequenceName = "seq_polygonregion_to_admregion")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_polygonregion_to_admregion")
    private Long id;

    private String countryIsoAlpha3Code;

    private String polygonRegion;

    @Column(name = "admRegion")
    private String administrativeRegion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryIsoAlpha3Code() {
        return countryIsoAlpha3Code;
    }

    public void setCountryIsoAlpha3Code(String countryIsoAlpha3Code) {
        this.countryIsoAlpha3Code = countryIsoAlpha3Code;
    }

    public String getPolygonRegion() {
        return polygonRegion;
    }

    public void setPolygonRegion(String polygonRegion) {
        this.polygonRegion = polygonRegion;
    }

    public String getAdministrativeRegion() {
        return administrativeRegion;
    }

    public void setAdministrativeRegion(String administrativeRegion) {
        this.administrativeRegion = administrativeRegion;
    }
}
