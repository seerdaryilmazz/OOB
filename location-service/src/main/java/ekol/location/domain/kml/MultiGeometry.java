package ekol.location.domain.kml;

import java.util.List;

public class MultiGeometry {

    private List<Polygon> polygonList;

    public List<Polygon> getPolygonList() {
        return polygonList;
    }

    public void setPolygonList(List<Polygon> polygonList) {
        this.polygonList = polygonList;
    }
}
