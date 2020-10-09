package ekol.orders.search.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="of")
public class ShipmentPartyResult {
	List<HandlingPartySearchDocument> senders;
	List<HandlingPartySearchDocument> consignees;
}
