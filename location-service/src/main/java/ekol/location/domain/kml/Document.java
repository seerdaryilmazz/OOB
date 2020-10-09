package ekol.location.domain.kml;

import java.util.List;

public class Document {

    private String name;
    private String description;
    private List<Style> styleList;
    private List<Placemark> placemarkList;

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

    public List<Style> getStyleList() {
        return styleList;
    }

    public void setStyleList(List<Style> styleList) {
        this.styleList = styleList;
    }

    public List<Placemark> getPlacemarkList() {
        return placemarkList;
    }

    public void setPlacemarkList(List<Placemark> placemarkList) {
        this.placemarkList = placemarkList;
    }
}
