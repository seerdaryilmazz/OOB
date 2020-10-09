package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.account.domain.model.AccountDetail;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDetailJson {

    private String totalLogisticsPotential;
    private String strategicInformation;
    private Boolean fortune500;
    private Boolean galaxy;
    private Boolean global;
    private Long globalAccountId;
    private String globalAccountOwner;

    public AccountDetail toEntity(){
        return AccountDetail.builder()
                .totalLogisticsPotential(getTotalLogisticsPotential())
                .strategicInformation(getStrategicInformation())
                .fortune500(getFortune500())
                .global(getGlobal())
                .globalAccountId(getGlobalAccountId())
                .globalAccountOwner(getGlobalAccountOwner())
                .galaxy(getGalaxy()).build();
    }

    public static AccountDetailJson fromEntity(AccountDetail accountDetail){
        return new AccountDetailJson.AccountDetailJsonBuilder()
                .totalLogisticsPotential(accountDetail.getTotalLogisticsPotential())
                .strategicInformation(accountDetail.getStrategicInformation())
                .fortune500(accountDetail.getFortune500())
                .global(accountDetail.getGlobal())
                .globalAccountId(accountDetail.getGlobalAccountId())
                .globalAccountOwner(accountDetail.getGlobalAccountOwner())
                .galaxy(accountDetail.getGalaxy()).build();
    }
}
