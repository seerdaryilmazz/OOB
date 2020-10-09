package ekol.hibernate5.builder;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;

import java.time.LocalDateTime;

public final class FixedZoneDateTimeBuilder {
    private LocalDateTime dateTime;
    private String timeZone;

    private FixedZoneDateTimeBuilder() {
    }

    public static FixedZoneDateTimeBuilder aFixedZoneDateTime() {
        return new FixedZoneDateTimeBuilder();
    }

    public FixedZoneDateTimeBuilder withDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public FixedZoneDateTimeBuilder withTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public FixedZoneDateTimeBuilder but() {
        return aFixedZoneDateTime().withDateTime(dateTime).withTimeZone(timeZone);
    }

    public FixedZoneDateTime build() {
        FixedZoneDateTime fixedZoneDateTime = new FixedZoneDateTime();
        fixedZoneDateTime.setDateTime(dateTime);
        fixedZoneDateTime.setTimeZone(timeZone);
        return fixedZoneDateTime;
    }
}
