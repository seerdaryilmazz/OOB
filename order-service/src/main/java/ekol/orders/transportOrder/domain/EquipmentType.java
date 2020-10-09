package ekol.orders.transportOrder.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;

@Entity
@Table(name = "EquipmentType")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentType extends LookupEntity {

    @Id
    @SequenceGenerator(name = "seq_equipment_type", sequenceName = "seq_equipment_type")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_equipment_type")
    private Long id;


    public static EquipmentType with(Long id, String name){
        EquipmentType type = new EquipmentType();
        type.setId(id);
        type.setName(name);
        return type;
    }
    public static EquipmentType with(IdNamePair idName){
        return EquipmentType.with(idName.getId(), idName.getName());
    }

    public static EquipmentType with(Long id, String code, String name){
    	EquipmentType type = new EquipmentType();
    	type.setId(id);
    	type.setCode(code);
    	type.setName(name);
    	return type;
    }
    public static EquipmentType with(IdCodeNameTrio idCodeName){
    	return EquipmentType.with(idCodeName.getId(), idCodeName.getCode(), idCodeName.getName());
    }

    public IdNamePair toIdNamePair(){
        return IdNamePair.with(getId(), getName());
    }

    public IdCodeNameTrio toIdCodeNameTrio(){
    	return IdCodeNameTrio.with(getId(), getCode(), getName());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
