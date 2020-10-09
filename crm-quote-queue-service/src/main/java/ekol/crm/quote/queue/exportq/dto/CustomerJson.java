package ekol.crm.quote.queue.exportq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="with")
public class CustomerJson {
	private Long id;
	private Long locationid;
}
