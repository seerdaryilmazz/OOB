package ekol.orders.transportOrder.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 16/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShipmentResponsibleEventMessage {

    private Long orderId;
    private Long shipmentId;
    private List<ShipmentSegment> shipmentSegments = new ArrayList<>();

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public List<ShipmentSegment> getShipmentSegments() {
        return shipmentSegments;
    }

    public void setShipmentSegments(List<ShipmentSegment> shipmentSegments) {
        this.shipmentSegments = shipmentSegments;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShipmentSegment {

        private Long id;
        private Integer index;
        private IdNamePair fromLocation;
        private IdNamePair toLocation;
        private IdNamePair responsible;
        private boolean planned;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public IdNamePair getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(IdNamePair fromLocation) {
            this.fromLocation = fromLocation;
        }

        public IdNamePair getToLocation() {
            return toLocation;
        }

        public void setToLocation(IdNamePair toLocation) {
            this.toLocation = toLocation;
        }

        public IdNamePair getResponsible() {
            return responsible;
        }

        public void setResponsible(IdNamePair responsible) {
            this.responsible = responsible;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public boolean isPlanned() {
            return planned;
        }

        public void setPlanned(boolean planned) {
            this.planned = planned;
        }
    }
}
