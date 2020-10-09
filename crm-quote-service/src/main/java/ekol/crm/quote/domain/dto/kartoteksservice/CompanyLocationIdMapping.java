package ekol.crm.quote.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyLocationIdMapping {
    private Long id;
    private String application;
    private String applicationLocationId;
}
