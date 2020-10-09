package ekol.crm.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class PostalAddress {
    private String city;
    private String district;
}
