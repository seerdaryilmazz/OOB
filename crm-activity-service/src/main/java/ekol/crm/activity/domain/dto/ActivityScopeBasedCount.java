package ekol.crm.activity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.activity.domain.ActivityScope;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityScopeBasedCount {

    private ActivityScope activityScope;

    private Long count;

    public ActivityScopeBasedCount() {
    }

    public ActivityScopeBasedCount(ActivityScope activityScope, Long count) {
        this.activityScope = activityScope;
        this.count = count;
    }

    public ActivityScope getActivityScope() {
        return activityScope;
    }

    public void setActivityScope(ActivityScope activityScope) {
        this.activityScope = activityScope;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
