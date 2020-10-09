package ekol.crm.quote.domain.model.businessVolume;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ekol.crm.quote.domain.model.AuditedBaseEntity;
import ekol.crm.quote.domain.model.quote.LongTermQuote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CrmBusinessVolume")
@SequenceGenerator(name = "SEQ_CRMBUSINESSVOLUME", sequenceName = "SEQ_CRMBUSINESSVOLUME")
@Getter
@Setter
@NoArgsConstructor
@Audited

public class BusinessVolume extends AuditedBaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMBUSINESSVOLUME")
	private Long id;
	
	private String inbound;
	
	private String storage;
	
	private String outbound;
	
	private String vas;
	
	private Boolean spot;
	
}
