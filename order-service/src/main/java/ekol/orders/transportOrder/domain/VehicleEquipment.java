package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private VehicleType vehicleType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }


}
