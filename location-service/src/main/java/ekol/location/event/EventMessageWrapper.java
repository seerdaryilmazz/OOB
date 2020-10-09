package ekol.location.event;

import lombok.*;

@Data
@AllArgsConstructor(staticName = "with")
public class EventMessageWrapper{
	Object data;
	private Operation operation;

	public <T> T getData(Class<T> clazz) {
		return clazz.cast(data);
	}
}
