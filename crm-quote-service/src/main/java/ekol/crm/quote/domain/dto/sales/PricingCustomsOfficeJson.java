package ekol.crm.quote.domain.dto.sales;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PricingCustomsOfficeJson {

    private Long id;
    private IdNamePair customsOffice;

}
