package ekol.location.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.model.IdNamePair;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 17/08/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripPlanEventMessage {

    private Long id;
    private String code;
    private Double grossWeight;
    private Double volume;
    private Double ldm;
    private Double payWeight;
    private List<Trip> trips = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getLdm() {
        return ldm;
    }

    public void setLdm(Double ldm) {
        this.ldm = ldm;
    }

    public Double getPayWeight() {
        return payWeight;
    }

    public void setPayWeight(Double payWeight) {
        this.payWeight = payWeight;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trip {
        private Long id;
        private TripStop fromTripStop;
        private TripStop toTripStop;
        private VehicleGroup vehicleGroup;
        private SpotVehicle spotVehicle;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public TripStop getFromTripStop() {
            return fromTripStop;
        }

        public void setFromTripStop(TripStop fromTripStop) {
            this.fromTripStop = fromTripStop;
        }

        public TripStop getToTripStop() {
            return toTripStop;
        }

        public void setToTripStop(TripStop toTripStop) {
            this.toTripStop = toTripStop;
        }

        public VehicleGroup getVehicleGroup() {
            return vehicleGroup;
        }

        public void setVehicleGroup(VehicleGroup vehicleGroup) {
            this.vehicleGroup = vehicleGroup;
        }

        public SpotVehicle getSpotVehicle() {
            return spotVehicle;
        }

        public void setSpotVehicle(SpotVehicle spotVehicle) {
            this.spotVehicle = spotVehicle;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripStop {
        private Long id;
        private Location location;
        private String locationType;
        private DateWithTimeZone plannedTimeArrival;
        private DateWithTimeZone plannedTimeDeparture;
        private DateWithTimeZone estimatedTimeArrival;
        private DateWithTimeZone estimatedTimeDeparture;
        private DateWithTimeZone actualTimeArrival;
        private DateWithTimeZone actualTimeDeparture;
        private List<TripOperation> tripOperations = new ArrayList<>();
        private TripRoute route;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getLocationType() {
            return locationType;
        }


        public void setLocationType(String locationType) {
            this.locationType = locationType;
        }

        public DateWithTimeZone getPlannedTimeArrival() {
            return plannedTimeArrival;
        }

        public void setPlannedTimeArrival(DateWithTimeZone plannedTimeArrival) {
            this.plannedTimeArrival = plannedTimeArrival;
        }

        public DateWithTimeZone getPlannedTimeDeparture() {
            return plannedTimeDeparture;
        }

        public void setPlannedTimeDeparture(DateWithTimeZone plannedTimeDeparture) {
            this.plannedTimeDeparture = plannedTimeDeparture;
        }

        public DateWithTimeZone getEstimatedTimeArrival() {
            return estimatedTimeArrival;
        }

        public void setEstimatedTimeArrival(DateWithTimeZone estimatedTimeArrival) {
            this.estimatedTimeArrival = estimatedTimeArrival;
        }

        public DateWithTimeZone getEstimatedTimeDeparture() {
            return estimatedTimeDeparture;
        }

        public void setEstimatedTimeDeparture(DateWithTimeZone estimatedTimeDeparture) {
            this.estimatedTimeDeparture = estimatedTimeDeparture;
        }

        public DateWithTimeZone getActualTimeArrival() {
            return actualTimeArrival;
        }

        public void setActualTimeArrival(DateWithTimeZone actualTimeArrival) {
            this.actualTimeArrival = actualTimeArrival;
        }

        public DateWithTimeZone getActualTimeDeparture() {
            return actualTimeDeparture;
        }

        public void setActualTimeDeparture(DateWithTimeZone actualTimeDeparture) {
            this.actualTimeDeparture = actualTimeDeparture;
        }

        public List<TripOperation> getTripOperations() {
            return tripOperations;
        }

        public void setTripOperations(List<TripOperation> tripOperations) {
            this.tripOperations = tripOperations;
        }

        public TripRoute getRoute() {
            return route;
        }

        public void setRoute(TripRoute route) {
            this.route = route;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location{
        private Long id;
        private String name;
        private String address;
        private String postalCode;
        private String countryCode;
        private PointOnMap pointOnMap;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public PointOnMap getPointOnMap() {
            return pointOnMap;
        }

        public void setPointOnMap(PointOnMap pointOnMap) {
            this.pointOnMap = pointOnMap;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PointOnMap{
        private BigDecimal lat;
        private BigDecimal lng;

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
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripOperation{
        private Long id;
        private Long segmentId;
        private String tripOperationType;
        private String operationStatus;
        private Transport transport;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getSegmentId() {
            return segmentId;
        }

        public void setSegmentId(Long segmentId) {
            this.segmentId = segmentId;
        }

        public String getTripOperationType() {
            return tripOperationType;
        }

        public void setTripOperationType(String tripOperationType) {
            this.tripOperationType = tripOperationType;
        }

        public String getOperationStatus() {
            return operationStatus;
        }

        public void setOperationStatus(String operationStatus) {
            this.operationStatus = operationStatus;
        }

        public Transport getTransport() {
            return transport;
        }

        public void setTransport(Transport transport) {
            this.transport = transport;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transport{
        private Long id;
        private Long shipmentId;
        private IdNamePair fromLocation;
        private IdNamePair toLocation;
        private String customer;
        private String details;
        private Double grossWeight;
        private Double volume;
        private Double ldm;
        private Double payWeight;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getShipmentId() {
            return shipmentId;
        }

        public void setShipmentId(Long shipmentId) {
            this.shipmentId = shipmentId;
        }

        public IdNamePair getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(IdNamePair fromLocation) {
            this.fromLocation = fromLocation;
        }

        public IdNamePair getToLocation() {
            return toLocation;
        }

        public void setToLocation(IdNamePair toLocation) {
            this.toLocation = toLocation;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Double getGrossWeight() {
            return grossWeight;
        }

        public void setGrossWeight(Double grossWeight) {
            this.grossWeight = grossWeight;
        }

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }

        public Double getLdm() {
            return ldm;
        }

        public void setLdm(Double ldm) {
            this.ldm = ldm;
        }

        public Double getPayWeight() {
            return payWeight;
        }

        public void setPayWeight(Double payWeight) {
            this.payWeight = payWeight;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VehicleGroup{
        private Long id;
        private IdNamePair loadable;
        private IdNamePair trailable;
        private IdNamePair drivable;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public IdNamePair getLoadable() {
            return loadable;
        }

        public void setLoadable(IdNamePair loadable) {
            this.loadable = loadable;
        }

        public IdNamePair getTrailable() {
            return trailable;
        }

        public void setTrailable(IdNamePair trailable) {
            this.trailable = trailable;
        }

        public IdNamePair getDrivable() {
            return drivable;
        }

        public void setDrivable(IdNamePair drivable) {
            this.drivable = drivable;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SpotVehicle {
        private Long id;
        private Long vehicleId;
        private Long vehicleRouteId;
        private String code;
        private String carrierPlateNumber;
        private String motorVehiclePlateNumber;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
        }

        public Long getVehicleRouteId() {
            return vehicleRouteId;
        }

        public void setVehicleRouteId(Long vehicleRouteId) {
            this.vehicleRouteId = vehicleRouteId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCarrierPlateNumber() {
            return carrierPlateNumber;
        }

        public void setCarrierPlateNumber(String carrierPlateNumber) {
            this.carrierPlateNumber = carrierPlateNumber;
        }

        public String getMotorVehiclePlateNumber() {
            return motorVehiclePlateNumber;
        }

        public void setMotorVehiclePlateNumber(String motorVehiclePlateNumber) {
            this.motorVehiclePlateNumber = motorVehiclePlateNumber;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DateWithTimeZone {
        private LocalDateTime dateTime;
        private String timezone;

        public static DateWithTimeZone createWithFixedZoneDateTime(FixedZoneDateTime fixedZoneDateTime){
            if(fixedZoneDateTime == null){
                return null;
            }
            DateWithTimeZone dateWithTimeZone = new DateWithTimeZone();
            dateWithTimeZone.setTimezone(fixedZoneDateTime.getTimeZone());
            dateWithTimeZone.setDateTime(fixedZoneDateTime.getDateTime());
            return dateWithTimeZone;
        }

        public FixedZoneDateTime toFixedZoneDateTime(){
            FixedZoneDateTime fixedZoneDateTime = new FixedZoneDateTime();
            fixedZoneDateTime.setDateTime(dateTime);
            fixedZoneDateTime.setTimeZone(timezone);
            return fixedZoneDateTime;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripRoute{
        private Long id;
        private String name;
        private List<TripRouteLeg> routeLegs = new ArrayList<>();

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

        public List<TripRouteLeg> getRouteLegs() {
            return routeLegs;
        }

        public void setRouteLegs(List<TripRouteLeg> routeLegs) {
            this.routeLegs = routeLegs;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripRouteLeg{

        private Long id;

        private Integer sortIndex;

        private String legType;

        private List<TripRouteLegStop> from = new ArrayList<>();
        private List<TripRouteLegStop> to = new ArrayList<>();

        private Long expeditionId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getSortIndex() {
            return sortIndex;
        }

        public void setSortIndex(Integer sortIndex) {
            this.sortIndex = sortIndex;
        }

        public String getLegType() {
            return legType;
        }

        public void setLegType(String legType) {
            this.legType = legType;
        }

        public List<TripRouteLegStop> getFrom() {
            return from;
        }

        public void setFrom(List<TripRouteLegStop> from) {
            this.from = from;
        }

        public List<TripRouteLegStop> getTo() {
            return to;
        }

        public void setTo(List<TripRouteLegStop> to) {
            this.to = to;
        }

        public Long getExpeditionId() {
            return expeditionId;
        }

        public void setExpeditionId(Long expeditionId) {
            this.expeditionId = expeditionId;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripRouteLegStop{

        private Long id;

        private Long locationId;
        private Long companyLocationId;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getLocationId() {
            return locationId;
        }

        public void setLocationId(Long locationId) {
            this.locationId = locationId;
        }

        public Long getCompanyLocationId() {
            return companyLocationId;
        }

        public void setCompanyLocationId(Long companyLocationId) {
            this.companyLocationId = companyLocationId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
