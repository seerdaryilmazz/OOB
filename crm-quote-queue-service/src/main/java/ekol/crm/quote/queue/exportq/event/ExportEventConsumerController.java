package ekol.crm.quote.queue.exportq.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.queue.common.dto.QuoteJson;
import ekol.crm.quote.queue.exportq.service.ExportQueueService;
import ekol.crm.quote.queue.exportq.service.ExportQuoteService;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ExportEventConsumerController {
	
	private ExportQueueService quoteQueueService;
	private ExportQuoteService quoteExportService;


    @PostMapping("/quote-update")
    @ConsumesWebEvent(event = "quote-update", name = "quote add to queue to export external system ")
    public void consumeQuoteWon(@RequestBody QuoteJson quoteJsonMessage){
        quoteQueueService.addQueueItem(quoteJsonMessage);
    }

    @PostMapping("/quote-export")
    @ConsumesWebEvent(event = "quote-export", name = "quote export external system")
    public void consumeOrderExport(@RequestBody QuoteExportEventMessage message){
        quoteExportService.export(message.getId());
    }

}
