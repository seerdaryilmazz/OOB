package ekol.crm.quote.queue.exportq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomsJson {

    private CustomsPointJson departure;
    private CustomsPointJson arrival;
}
