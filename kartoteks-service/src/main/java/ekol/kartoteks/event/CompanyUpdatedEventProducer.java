package ekol.kartoteks.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.event.annotation.ProducesEvent;
import ekol.kartoteks.domain.Company;

/**
 * Created by kilimci on 27/11/2017.
 */
@Component
public class CompanyUpdatedEventProducer {

    private Logger logger = LoggerFactory.getLogger(CompanyUpdatedEventProducer.class);

    @ProducesEvent(event = "company-updated")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.entity instanceof T(ekol.kartoteks.domain.Company)")
    public CompanyDataEventMessage produce(CompanyEvent event) {
    	Company company = (Company)event.getEntity();
        logger.info("producing company-updated event, companyId: {}", company.getId());
        return CompanyDataEventMessage.createWith(company, Operation.AFTER_COMPANY_UPDATED);
    }
}

