package ekol.crm.activity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HomePageStatistics {

    private Long totalCount;

    private List<ActivityScopeBasedCount> activityScopeBasedCounts;

    public HomePageStatistics() {
    }

    public HomePageStatistics(Long totalCount, List<ActivityScopeBasedCount> activityScopeBasedCounts) {
        this.totalCount = totalCount;
        this.activityScopeBasedCounts = activityScopeBasedCounts;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<ActivityScopeBasedCount> getActivityScopeBasedCounts() {
        return activityScopeBasedCounts;
    }

    public void setActivityScopeBasedCounts(List<ActivityScopeBasedCount> activityScopeBasedCounts) {
        this.activityScopeBasedCounts = activityScopeBasedCounts;
    }
}
