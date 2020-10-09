package ekol.crm.account.domain.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

/**
 * Created by Dogukan Sahinturk on 25.12.2019
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyCrInfo {
    private String defaultAccountable;
    private String displayName;
    private String username;
    private String email;
    private String phone;
    private Set<String> workArea;
    private Set<String> serviceGroupCode;
    private String status;
    private String quadroCompanyCode;
    private String location;
}
