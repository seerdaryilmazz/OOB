package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoadJson {

    private Long id;
    private CodeNamePair type;
    private String riskFactor;
    private Integer minTemperature;
    private Integer maxTemperature;
}
