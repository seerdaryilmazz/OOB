package ekol.crm.account.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CompanyUpdatedEventMessage {

    private Long id;
    private String name;
}
