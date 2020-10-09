package ekol.crm.quote.queue.importq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.queue.importq.dto.*;
import ekol.crm.quote.queue.importq.service.*;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor(onConstructor=@__(@Autowired))
@RequestMapping("/import")
public class ImportController {

	private ImportQueueService importQueueService;
	private ImportQuoteOrderService importQuoteOrderService;
	
	@PostMapping({"/quote","/quote/"})
	public String importQuote(@RequestBody ImportQuoteJson importJson) {
		return importQueueService.addQueueItem(importJson);
	}

	@GetMapping({"/quote/order", "/quote/order/"})
	public String importQuoteOrder(ImportQuoteOrderJson importJson) {
		return importQuoteOrderService.importQuoteOrderData(importJson);
	}
}