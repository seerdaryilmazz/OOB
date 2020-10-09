package ekol.crm.quote.domain.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.model.CodeNamePair;
import lombok.*;

@Entity
@Table(name = "CrmQuoteOrderMapping")
@SequenceGenerator(name = "SEQ_CRMQUOTEORDERMAPPING", sequenceName = "SEQ_CRMQUOTEORDERMAPPING")
@Where(clause = "deleted = 0")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuoteOrderMapping extends BaseEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMQUOTEORDERMAPPING")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "quote_id")
	private Quote quote;
	
	private String orderNumber;
	
	@Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="code", column=@Column(name="ORDER_STATUS_CODE")),
            @AttributeOverride(name = "name", column=@Column(name="ORDER_STATUS_NAME"))})
	private CodeNamePair orderStatus;
	
	private String orderRelation;
}
