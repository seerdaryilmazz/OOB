package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ozer on 14/02/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskTemplate {

    private String id;
    private String name;
    private long timeLimit;
    private String url;
    private User assignee;
    private Map<String, Object> params = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}

