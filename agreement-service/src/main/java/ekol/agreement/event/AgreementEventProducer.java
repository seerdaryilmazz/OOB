package ekol.agreement.event;

import ekol.agreement.domain.dto.EmailMessage;
import ekol.agreement.domain.dto.agreement.AgreementExtensionJson;
import ekol.agreement.domain.dto.agreement.AgreementJson;
import ekol.agreement.domain.model.AgreementExtension;
import ekol.event.annotation.ProducesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AgreementEventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgreementEventProducer.class);


    @ProducesEvent(event = "agreement-create")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.agreement.event.Operation).CREATED")
    public AgreementJson produceCreateAgreement(AgreementEventData event){
        return event.getAgreement().toJson();
    }

    @ProducesEvent(event = "agreement-update")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.agreement.event.Operation).UPDATED")
    public AgreementJson produceUpdateAgreement(AgreementEventData event){
        return event.getAgreement().toJson();
    }

    @ProducesEvent(event = "agreement-delete")
    @TransactionalEventListener(fallbackExecution = true, condition = "#event.operation == T(ekol.agreement.event.Operation).DELETED")
    public AgreementJson produceDeleteAgreement(AgreementEventData event){
        return event.getAgreement().toJson();
    }

    @ProducesEvent(event = "agreement-extension")
    @TransactionalEventListener(fallbackExecution = true)
    public AgreementExtensionJson produceExtensionAgreement(AgreementExtension agreementExtension){
        return agreementExtension.toJson();
    }

    @ProducesEvent(event = "force-location-export")
    @TransactionalEventListener(fallbackExecution = true)
    public ForceLocationExportEventMessageReturned produceForceLocationExportEvent(ForceLocationExportEventMessage message) {
        if(LOGGER.isWarnEnabled()) {
            LOGGER.warn("producing force-location-export event - locationId: {}", message.getLocationId());
        }
        return new ForceLocationExportEventMessageReturned(message.getLocationId());
    }

    @ProducesEvent(event = "email-message-created")
    @TransactionalEventListener(fallbackExecution = true)
    public EmailMessageReturned produceEmailMessageCreated(EmailMessage message) {
        return new EmailMessageReturned(
                message.getFrom(),
                message.getReplyTo(),
                message.getTo(),
                message.getCc(),
                message.getBcc(),
                message.getSubject(),
                message.getBody(),
                message.getHtml(),
                message.getAttachments());
    }

}
