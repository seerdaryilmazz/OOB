package ekol.crm.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteHomePageStatistics {

    private Long totalCount;

    private List<ServiceAreaBasedCount> serviceAreaBasedCounts;

    public QuoteHomePageStatistics() {
    }

    public QuoteHomePageStatistics(Long totalCount, List<ServiceAreaBasedCount> serviceAreaBasedCounts) {
        this.totalCount = totalCount;
        this.serviceAreaBasedCounts = serviceAreaBasedCounts;
    }
}
