package ekol.crm.quote.queue.exportq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;
import lombok.*;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LostReasonJson {

    private IdNamePair competitor;
    private BigDecimal competitorPrice;
    private String competitorPriceCurrency;
    private String reason;
    private String reasonDetail;
}
