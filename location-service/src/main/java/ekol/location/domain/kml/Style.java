package ekol.location.domain.kml;

public class Style {

    private String id;
    private LineStyle lineStyle;
    private PolyStyle polyStyle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public PolyStyle getPolyStyle() {
        return polyStyle;
    }

    public void setPolyStyle(PolyStyle polyStyle) {
        this.polyStyle = polyStyle;
    }
}
