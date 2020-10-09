package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private List<KpiInfoJson> kpiInfos;
    private List<OwnerInfoJson> ownerInfos;
    private List<InsuranceInfoJson> insuranceInfos;
    private List<UnitPriceJson> unitPrices;
    private List<PriceAdaptationModelJson> priceAdaptationModels;
    private List<SignatureInfoJson> signatureInfos;
    private FinancialInfoJson financialInfo;
    private List<LetterOfGuarenteeJson> letterOfGuarentees;
    private LegalInfoJson legalInfo;
    private String discriminator;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
}
