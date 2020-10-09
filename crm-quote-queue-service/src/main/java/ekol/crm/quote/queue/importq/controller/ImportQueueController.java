package ekol.crm.quote.queue.importq.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.queue.importq.domain.ImportQuoteQueueItem;
import ekol.crm.quote.queue.importq.dto.ImportQueueSearchJson;
import ekol.crm.quote.queue.importq.service.ImportQueueService;
import ekol.event.auth.Authorize;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/import-queue")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ImportQueueController {
	
	private ImportQueueService importQueueService;

	@GetMapping(value = "/{id}")
	public ImportQuoteQueueItem getById(@PathVariable String id) {
		return importQueueService.findById(id);
	}

	@Authorize(operations="shipment.import.execute")
	@PutMapping(value="/requeue/{id}")
	public void requeue(@PathVariable String id) {
		importQueueService.requeue(id);
	}
	
	@GetMapping(value="/search")
	public Page<ImportQuoteQueueItem> search(@Valid ImportQueueSearchJson json) {
		return importQueueService.search(json);
	}
}
