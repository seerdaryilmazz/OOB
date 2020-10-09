package ekol.agreement.queue.event;

import ekol.agreement.queue.domain.dto.AgreementJson;
import ekol.agreement.queue.service.AgreementQueueService;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {

    private AgreementQueueService agreementQueueService;

    @PostMapping("/agreement-update")
    @ConsumesWebEvent(event = "agreement-update", name = "send agreement data to rainbow")
    public void consumedAgreementUpdated(@RequestBody AgreementJson message){
        if(message.getServiceAreas().stream().anyMatch(serviceArea -> "WHM".equals(serviceArea.getCode()))){
            Optional.ofNullable(message.getUnitPrices()).ifPresent(i->
                    message.setUnitPrices(i.stream().filter(unitPriceJson -> "WHM".equals(unitPriceJson.getBillingItem().getServiceArea())).collect(Collectors.toList())));
            if(!CollectionUtils.isEmpty(agreementQueueService.findByAgreementIdAndSuccessfulStatus(message.getId()))){
                agreementQueueService.addQueueItem(message);
            }else {
                if(!CollectionUtils.isEmpty(message.getUnitPrices()) && Objects.equals( "APPROVED", message.getStatus().getCode())){
                    agreementQueueService.addQueueItem(message);
                }
            }
        }
    }

    @PostMapping("/agreement-export")
    @ConsumesWebEvent(event = "agreement-export", name = "agreement export external system")
    public void consumeAgreementExport(@RequestBody AgreementExportEventMessage message){
        agreementQueueService.export(message.getId());
    }
}
