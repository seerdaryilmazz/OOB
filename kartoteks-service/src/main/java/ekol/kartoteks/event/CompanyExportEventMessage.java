package ekol.kartoteks.event;

import ekol.kartoteks.domain.CompanyExportQueue;

/**
 * Created by kilimci on 07/12/2017.
 */
public class CompanyExportEventMessage {

    private Long id;

    public static CompanyExportEventMessage createWithExportQueue(CompanyExportQueue companyExportQueue){
        CompanyExportEventMessage message = new CompanyExportEventMessage();
        message.setId(companyExportQueue.getId());
        return message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
