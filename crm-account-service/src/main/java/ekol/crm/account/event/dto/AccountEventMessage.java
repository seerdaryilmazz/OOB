package ekol.crm.account.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="with")
public class AccountEventMessage {
    private Object data;
    private Operation operation;
    
    public <T> T getData(Class<T> type){
		return type.cast(getData());
	}
}
