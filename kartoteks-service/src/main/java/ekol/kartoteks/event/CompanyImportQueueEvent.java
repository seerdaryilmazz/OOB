package ekol.kartoteks.event;

import ekol.kartoteks.domain.CompanyImportQueue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "with")
public class CompanyImportQueueEvent {
	private CompanyImportQueue data;
	private Operation operation;
	
	public enum Operation{
		IMPORT,
		IMPORT_COMPLETED,
		;
	}
}
