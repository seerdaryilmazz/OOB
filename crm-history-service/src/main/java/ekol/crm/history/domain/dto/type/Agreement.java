package ekol.crm.history.domain.dto.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.event.dto.agreement.AgreementJson;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String account;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<String> serviceAreas;
    private Integer renewalLength;
    private String renewalDateType;
    private Integer autoRenewalLength;
    private String autoRenewalDateType;
    private LocalDate autoRenewalDate;

    public static Agreement fromJson(AgreementJson json){
        return new AgreementBuilder()
                .id(json.getId())
                .name(json.getName())
                .type(Optional.ofNullable(json.getType()).map(CodeNamePair::getCode).orElse(null))
                .status(Optional.ofNullable(json.getStatus()).map(CodeNamePair::getCode).orElse(null))
                .account(Optional.ofNullable(json.getAccount()).map(IdNamePair::getName).orElse(null))
                .startDate(Optional.ofNullable(json.getStartDate()).orElse(null))
                .endDate(Optional.ofNullable(json.getEndDate()).orElse(null))
                .serviceAreas(Optional.ofNullable(json.getServiceAreas()).map(Collection::stream).orElseGet(Stream::empty).map(CodeNamePair::getCode).collect(Collectors.toSet()))
                .renewalLength(Optional.ofNullable(json.getRenewalLength()).orElse(null))
                .renewalDateType(Optional.ofNullable(json.getRenewalDateType()).map(CodeNamePair::getCode).orElse(null))
                .autoRenewalLength(Optional.ofNullable(json.getAutoRenewalLength()).orElse(null))
                .autoRenewalDateType(Optional.ofNullable(json.getAutoRenewalDateType()).map(CodeNamePair::getCode).orElse(null))
                .autoRenewalDate(json.getAutoRenewalDate()).build();
    }
}
