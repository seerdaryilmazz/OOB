package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.order.domain.dto.json.AppointmentJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateShipmentDateRequestJson {

    private String readyDate;
    private AppointmentJson loadingAppointment;
    private AppointmentJson unloadingAppointment;

    public String getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(String readyDate) {
        this.readyDate = readyDate;
    }

    public AppointmentJson getLoadingAppointment() {
        return loadingAppointment;
    }

    public void setLoadingAppointment(AppointmentJson loadingAppointment) {
        this.loadingAppointment = loadingAppointment;
    }

    public AppointmentJson getUnloadingAppointment() {
        return unloadingAppointment;
    }

    public void setUnloadingAppointment(AppointmentJson unloadingAppointment) {
        this.unloadingAppointment = unloadingAppointment;
    }
}
