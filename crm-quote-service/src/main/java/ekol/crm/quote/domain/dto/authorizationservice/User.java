package ekol.crm.quote.domain.dto.authorizationservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User {

    private Long id;

    private String username;

    private String displayName;

    private String firstLevel;

    private String secondLevel;

    private String thirdLevel;

    private boolean deleted;

    public ekol.crm.quote.domain.model.User toEntity() {
        return ekol.crm.quote.domain.model.User.builder()
                .id(getId())
                .name(getUsername())
                .displayName(getDisplayName())
                .firstLevel(getFirstLevel())
                .secondLevel(getSecondLevel())
                .thirdLevel(getThirdLevel())
                .build();
    }
}
