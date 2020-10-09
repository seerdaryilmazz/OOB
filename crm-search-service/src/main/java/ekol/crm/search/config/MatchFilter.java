package ekol.crm.search.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchFilter {

    private String name;
    private String val;
    private boolean not; 
    private String operator = "eq";
}
