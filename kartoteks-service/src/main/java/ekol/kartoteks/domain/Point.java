package ekol.kartoteks.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */
@Embeddable
@Access(AccessType.FIELD)
public class Point implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column(precision = 9, scale = 6)
    private BigDecimal lat;

    @Column(precision = 9, scale = 6)
    private BigDecimal lng;

    public Point() {
        //Default constructor
    }

    public Point(BigDecimal lat, BigDecimal lng) {
        this.lat = lat;
        this.lng = lng;
    }

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

    @Override
    public String toString(){
        return lat + "," + lng;
    }

    @Override
    public boolean equals(Object other){
        if(other == null || !(other instanceof Point)){
            return false;
        }
        Point otherPoint = (Point)other;
        return getLat().equals(otherPoint.getLat()) && getLng().equals(otherPoint.getLng());
    }
    @Override
    public int hashCode(){
        int hashCode = 1;
        hashCode = 31 * hashCode + (getLat().hashCode());
        hashCode = 31 * hashCode + (getLng().hashCode());
        return hashCode;
    }
}
