package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Coordinate")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "lastUpdated", "lastUpdatedBy"})
public class Coordinate extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_coordinate", sequenceName = "seq_coordinate")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_coordinate")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ringId")
    @JsonBackReference
    private CoordinateRing ring;

    private Integer orderNo;

    private BigDecimal longitude;

    private BigDecimal latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoordinateRing getRing() {
        return ring;
    }

    public void setRing(CoordinateRing ring) {
        this.ring = ring;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
