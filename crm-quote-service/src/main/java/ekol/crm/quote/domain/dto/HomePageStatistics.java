package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomePageStatistics {

    private Long totalCount;

    private List<ServiceAreaBasedCount> serviceAreaBasedCounts;

    public HomePageStatistics() {
    }

    public HomePageStatistics(Long totalCount, List<ServiceAreaBasedCount> serviceAreaBasedCounts) {
        this.totalCount = totalCount;
        this.serviceAreaBasedCounts = serviceAreaBasedCounts;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<ServiceAreaBasedCount> getServiceAreaBasedCounts() {
        return serviceAreaBasedCounts;
    }

    public void setServiceAreaBasedCounts(List<ServiceAreaBasedCount> serviceAreaBasedCounts) {
        this.serviceAreaBasedCounts = serviceAreaBasedCounts;
    }
}
