package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PriceAdaptationModelJson {
    private Long id;
    private String name;
    private Integer eur;
    private Integer usd;
    private Integer inflation;
    private Integer minimumWage;
    private LocalDate validityStartDate;
    private String notes;
}
