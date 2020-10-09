package ekol.crm.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Dogukan Sahinturk on 19.08.2020
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountSearchJson {
    private Integer page = 0;
    private Integer pageSize = 10;
    private List<String> accountOwners;
    private String countryIso;
    private String createdBy;
    private String name;
    private Long companyId;
    private String accountTypeCode;
    private String segmentCode;
    private String parentSectorCode;
    private String subSectorCode;

}
