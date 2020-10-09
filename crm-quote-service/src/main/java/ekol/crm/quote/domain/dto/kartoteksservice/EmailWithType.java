package ekol.crm.quote.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdCodeName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailWithType {

    private Email email;
    private IdCodeName usageType;
    private boolean isDefault;
}
