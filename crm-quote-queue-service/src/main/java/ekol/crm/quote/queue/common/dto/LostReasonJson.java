package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
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

    private Long id;
    private IdNamePair competitor;
    private BigDecimal competitorPrice;
    private String competitorPriceCurrency;
    private CodeNamePair reason;
    private String reasonDetail;
}
