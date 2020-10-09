package ekol.crm.inbound.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.inbound.event.dto.QuoteJson;
import ekol.crm.inbound.service.InboundService;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {
	
	private InboundService inboundService;
	
	@PostMapping("/quote-update")
    @ConsumesWebEvent(event = "quote-update", name = "consume quote-update event in crm-inbound")
    public void consumeQuoteSearchIndexEvent(@RequestBody QuoteJson message) {
    	inboundService.relateInbound(message);
    }

}
