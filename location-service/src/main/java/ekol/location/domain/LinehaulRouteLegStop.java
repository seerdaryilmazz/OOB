package ekol.location.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.location.comnon.Place;
import ekol.location.domain.location.comnon.Point;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Bir rota ayağının hem başlangıcını hem de bitişini ifade etmek için kullandığımız sınıftır.
 * 'stop' kelimesini, 'durak' anlamında kullanıyoruz.
 */
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "LinehaulRouteLegStop.allDetails",
                attributeNodes = {
                        @NamedAttributeNode(value = "types")
                }
        )
})
@Entity
@Table(name = "LhaulRouteLegStop")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinehaulRouteLegStop extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_lhaul_route_leg_stop", sequenceName = "seq_lhaul_route_leg_stop")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lhaul_route_leg_stop")
    private Long id;

    private String name;

    private String timezone;

    @Embedded
    private Point pointOnMap;

    /**
     * Bir durak, aynı anda hem bir liman, hem bir tren terminali hem de bir depo olabilir.
     */
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "LhaulRouteLegStopTypes", joinColumns = @JoinColumn(name = "parentId"))
    @Column(name = "type")
    private Set<LocationType> types = new HashSet<>();

    public static LinehaulRouteLegStop withPlace(Place place){
        LinehaulRouteLegStop routeLegStop = new LinehaulRouteLegStop();
        routeLegStop.copyFrom(place);
        return routeLegStop;
    }

    public void copyFrom(Place place){
        setName(place.getName());
        setPointOnMap(place.getLocation() != null ? place.getLocation().getPointOnMap() : null);
        setTimezone(place.getLocation().getTimezone());
        if(getTypes() == null){
            setTypes(new HashSet<>());
        }
        getTypes().add(place.getType());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getPointOnMap() {
        return pointOnMap;
    }

    public void setPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
    }

    public Set<LocationType> getTypes() {
        return types;
    }

    public void setTypes(Set<LocationType> types) {
        this.types = types;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
