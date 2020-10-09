package ekol.orders.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import ekol.hibernate5.domain.entity.LookupEntity;
import ekol.orders.order.domain.dto.json.OrderShipmentDefinitionOfGoodsJson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@Entity
@Where(clause = "deleted = 0")
@Table(name = "order_shipment_def_of_goods")
public class OrderShipmentDefinitionOfGoods extends LookupEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5497700546146431037L;

	@Id
    @SequenceGenerator(name = "seq_order_shipment_def_of_good", sequenceName = "seq_order_shipment_def_of_good")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_order_shipment_def_of_good")
    private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId")
    private OrderShipment shipment;
	
	@Column
	private Long hscodeId;
	
	public static OrderShipmentDefinitionOfGoods with(Long id, String code, String name, Long hscodeId){
		OrderShipmentDefinitionOfGoods instance = new OrderShipmentDefinitionOfGoods();
		instance.setId(id);
		instance.setCode(code);
		instance.setName(name);
		instance.setHscodeId(hscodeId);
    	return instance;
    }
    public static OrderShipmentDefinitionOfGoods with(OrderShipmentDefinitionOfGoodsJson json){
    	return OrderShipmentDefinitionOfGoods.with(json.getId(), json.getCode(), json.getName(), json.getHscodeId());
    }
}