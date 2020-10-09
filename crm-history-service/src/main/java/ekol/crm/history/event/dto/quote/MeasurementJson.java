package ekol.crm.history.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class MeasurementJson {

    private BigDecimal weight;
    private BigDecimal ldm;
    private BigDecimal volume;

}
