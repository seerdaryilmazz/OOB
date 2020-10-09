package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.order.domain.dto.json.AppointmentJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentUnloadingAppointment {

    private Long shipmentId;
    private boolean deleted;
    private AppointmentJson appointment;

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public AppointmentJson getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentJson appointment) {
        this.appointment = appointment;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}