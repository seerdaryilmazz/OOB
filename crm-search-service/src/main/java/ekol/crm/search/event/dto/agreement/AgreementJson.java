package ekol.crm.search.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class AgreementJson {
    private Long id;
    private Long number;
    private String name;
    private CodeNamePair type;
    private CodeNamePair status;
    private IdNamePair account;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CodeNamePair> serviceAreas;
    private Integer renewalLength;
    private CodeNamePair renewalDateType;
    private Integer autoRenewalLength;
    private CodeNamePair autoRenewalDateType;
    private LocalDate autoRenewalDate;
    private String lastUpdatedBy;
    private String discriminator;
}
