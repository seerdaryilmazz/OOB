package ekol.crm.quote.domain.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.enumaration.ServiceTypeCategory;
import lombok.*;

@Data
@Entity
@Table(name = "CrmServiceType")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@NotEmpty
	private String code;
	
	@NotEmpty
	private String name;
	
	@Enumerated(EnumType.STRING)
	private ServiceTypeCategory category;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "billingItemId")
	private BillingItem billingItem;
	
	private String serviceArea;
	
	@PrePersist
	@PreUpdate
	private void generateId() {
		if(Objects.isNull(getId())) {
			setId(getCode());
		}
	}
}
