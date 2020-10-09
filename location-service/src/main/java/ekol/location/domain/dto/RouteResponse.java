package ekol.location.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.LocationType;
import ekol.location.domain.RouteLegType;
import ekol.location.domain.location.port.Port;
import ekol.location.domain.location.terminal.Terminal;
import ekol.location.domain.location.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 06/12/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteResponse {

    private Long id;
    private String name;
    private List<RouteLegResponse> routeLegs = new ArrayList<>();

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

    public List<RouteLegResponse> getRouteLegs() {
        return routeLegs;
    }

    public void setRouteLegs(List<RouteLegResponse> routeLegs) {
        this.routeLegs = routeLegs;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RouteLegResponse {

        private Long id;

        private List<RouteLegStopResponse> fromLocations = new ArrayList<>();
        private List<RouteLegStopResponse> toLocations = new ArrayList<>();

        private RouteLegType routeLegType;

        private List<RouteLegExpeditionResponse> expeditions;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<RouteLegStopResponse> getFromLocations() {
            return fromLocations;
        }

        public void setFromLocations(List<RouteLegStopResponse> fromLocations) {
            this.fromLocations = fromLocations;
        }

        public List<RouteLegStopResponse> getToLocations() {
            return toLocations;
        }

        public void setToLocations(List<RouteLegStopResponse> toLocations) {
            this.toLocations = toLocations;
        }

        public RouteLegType getRouteLegType() {
            return routeLegType;
        }

        public void setRouteLegType(RouteLegType routeLegType) {
            this.routeLegType = routeLegType;
        }

        public List<RouteLegExpeditionResponse> getExpeditions() {
            return expeditions;
        }

        public void setExpeditions(List<RouteLegExpeditionResponse> expeditions) {
            this.expeditions = expeditions;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RouteLegStopResponse {

        private Long id;
        private String name;
        private Long locationId;
        private Long companyLocationId;
        private LocationType locationType;

        public static RouteLegStopResponse with(Warehouse warehouse) {
            RouteLegStopResponse response = new RouteLegStopResponse();
            response.setId(warehouse.getId());
            response.setName(warehouse.getName());
            response.setLocationId(warehouse.getLocation() != null ? warehouse.getLocation().getId() : null);
            response.setCompanyLocationId(warehouse.getCompanyLocation() != null ? warehouse.getCompanyLocation().getId() : null);
            response.setLocationType(warehouse.getType());
            return response;
        }

        public static RouteLegStopResponse with(Port port) {
            RouteLegStopResponse response = new RouteLegStopResponse();
            response.setId(port.getId());
            response.setName(port.getName());
            response.setLocationId(port.getLocation() != null ? port.getLocation().getId() : null);
            response.setCompanyLocationId(null);
            response.setLocationType(port.getType());
            return response;
        }

        public static RouteLegStopResponse with(Terminal terminal) {
            RouteLegStopResponse response = new RouteLegStopResponse();
            response.setId(terminal.getId());
            response.setName(terminal.getName());
            response.setLocationId(terminal.getLocation() != null ? terminal.getLocation().getId() : null);
            response.setLocationType(terminal.getType());
            return response;
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

        public LocationType getLocationType() {
            return locationType;
        }

        public void setLocationType(LocationType locationType) {
            this.locationType = locationType;
        }
    }

}
