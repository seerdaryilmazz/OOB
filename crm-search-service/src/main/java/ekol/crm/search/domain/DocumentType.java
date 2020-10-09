package ekol.crm.search.domain;

import ekol.crm.search.domain.account.*;
import ekol.crm.search.domain.agreement.AgreementSearchDoc;
import ekol.crm.search.domain.opportunity.OpportunitySearchDoc;
import ekol.crm.search.domain.quote.QuoteSearchDoc;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DocumentType {
	account(AccountSearchDoc.class),
	quote(QuoteSearchDoc.class),
	contact(ContactSearchDoc.class),
	agreement(AgreementSearchDoc.class),
	opportunity(OpportunitySearchDoc.class)
	;

	private Class<? extends SearchDoc> searchDoc;
}
