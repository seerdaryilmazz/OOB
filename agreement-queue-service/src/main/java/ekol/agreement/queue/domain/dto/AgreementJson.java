package ekol.agreement.queue.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AgreementJson {
    private Long id;
    private Long number;
    private String name;
    private List<CodeNamePair> serviceAreas = new ArrayList<>();
    private CodeNamePair status;
    private IdNamePair account;
    private List<UnitPriceJson> unitPrices = new ArrayList<>();
}
