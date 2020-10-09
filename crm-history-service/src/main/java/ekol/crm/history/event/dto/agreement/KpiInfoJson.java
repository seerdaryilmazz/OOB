package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class KpiInfoJson {
    private Long id;
    private String name;
    private String target;
    private String actual;
    private LocalDate lastUpdateDate;
    private Integer updatePeriod;
    private CodeNamePair renewalDateType;
    private LocalDate nextUpdateDate;
}
