package ekol.location.domain.kml;

import java.util.List;

public class Polygon {

    private OuterBoundaryIs outerBoundaryIs;
    private List<InnerBoundaryIs> innerBoundaryIsList;

    public OuterBoundaryIs getOuterBoundaryIs() {
        return outerBoundaryIs;
    }

    public void setOuterBoundaryIs(OuterBoundaryIs outerBoundaryIs) {
        this.outerBoundaryIs = outerBoundaryIs;
    }

    public List<InnerBoundaryIs> getInnerBoundaryIsList() {
        return innerBoundaryIsList;
    }

    public void setInnerBoundaryIsList(List<InnerBoundaryIs> innerBoundaryIsList) {
        this.innerBoundaryIsList = innerBoundaryIsList;
    }
}
