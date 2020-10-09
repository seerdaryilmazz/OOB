package ekol.email.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "with")

public class EmailEvent {
	
	private Object data;
	
	public <T> T getData(Class<T> type){
		return type.cast(getData());
	}

}
