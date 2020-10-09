package ekol.location.domain;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.BaseEntity;

/**
 * Created by ozer on 26/12/2016.
 */
@Entity
@Table(name = "zone_polygon_region")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "ZonePolygonRegion.allDetails",
                attributeNodes = {
                        @NamedAttributeNode("polygonRegion")
                }
        )
})
public class ZonePolygonRegion extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_zone_polygon_region", sequenceName = "seq_zone_polygon_region")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_zone_polygon_region")
    private Long id;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    @JsonBackReference
    private Zone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(column = @JoinColumn(name = "parent", referencedColumnName = "parent")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "name", referencedColumnName = "name")),
    })
    private PolygonRegion polygonRegion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_zip_code_id")
    private ZoneZipCode zoneZipCode;

    @Transient
    private String zoneZipCodeRep;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public PolygonRegion getPolygonRegion() {
        return polygonRegion;
    }

    public void setPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
    }

    public ZoneZipCode getZoneZipCode() {
        return zoneZipCode;
    }

    public void setZoneZipCode(ZoneZipCode zoneZipCode) {
        this.zoneZipCode = zoneZipCode;
    }

    public String getZoneZipCodeRep() {
        return zoneZipCodeRep;
    }

    public void setZoneZipCodeRep(String zoneZipCodeRep) {
        this.zoneZipCodeRep = zoneZipCodeRep;
    }
}
