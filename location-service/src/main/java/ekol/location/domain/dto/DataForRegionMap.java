package ekol.location.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.PolygonRegion;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataForRegionMap {

    private List<PolygonRegion> polygonRegionsThatCanBeAdded = new ArrayList<>();
    private List<PolygonRegion> polygonRegionsThatCannotBeAdded = new ArrayList<>();
    private List<PolygonRegion> polygonRegionsThatAreAdded = new ArrayList<>();

    public List<PolygonRegion> getPolygonRegionsThatCanBeAdded() {
        return polygonRegionsThatCanBeAdded;
    }

    public void setPolygonRegionsThatCanBeAdded(List<PolygonRegion> polygonRegionsThatCanBeAdded) {
        this.polygonRegionsThatCanBeAdded = polygonRegionsThatCanBeAdded;
    }

    public List<PolygonRegion> getPolygonRegionsThatCannotBeAdded() {
        return polygonRegionsThatCannotBeAdded;
    }

    public void setPolygonRegionsThatCannotBeAdded(List<PolygonRegion> polygonRegionsThatCannotBeAdded) {
        this.polygonRegionsThatCannotBeAdded = polygonRegionsThatCannotBeAdded;
    }

    public List<PolygonRegion> getPolygonRegionsThatAreAdded() {
        return polygonRegionsThatAreAdded;
    }

    public void setPolygonRegionsThatAreAdded(List<PolygonRegion> polygonRegionsThatAreAdded) {
        this.polygonRegionsThatAreAdded = polygonRegionsThatAreAdded;
    }
}
