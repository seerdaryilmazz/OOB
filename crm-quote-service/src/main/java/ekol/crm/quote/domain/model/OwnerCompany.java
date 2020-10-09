package ekol.crm.quote.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import ekol.hibernate5.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="CrmShipownerAirline")
@SequenceGenerator(name="SEQ_CRMOWNERCOMPANY", sequenceName="SEQ_CRMOWNERCOMPANY")
@Where(clause="deleted = 0")
@Getter
@Setter
public class OwnerCompany extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator= "SEQ_CRMOWNERCOMPANY")
	private Long id;
	
	@Column
	private String code;
	
	@Column
	private String name;
	
	@Column
	private String type;



}
