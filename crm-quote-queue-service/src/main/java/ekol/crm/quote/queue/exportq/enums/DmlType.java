package ekol.crm.quote.queue.exportq.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public enum DmlType {
	INSERT("I"), 
	UPDATE("U"), 
	DELETE("D")
	;
	
	private String code;
}
