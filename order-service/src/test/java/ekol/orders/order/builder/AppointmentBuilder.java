package ekol.orders.order.builder;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.order.domain.Appointment;

public final class AppointmentBuilder {
    private FixedZoneDateTime startDateTime;
    private FixedZoneDateTime endDateTime;

    private AppointmentBuilder() {
    }

    public static AppointmentBuilder anAppointment() {
        return new AppointmentBuilder();
    }

    public AppointmentBuilder withStartDateTime(FixedZoneDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public AppointmentBuilder withEndDateTime(FixedZoneDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public AppointmentBuilder but() {
        return anAppointment().withStartDateTime(startDateTime).withEndDateTime(endDateTime);
    }

    public Appointment build() {
        Appointment appointment = new Appointment();
        appointment.setStartDateTime(startDateTime);
        appointment.setEndDateTime(endDateTime);
        return appointment;
    }
}
