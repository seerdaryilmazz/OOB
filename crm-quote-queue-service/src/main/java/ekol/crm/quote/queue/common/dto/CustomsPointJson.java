package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomsPointJson {

    private CodeNamePair clearanceResponsible;
    private CodeNamePair clearanceType;
    private IdNamePair location;
    private IdNamePair office;
    private IsoNamePair customsLocationCountry;
    private IdNamePair customsLocationPostal;

}

