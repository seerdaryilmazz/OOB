package ekol.orders.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.Data;

@Data
@Embeddable
public class ShipmentParty {

	@Embedded
	private IdNameEmbeddable company;

	@Embedded
	private IdNameEmbeddable companyLocation;

	@Embedded
	private IdNameEmbeddable companyContact;
}
