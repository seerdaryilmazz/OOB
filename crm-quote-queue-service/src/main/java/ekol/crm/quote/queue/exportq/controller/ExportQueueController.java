package ekol.crm.quote.queue.exportq.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.queue.common.dto.*;
import ekol.crm.quote.queue.exportq.domain.ExportQuoteQueueItem;
import ekol.crm.quote.queue.exportq.service.ExportQueueService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/export-queue")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ExportQueueController {

	private ExportQueueService quoteQueueService;

	@GetMapping("/{id}")
	public ExportQuoteQueueItem getById(@PathVariable String id) {
		return quoteQueueService.findById(id);
	}

	@GetMapping
	public List<ExportQuoteQueueItem> get(QuoteExportQueryJson param) {
		if (param.getQuoteId() != null) {
			return quoteQueueService.findByQuoteId(param.getQuoteId());
		} else if (param.getQuoteNumber() != null) {
			return quoteQueueService.findByQuoteNumber(param.getQuoteNumber());
		} else {
			return quoteQueueService.findAll();
		}
	}
	
	@PutMapping("/requeue/{id}")
	public void requeue(@PathVariable String id) {
		quoteQueueService.requeue(id);
	}
	
	@GetMapping("/requeue")
	public void requeue(@RequestParam(required = false, defaultValue = "7") Long daysBefore) {
		quoteQueueService.requeue(daysBefore);
	}
	
	@GetMapping("/search")
	public Page<ExportQuoteQueueItem> search(@Valid QuoteExportSearchJson json) {
		return quoteQueueService.search(json);
	}
	
	@PostMapping
	public void addQueueItems(@RequestBody List<Long> quoteNumber) {
		quoteNumber.stream().forEach(quoteQueueService::addQueueItemByNumber);
	}
}
