package ekol.orders.order.domain;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.orders.transportOrder.domain.OrderPlanningOperationType;
import ekol.orders.transportOrder.domain.VehicleFeature;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Where(clause = "deleted = 0")
@Table(name = "ORDER_SHIPMENT_VEHICLE_REQ")
public class OrderShipmentVehicleRequirement extends BaseEntity{

    @Id
    @SequenceGenerator(name = "seq_order_shipment_vehicle_req", sequenceName = "seq_order_shipment_vehicle_req")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_vehicle_req")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;

    @Enumerated(EnumType.STRING)
    private VehicleFeature requirement;

    @Enumerated(EnumType.STRING)
    private OrderPlanningOperationType operationType;

    @Enumerated(EnumType.STRING)
    private VehicleRequirementReason requirementReason;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderShipment getShipment() {
        return shipment;
    }

    public void setShipment(OrderShipment shipment) {
        this.shipment = shipment;
    }

    public VehicleFeature getRequirement() {
        return requirement;
    }

    public void setRequirement(VehicleFeature requirement) {
        this.requirement = requirement;
    }

    public OrderPlanningOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OrderPlanningOperationType operationType) {
        this.operationType = operationType;
    }

    public VehicleRequirementReason getRequirementReason() {
        return requirementReason;
    }

    public void setRequirementReason(VehicleRequirementReason requirementReason) {
        this.requirementReason = requirementReason;
    }

    public boolean isCurtainSideRequirement(){
        return getRequirement() != null && getRequirement().equals(VehicleFeature.CURTAIN_SIDER);
    }
    public boolean isHangingLoadRequirement(){
        return getRequirement() != null && getRequirement().equals(VehicleFeature.SUITABLE_FOR_HANGING_LOADS);
    }
    public boolean isRequiredByLoad(){
        return getRequirementReason() != null && getRequirementReason().equals(VehicleRequirementReason.BY_LOAD);
    }
}
