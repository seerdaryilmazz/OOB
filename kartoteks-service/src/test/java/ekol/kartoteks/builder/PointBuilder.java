package ekol.kartoteks.builder;

import ekol.kartoteks.domain.Point;

import java.math.BigDecimal;

/**
 * Created by kilimci on 14/10/16.
 */
public final class PointBuilder {
    private BigDecimal lat;
    private BigDecimal lng;

    private PointBuilder() {
    }

    public static PointBuilder aPoint() {
        return new PointBuilder();
    }

    public PointBuilder withLat(BigDecimal lat) {
        this.lat = lat;
        return this;
    }

    public PointBuilder withLng(BigDecimal lng) {
        this.lng = lng;
        return this;
    }

    public PointBuilder but() {
        return aPoint().withLat(lat).withLng(lng);
    }

    public Point build() {
        Point point = new Point();
        point.setLat(lat);
        point.setLng(lng);
        return point;
    }
}
