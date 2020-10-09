package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by ozer on 14/12/16.
 */
@Entity
@Table(name = "zone_zip_code")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneZipCode extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_zone_zip_code", sequenceName = "seq_zone_zip_code")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_zone_zip_code")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    @JsonBackReference
    private Zone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Enumerated(EnumType.STRING)
    private ZoneZipCodeType zoneZipCodeType;

    @Column
    private String value1;

    @Column
    private String value2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ZoneZipCodeType getZoneZipCodeType() {
        return zoneZipCodeType;
    }

    public void setZoneZipCodeType(ZoneZipCodeType zoneZipCodeType) {
        this.zoneZipCodeType = zoneZipCodeType;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getRep() {
        // Should be the same as ZoneMap.getZoneZipCodeRep
        if (zoneZipCodeType == ZoneZipCodeType.STARTS) {
            return country.getIso() + " " + value1 + "*";
        } else if (zoneZipCodeType == ZoneZipCodeType.EQUALS) {
            return country.getIso() + " " + value1;
        } else {
            return null;
        }
    }
}
