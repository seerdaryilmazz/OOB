package ekol.crm.quote.queue.importq.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.queue.importq.dto.ImportQuoteOrderJson;
import ekol.crm.quote.queue.importq.service.*;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ImportEventConsumerController {
	
	private ImportQuoteService importQuoteService;
	private ImportQuoteOrderService importQuoteOrderService;

    @PostMapping(value = "/quote-import")
    @ConsumesWebEvent(event = "quote-import", name = "import quote referenceNo of external system")
    public void consumeReturnShipmentImport(@RequestBody ImportQuoteEventMessage message){
    	importQuoteService.updateWithQuoteInfo(message.getId());
    }
    
    @PostMapping(value = "/quote-order-import")
    @ConsumesWebEvent(event = "quote-order-import", name = "import order number related with quote")
    public void consumeReturnShipmentImport(@RequestBody ImportQuoteOrderJson message){
    	importQuoteOrderService.updateOrderMapping(message);
    }
}
