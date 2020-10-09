package ekol.crm.history.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class LoadJson {

    private Long id;
    private CodeNamePair type;
    private String riskFactor;
    private Integer minTemperature;
    private Integer maxTemperature;

}
