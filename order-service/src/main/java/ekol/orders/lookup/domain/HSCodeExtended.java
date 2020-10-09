package ekol.orders.lookup.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.LookupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "hscodeExtended")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper=true)
public class HSCodeExtended extends LookupEntity {

	/**
	* 
	*/
	private static final long serialVersionUID = 6665823737414920292L;
	
	@Id
	@SequenceGenerator(name = "seq_hscode_extended", sequenceName = "seq_hscode_extended")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hscode_extended")
	private Long id;
}
