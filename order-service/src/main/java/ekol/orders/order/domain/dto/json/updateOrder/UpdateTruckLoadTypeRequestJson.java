package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.lookup.domain.TruckLoadType;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTruckLoadTypeRequestJson {

    private TruckLoadType truckLoadType;
    private List<ShipmentUnloadingAppointment> unloadingAppointments = new ArrayList<>();

    public TruckLoadType getTruckLoadType() {
        return truckLoadType;
    }

    public void setTruckLoadType(TruckLoadType truckLoadType) {
        this.truckLoadType = truckLoadType;
    }

    public void setUnloadingAppointments(List<ShipmentUnloadingAppointment> unloadingAppointments) {
        this.unloadingAppointments = unloadingAppointments;
    }

    public List<ShipmentUnloadingAppointment> getUnloadingAppointments() {
        return unloadingAppointments;
    }
}
