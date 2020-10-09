package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Created by Dogukan Sahinturk on 4.12.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class QuoteJson {
    private Long id;
    private Long number;
    private String name;
    private CodeNamePair serviceArea;
    private CodeNamePair type;
    private CodeNamePair status;
    private Map<String, String> quoteAttribute = new HashMap<>();
    private Long opportunityId;
    private List<QuoteProductJson> products;
    private List<PriceJson> prices;
    private QuoteJson previousData;
}
