package ekol.location.domain.kml;

public class Placemark {

    private String name;
    private String description;
    private String styleUrl;
    private String visibility;
    private MultiGeometry multiGeometry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public MultiGeometry getMultiGeometry() {
        return multiGeometry;
    }

    public void setMultiGeometry(MultiGeometry multiGeometry) {
        this.multiGeometry = multiGeometry;
    }
}
