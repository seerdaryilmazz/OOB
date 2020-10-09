package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.json.AppointmentJson;

import java.time.ZonedDateTime;

public final class AppointmentJsonBuilder {
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

    private AppointmentJsonBuilder() {
    }

    public static AppointmentJsonBuilder anAppointmentJson() {
        return new AppointmentJsonBuilder();
    }

    public AppointmentJsonBuilder withStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public AppointmentJsonBuilder withEndDateTime(ZonedDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public AppointmentJsonBuilder but() {
        return anAppointmentJson().withStartDateTime(startDateTime).withEndDateTime(endDateTime);
    }

    public AppointmentJson build() {
        AppointmentJson appointmentJson = new AppointmentJson();
        appointmentJson.setStartDateTime(startDateTime);
        appointmentJson.setEndDateTime(endDateTime);
        return appointmentJson;
    }
}
