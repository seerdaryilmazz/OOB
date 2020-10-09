package ekol.crm.history.domain.dto.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.event.dto.account.AccountDetailJson;
import ekol.crm.history.event.dto.account.AccountJson;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.*;

import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;
    private String name;
    private String company;
    private String country;
    private String accountOwner;
    private String accountType;
    private String segment;
    private String parentSector;
    private String subSector;
    private String totalLogisticsPotential;
    private String strategicInformation;
    private Boolean fortune500;
    private Boolean galaxy;
    private Boolean global;

    public static Account fromJson(AccountJson json){
        return new AccountBuilder()
                .id(json.getId())
                .name(json.getName())
                .company(Optional.ofNullable(json.getCompany()).map(IdNamePair::getName).orElse(null))
                .country(Optional.ofNullable(json.getCountry()).map(IsoNamePair::getName).orElse(null))
                .accountOwner(json.getAccountOwner())
                .accountType(Optional.ofNullable(json.getAccountType()).map(CodeNamePair::getName).orElse(null))
                .segment(Optional.ofNullable(json.getSegment()).map(CodeNamePair::getName).orElse(null))
                .parentSector(Optional.ofNullable(json.getParentSector()).map(CodeNamePair::getName).orElse(null))
                .subSector(Optional.ofNullable(json.getSubSector()).map(CodeNamePair::getName).orElse(null))
                .totalLogisticsPotential(Optional.ofNullable(json.getDetails()).map(AccountDetailJson::getTotalLogisticsPotential).orElse(null))
                .strategicInformation(Optional.ofNullable(json.getDetails()).map(AccountDetailJson::getStrategicInformation).orElse(null))
                .fortune500(Optional.ofNullable(json.getDetails()).map(AccountDetailJson::getFortune500).orElse(null))
                .galaxy(Optional.ofNullable(json.getDetails()).map(AccountDetailJson::getGalaxy).orElse(null))
                .global(Optional.ofNullable(json.getDetails()).map(AccountDetailJson::getGlobal).orElse(null)).build();
    }
}
