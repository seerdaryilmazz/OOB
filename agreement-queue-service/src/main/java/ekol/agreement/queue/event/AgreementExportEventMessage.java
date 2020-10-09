package ekol.agreement.queue.event;

import ekol.agreement.queue.wscbfunitprice.wsdl.WSCBFUNITPRICEInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "with")
@NoArgsConstructor
public class AgreementExportEventMessage {
	private String id;
}
