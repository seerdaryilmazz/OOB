package ekol.agreement.domain.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ekol.agreement.domain.enumaration.AgreementStatus;
import ekol.agreement.domain.enumaration.AgreementType;
import ekol.agreement.domain.enumaration.RenewalDateType;
import ekol.agreement.domain.enumaration.ResponsibilityType;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "discriminator",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AgreementLogisticJson.class, name = "LOGISTIC")
})
@Data
@NoArgsConstructor
public abstract class AgreementJson {
    private Long id;
    private Long number;
    @NotNull(message = "Agreement name can not be null")
    private String name;
    private UtcDateTime createdAt;
    private String createdBy;
    @NotNull(message = "Agreement Type can not be null")
    private AgreementType type;
    private AgreementStatus status;
    @NotNull(message = "Agreement account can not be null")
    private IdNamePair account;
    @NotNull(message = "Agreement start date can not be null")
    private LocalDate startDate;
    @NotNull(message = "Agreement end date can not be null")
    private LocalDate endDate;
    @NotNull(message = "Agreement service areas can not be empty")
    private List<CodeNamePair> serviceAreas;
    private Integer renewalLength;
    private RenewalDateType renewalDateType;
    private Integer autoRenewalLength;
    private RenewalDateType autoRenewalDateType;
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
    private List<NoteJson> notes = new ArrayList<>();
    private List<DocumentJson> documents = new ArrayList<>();
    private String lastUpdatedBy;
    private String discriminator;

    public abstract Agreement toEntity();
}
