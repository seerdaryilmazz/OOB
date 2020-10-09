package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class LetterOfGuarenteeJson {
    private Long id;
    private String scope;
    private BigDecimal contractAmount;
    private String currency;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
}
