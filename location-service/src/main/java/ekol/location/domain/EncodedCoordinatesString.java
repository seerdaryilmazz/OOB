package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "EncodedCoordsStr")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class EncodedCoordinatesString extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_encoded_coords_str", sequenceName = "seq_encoded_coords_str")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_encoded_coords_str")
    private Long id;

    /**
     * Coordinates are encoded to form a string according to 'Encoded Polyline Algorithm Format'.
     * See <a href="https://developers.google.com/maps/documentation/utilities/polylinealgorithm">this</a> for details.
     */
    @Column(columnDefinition = "clob")
    private String value;

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
}
