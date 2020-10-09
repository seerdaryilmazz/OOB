package ekol.usermgr.builder;

import ekol.hibernate5.domain.embeddable.DateTimeWindow;

import java.time.LocalDateTime;

public final class DateTimeWindowBuilder {

    private LocalDateTime start;
    private LocalDateTime end;

    private DateTimeWindowBuilder() {
    }

    public static DateTimeWindowBuilder aDateTimeWindow() {
        return new DateTimeWindowBuilder();
    }

    public DateTimeWindowBuilder withStart(LocalDateTime start) {
        this.start = start;
        return this;
    }

    public DateTimeWindowBuilder withEnd(LocalDateTime end) {
        this.end = end;
        return this;
    }

    public DateTimeWindow build() {
        DateTimeWindow dateTimeWindow = new DateTimeWindow();
        dateTimeWindow.setStart(start);
        dateTimeWindow.setEnd(end);
        return dateTimeWindow;
    }
}
