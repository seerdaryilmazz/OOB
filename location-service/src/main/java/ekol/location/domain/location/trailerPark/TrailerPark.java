package ekol.location.domain.location.trailerPark;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.LocationType;
import ekol.location.domain.location.comnon.Place;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by kilimci on 02/05/2017.
 */

@Entity
@Table(name="trailer_park")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrailerPark extends Place{

    @Id
    @SequenceGenerator(name = "seq_trailer_park", sequenceName = "seq_trailer_park")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trailer_park")
    private Long id;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean hasSecurityStaff;
    
    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean hasCamera;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHasSecurityStaff() {
        return hasSecurityStaff;
    }

    public void setHasSecurityStaff(boolean hasSecurityStaff) {
        this.hasSecurityStaff = hasSecurityStaff;
    }

    public boolean isHasCamera() {
        return hasCamera;
    }

    public void setHasCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    @Override
    public LocationType getType() {
        return LocationType.PARKING_AREA;
    }
}
