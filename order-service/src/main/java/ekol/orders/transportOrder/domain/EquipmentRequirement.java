package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.hibernate5.domain.entity.BaseEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "EquipmentRequirement")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentRequirement extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_equipment_requirement", sequenceName = "seq_equipment_requirement")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_equipment_requirement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportOrderId")
    @JsonBackReference
    private TransportOrder transportOrder;

    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipmentTypeId")
    private EquipmentType equipmentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportOrder getTransportOrder() {
        return transportOrder;
    }

    public void setTransportOrder(TransportOrder transportOrder) {
        this.transportOrder = transportOrder;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }
}
