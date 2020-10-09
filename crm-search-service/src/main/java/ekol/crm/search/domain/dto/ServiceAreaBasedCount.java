package ekol.crm.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceAreaBasedCount {

    private String serviceArea;

    private Long count;

    public ServiceAreaBasedCount() {
    }

    public ServiceAreaBasedCount(String serviceArea, Long count) {
        this.serviceArea = serviceArea;
        this.count = count;
    }
}
