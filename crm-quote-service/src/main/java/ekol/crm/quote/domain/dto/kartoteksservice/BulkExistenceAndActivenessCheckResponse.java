package ekol.crm.quote.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkExistenceAndActivenessCheckResponse {
    
    private Set<Long> ok;
    private Set<Long> notOk;

    public BulkExistenceAndActivenessCheckResponse() {
    }

    public BulkExistenceAndActivenessCheckResponse(Set<Long> ok, Set<Long> notOk) {
        this.ok = ok;
        this.notOk = notOk;
    }

    public Set<Long> getOk() {
        return ok;
    }

    public void setOk(Set<Long> ok) {
        this.ok = ok;
    }

    public Set<Long> getNotOk() {
        return notOk;
    }

    public void setNotOk(Set<Long> notOk) {
        this.notOk = notOk;
    }
}
