package ekol.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by burak on 15/12/17.
 */
@Entity
@Table(name = "RouteLegExpeditionTrip")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteLegExpeditionTrip extends BaseEntity{

    @Id
    @SequenceGenerator(name = "seq_route_leg_expedition_trip", sequenceName = "seq_route_leg_expedition_trip")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_route_leg_expedition_trip")
    private Long id;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "expeditionId")
    private RouteLegExpedition expedition;

    private Long tripId;
    private String tripPlanCode;

    private Long trailerId;
    private String trailerPlate;

    private Long spotVehicleId;
    private Long spotVehicleRouteId;
    private String spotVehicleCarrierPlate;

    //kartoteks location id
    private Long nextLocationId;
    private String nextLocationName;
    private String nextLocationPostalCode;
    private String nextLocationCountryCode;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "dateTime", column = @Column(name = "nextLocationEta")),
            @AttributeOverride(name = "timeZone", column = @Column(name = "nextLocationEtaTz"))
    })
    private FixedZoneDateTime nextLocationEta;
    private String finalLocationCountryCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RouteLegExpedition getExpedition() {
        return expedition;
    }

    public void setExpedition(RouteLegExpedition expedition) {
        this.expedition = expedition;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getTripPlanCode() {
        return tripPlanCode;
    }

    public void setTripPlanCode(String tripPlanCode) {
        this.tripPlanCode = tripPlanCode;
    }

    public Long getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(Long trailerId) {
        this.trailerId = trailerId;
    }

    public String getTrailerPlate() {
        return trailerPlate;
    }

    public void setTrailerPlate(String trailerPlate) {
        this.trailerPlate = trailerPlate;
    }

    public Long getSpotVehicleId() {
        return spotVehicleId;
    }

    public void setSpotVehicleId(Long spotVehicleId) {
        this.spotVehicleId = spotVehicleId;
    }

    public Long getSpotVehicleRouteId() {
        return spotVehicleRouteId;
    }

    public void setSpotVehicleRouteId(Long spotVehicleRouteId) {
        this.spotVehicleRouteId = spotVehicleRouteId;
    }

    public String getSpotVehicleCarrierPlate() {
        return spotVehicleCarrierPlate;
    }

    public void setSpotVehicleCarrierPlate(String spotVehicleCarrierPlate) {
        this.spotVehicleCarrierPlate = spotVehicleCarrierPlate;
    }

    public Long getNextLocationId() {
        return nextLocationId;
    }

    public void setNextLocationId(Long nextLocationId) {
        this.nextLocationId = nextLocationId;
    }

    public String getNextLocationName() {
        return nextLocationName;
    }

    public void setNextLocationName(String nextLocationName) {
        this.nextLocationName = nextLocationName;
    }

    public String getNextLocationPostalCode() {
        return nextLocationPostalCode;
    }

    public void setNextLocationPostalCode(String nextLocationPostalCode) {
        this.nextLocationPostalCode = nextLocationPostalCode;
    }

    public String getNextLocationCountryCode() {
        return nextLocationCountryCode;
    }

    public void setNextLocationCountryCode(String nextLocationCountryCode) {
        this.nextLocationCountryCode = nextLocationCountryCode;
    }

    public FixedZoneDateTime getNextLocationEta() {
        return nextLocationEta;
    }

    public void setNextLocationEta(FixedZoneDateTime nextLocationEta) {
        this.nextLocationEta = nextLocationEta;
    }

    public String getFinalLocationCountryCode() {
        return finalLocationCountryCode;
    }

    public void setFinalLocationCountryCode(String finalLocationCountryCode) {
        this.finalLocationCountryCode = finalLocationCountryCode;
    }
}
