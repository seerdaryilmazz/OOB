package ekol.crm.account.domain.dto;

import ekol.exceptions.ValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationJson {

    private boolean result;
    private String description;

    public static ValidationJson anInvalidResult(ValidationException ex){
        ValidationJson json = new ValidationJson();
        json.setResult(false);
        json.setDescription(ex.getMessage());
        return json;
    }
    public static ValidationJson aValidResult(){
        ValidationJson json = new ValidationJson();
        json.setResult(true);
        json.setDescription(null);
        return json;
    }
}
