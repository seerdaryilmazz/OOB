package ekol.crm.history.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class CustomsPointJson {

    private CodeNamePair clearanceResponsible;
    private CodeNamePair clearanceType;
    private IdNamePair location;
    private IdNamePair office;

}

