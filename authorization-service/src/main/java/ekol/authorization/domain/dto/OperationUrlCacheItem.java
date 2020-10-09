package ekol.authorization.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationUrlCacheItem {

    private String url;
    private String method;
    private String operation;

    public OperationUrlCacheItem() {
    }

    public OperationUrlCacheItem(String url, String method, String operation) {
        this.url = url;
        this.method = method;
        this.operation = operation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
