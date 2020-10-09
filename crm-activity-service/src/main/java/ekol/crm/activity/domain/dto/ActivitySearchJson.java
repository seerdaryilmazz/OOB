package ekol.crm.activity.domain.dto;

import javax.validation.constraints.Max;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivitySearchJson {

    private Integer page = 0;
    @Max(100)
    private Integer pageSize = 10;
    private Long accountId;
    private String scopeCode;
    private String toolCode;
    private String statusCode;
    private String minStartDate;
    private String maxStartDate;
    private String minCreationDate;
    private String maxCreationDate;
    private String serviceAreaCode;
    private String username;
    private String activityAttributeKey;
    private String activityAttributeValue;

}
