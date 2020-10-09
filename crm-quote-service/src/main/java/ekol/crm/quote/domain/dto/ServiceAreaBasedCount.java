package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceAreaBasedCount {

    private CodeNamePair serviceArea;

    private Long count;

    public ServiceAreaBasedCount() {
    }

    public ServiceAreaBasedCount(CodeNamePair serviceArea, Long count) {
        this.serviceArea = serviceArea;
        this.count = count;
    }

    public CodeNamePair getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(CodeNamePair serviceArea) {
        this.serviceArea = serviceArea;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
