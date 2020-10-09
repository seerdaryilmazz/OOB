package ekol.crm.activity.event.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(staticName = "with")
public class ActivityEvent {
	private Object data;
	private Operation operation;
	
	public <T> T getData(Class<T> type){
		return type.cast(getData());
	}
}
