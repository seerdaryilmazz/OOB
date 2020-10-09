package ekol.location.testdata;

import ekol.location.builder.*;
import ekol.location.domain.*;
import ekol.location.domain.location.enumeration.PortRegistrationMethod;
import ekol.location.domain.location.port.Port;

import java.util.Set;

/**
 * Created by ozer on 02/02/2017.
 */
public class SomeData {

    public static Country someCountry() {
        return CountryBuilder.acountry()
                .withDeleted(false)
                .withAllowedChars("allowed")
                .withCurrency("TRL")
                .withEuMember(false)
                .withIso("is")
                .withIsoAlpha3Code("iso")
                .withLanguage("tr")
                .withName("Türkiye")
                .build();
    }

    public static Port somePort(Country country) {
        return somePort("port name", country);
    }

    public static Port somePort(String name, Country country) {
        return PortBuilder.aPort()
                .withName(name)
                .withLocalName(name)
                .withEntranceGate("entrance")
                .withEntranceTimeFrom(2)
                .withEntranceTimeTo(4)
                .withRegistrationMethod(PortRegistrationMethod.REGISTRATION_A)
                .build();
    }

    public static City someCity(Country country) {
        return CityBuilder.aCity()
                .withDeleted(false)
                .withName("city name")
                .withCountry(country)
                .build();
    }

    public static Region someRegion(Country country) {
        return RegionBuilder.aRegion()
                .withDeleted(false)
                .withName("region name")
                .withCountry(country)
                .build();
    }

    public static ZoneType someZoneType() {
        return ZoneTypeBuilder.aZoneType()
                .withName("name")
                .withDeleted(false)
                .withCode("code")
                .build();
    }

    public static Zone someZone(ZoneType zoneType, Set<ZonePolygonRegion> polygonRegions, Set<ZoneTag> tags, Set<ZoneZipCode> zipCodes) {
        return ZoneBuilder.aZone()
                .withName("zone name")
                .withDeleted(false)
                .withDescription("zone description")
                .withZoneType(zoneType)
                .withPolygonRegions(polygonRegions)
                .withTags(tags)
                .withZipCodes(zipCodes)
                .build();
    }

    public static ZoneTag someZoneTag(Zone zone) {
        return ZoneTagBuilder.aZoneTag()
                .withDeleted(false)
                .withValue("tag")
                .withZone(zone)
                .build();
    }

    public static ZoneZipCode someZoneZipCode(Zone zone, Country country) {
        return ZoneZipCodeBuilder.aZoneZipCode()
                .withZone(zone)
                .withCountry(country)
                .withValue1("value1")
                .withValue2("value2")
                .withZoneZipCodeType(ZoneZipCodeType.EQUALS)
                .build();
    }

    public static PolygonRegion somePolygonRegion() {
        return PolygonRegionBuilder.aPolygonRegion()
                .withDeleted(false)
                .withCountryIsoAlpha3Code("trl")
                .withLevel(1)
                .withLocalName("local name")
                .withName("name")
                .withParent("some parent")
                .build();
    }

    public static ZonePolygonRegion someZonePolygonRegion(Zone zone, PolygonRegion polygonRegion, ZoneZipCode zoneZipCode) {
        return ZonePolygonRegionBuilder.aZonePolygonRegion()
                .withDeleted(false)
                .withZone(zone)
                .withPolygonRegion(polygonRegion)
                .withSelected(true)
                .withZoneZipCode(zoneZipCode)
                .build();
    }
}
