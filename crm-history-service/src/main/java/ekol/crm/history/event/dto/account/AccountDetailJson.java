package ekol.crm.history.event.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class AccountDetailJson {

    private String totalLogisticsPotential;
    private String strategicInformation;
    private Boolean fortune500;
    private Boolean galaxy;
    private Boolean global;

}
