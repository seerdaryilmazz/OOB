package ekol.crm.quote.domain.dto.locationservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomsDetailsJson{
    private CodeNamePair customsType;
}
