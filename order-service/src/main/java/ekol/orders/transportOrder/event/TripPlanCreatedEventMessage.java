package ekol.orders.transportOrder.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 17/08/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripPlanCreatedEventMessage {

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

    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripStop {
        private Long id;
        private IdNamePair location;
        private String locationType;
        private DateWithTimeZone plannedTimeArrival;
        private DateWithTimeZone plannedTimeDeparture;
        private DateWithTimeZone estimatedTimeArrival;
        private DateWithTimeZone estimatedTimeDeparture;
        private DateWithTimeZone actualTimeArrival;
        private DateWithTimeZone actualTimeDeparture;
        private List<TripOperation> tripOperations = new ArrayList<>();

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public IdNamePair getLocation() {
            return location;
        }

        public void setLocation(IdNamePair location) {
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
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripOperation{
        private Long id;
        private String tripOperationType;
        private Transport transport;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTripOperationType() {
            return tripOperationType;
        }

        public void setTripOperationType(String tripOperationType) {
            this.tripOperationType = tripOperationType;
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
        private Long segmentId;
        private IdNamePair fromLocation;
        private IdNamePair toLocation;
        private String transportStatus;
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

        public Long getSegmentId() {
            return segmentId;
        }

        public void setSegmentId(Long segmentId) {
            this.segmentId = segmentId;
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

        public String getTransportStatus() {
            return transportStatus;
        }

        public void setTransportStatus(String transportStatus) {
            this.transportStatus = transportStatus;
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
    public static class DateWithTimeZone {
        private LocalDateTime dateTime;
        private String timezone;

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
}
