package ekol.location.domain.location.comnon;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

/**
 * Created by burak on 19/06/17.
 */
@Embeddable
@Access(AccessType.FIELD)
public class PolygonPoint {


    private static final long serialVersionUID = 1L;

    @Column(precision = 9, scale = 6)
    private BigDecimal lat;

    @Column(precision = 9, scale = 6)
    private BigDecimal lng;

    private Integer sortIndex;


    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof PolygonPoint)) {
            return false;
        }
        PolygonPoint otherPoint = (PolygonPoint) other;
        return (
                ((getLat() == null && otherPoint.getLat() == null) || getLat().equals(otherPoint.getLat()))
                        &&
                        ((getLng() == null && otherPoint.getLng() == null) || getLng().equals(otherPoint.getLng()))
                        &&
                        ((getSortIndex() == null && otherPoint.getSortIndex() == null) || getSortIndex().equals(otherPoint.getSortIndex()))
        );
    }

    @Override
    public int hashCode(){
        int hashCode = 1;
        hashCode = 31 * hashCode + (getLat() == null ? 0 : getLat().hashCode());
        hashCode = 31 * hashCode + (getLng() == null ? 0 : getLng().hashCode());
        hashCode = 31 * hashCode + (getSortIndex() == null ? 0 : getSortIndex());
        return hashCode;
    }



    @Override
    public String toString(){
        return getSortIndex() + "-" + getLat() + "," + getLng();
    }


}
