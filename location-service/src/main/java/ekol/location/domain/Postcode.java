package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Postcode.allDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "polygonRegion")
                }
        )
})
@Entity
@Table(name = "Postcode")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Postcode extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_postcode", sequenceName = "seq_postcode")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_postcode")
    private Long id;

    private String value;

    /**
     * Veriyi aldığımız kaynakta bu alan her zaman dolu olmuyor, o yüzden bu alan nullable.
     */
    private BigDecimal latitude;

    /**
     * Veriyi aldığımız kaynakta bu alan her zaman dolu olmuyor, o yüzden bu alan nullable.
     */
    private BigDecimal longitude;

    private String countryIsoAlpha3Code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regionId")
    private PolygonRegion polygonRegion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCountryIsoAlpha3Code() {
        return countryIsoAlpha3Code;
    }

    public void setCountryIsoAlpha3Code(String countryIsoAlpha3Code) {
        this.countryIsoAlpha3Code = countryIsoAlpha3Code;
    }

    public PolygonRegion getPolygonRegion() {
        return polygonRegion;
    }

    public void setPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
    }
}
