package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;

import java.time.LocalDateTime;

public final class FixedZoneDateTimeWindowBuilder {

    private LocalDateTime start;
    private LocalDateTime end;
    private String timeZone;

    private FixedZoneDateTimeWindowBuilder() {
    }

    public static FixedZoneDateTimeWindowBuilder aFixedZoneDateTimeWindow() {
        return new FixedZoneDateTimeWindowBuilder();
    }

    public FixedZoneDateTimeWindowBuilder withStart(LocalDateTime start) {
        this.start = start;
        return this;
    }

    public FixedZoneDateTimeWindowBuilder withEnd(LocalDateTime end) {
        this.end = end;
        return this;
    }

    public FixedZoneDateTimeWindowBuilder withTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public FixedZoneDateTimeWindow build() {
        FixedZoneDateTimeWindow fixedZoneDateTimeWindow = new FixedZoneDateTimeWindow();
        fixedZoneDateTimeWindow.setStart(start);
        fixedZoneDateTimeWindow.setEnd(end);
        fixedZoneDateTimeWindow.setTimeZone(timeZone);
        return fixedZoneDateTimeWindow;
    }
}
