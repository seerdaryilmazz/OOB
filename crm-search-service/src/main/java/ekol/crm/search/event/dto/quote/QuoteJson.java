package ekol.crm.search.event.dto.quote;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class QuoteJson {
    private Long id;
    private Long number;
    private String name;
    private IdNamePair account;
    private IdNamePair accountLocation;
    private CodeNamePair serviceArea;
    private CodeNamePair type;
    private CodeNamePair status;
    private String createdBy;
    private UtcDateTime createdAt;
    private String lastUpdatedBy;
    private String accountOwner;
    private UtcDateTime lastUpdated;
    private Long potentialId;
    private Map<String, String> mappedIds;
    private List<ProductJson> products;
    private BigDecimal payWeight;
    private Map<String, String> quoteAttribute;

}


