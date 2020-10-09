package ekol.crm.quote.domain.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CrmBillingItem")
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(name = "SEQ_BILLINGITEM", sequenceName = "SEQ_BILLINGITEM")
@EqualsAndHashCode
public class BillingItem implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BILLINGITEM")
    private Long id;

    private String name;

    private String description;

    private String code;

    private String serviceArea;
}
