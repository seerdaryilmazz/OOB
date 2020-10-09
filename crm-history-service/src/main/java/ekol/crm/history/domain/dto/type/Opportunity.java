package ekol.crm.history.domain.dto.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.event.dto.opportunity.OpportunityJson;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 22.11.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Opportunity {
    private Long id;
    private Long number;
    private String status;
    private String serviceArea;
    private String opportunityOwner;
    private String name;
    private String account;
    private String accountLocation;
    private String ownerSubsidiary;
    private String expectedTurnoverPerYear;
    private String actualTurnoverPerYear;

    public static Opportunity fromJson(OpportunityJson json){
        return new OpportunityBuilder()
                .id(json.getId())
                .number(json.getNumber())
                .status(Optional.ofNullable(json.getStatus()).map(CodeNamePair::getCode).orElse(null))
                .serviceArea(Optional.ofNullable(json.getServiceArea()).map(CodeNamePair::getName).orElse(null))
                .opportunityOwner(json.getOpportunityOwner())
                .name(json.getName())
                .account(Optional.ofNullable(json.getAccount()).map(IdNamePair::getName).orElse(null))
                .accountLocation(Optional.ofNullable(json.getAccountLocation()).map(IdNamePair::getName).orElse(null))
                .ownerSubsidiary(Optional.ofNullable(json.getOwnerSubsidiary()).map(IdNamePair::getName).orElse(null))
                .expectedTurnoverPerYear(Optional.ofNullable(json.getExpectedTurnoverPerYear())
                        .map(price -> price.getAmount() + " " + Optional.ofNullable(price.getCurrency())
                                .map(CodeNamePair::getName).orElse("")).orElse(null))
                .actualTurnoverPerYear(Optional.ofNullable(json.getActualTurnoverPerYear())
                        .map(price -> price.getAmount() + " " + Optional.ofNullable(price.getCurrency())
                                .map(CodeNamePair::getName).orElse("")).orElse(null)).build();
    }
}
