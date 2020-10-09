package ekol.orders.order.domain.dto.json.updateOrder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.lookup.domain.ServiceType;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateServiceTypeRequestJson {

    private ServiceType serviceType;
    private List<ShipmentUnloadingAppointment> unloadingAppointments = new ArrayList<>();

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public List<ShipmentUnloadingAppointment> getUnloadingAppointments() {
        return unloadingAppointments;
    }

    public void setUnloadingAppointments(List<ShipmentUnloadingAppointment> unloadingAppointments) {
        this.unloadingAppointments = unloadingAppointments;
    }
}

