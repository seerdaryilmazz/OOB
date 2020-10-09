package ekol.orders.transportOrder.elastic.shipment.document;


import ekol.orders.transportOrder.domain.Location;
import ekol.orders.transportOrder.elastic.shipment.model.LocationType;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.math.BigDecimal;

/**
 * Created by ozer on 03/10/16.
 */
public class LocationDocument {

    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.String, index = FieldIndex.not_analyzed),
            otherFields = {
                    @InnerField(type = FieldType.String, indexAnalyzer = "turkish", searchAnalyzer = "turkish", suffix = "tr")
            }
    )
    private String name;

    private String postalCode;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countryIsoCode;

    private GeoPoint location;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String type;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String timezone;


    public static LocationDocument fromLocation(Location location, LocationType locationType){
        LocationDocument document = new LocationDocument();

        if (location != null) {
            document.setId(String.valueOf(location.getId()));
            document.setName(location.getName());
            if (location.getPostaladdress() != null) {
                document.setPostalCode(location.getPostaladdress().getPostalCode());
                if (location.getPostaladdress().getCountry() != null) {
                    document.setCountryIsoCode(location.getPostaladdress().getCountry().getIso());
                }
                if (location.getPostaladdress().getPointOnMap() != null) {
                    BigDecimal lat = location.getPostaladdress().getPointOnMap().getLat();
                    BigDecimal lng = location.getPostaladdress().getPointOnMap().getLng();
                    if (lat != null && lng != null) {
                        document.setLocation(new GeoPoint(lat.doubleValue(), lng.doubleValue()));
                    }
                }

            }
            document.setTimezone(location.getTimezone());
            document.setType(locationType.name());
        }
        return document;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
