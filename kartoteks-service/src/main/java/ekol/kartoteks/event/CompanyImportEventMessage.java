package ekol.kartoteks.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.kartoteks.domain.CompanyImportQueue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyImportEventMessage {

    private Long id;

    private Long companyId;

    private String data;

    public static CompanyImportEventMessage createWithImportQueue(CompanyImportQueue companyImportQueue) {
        CompanyImportEventMessage message = new CompanyImportEventMessage();
        message.setId(companyImportQueue.getId());
        message.setCompanyId(companyImportQueue.getCompanyId());
        message.setData(companyImportQueue.getData());
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
