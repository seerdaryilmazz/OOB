package ekol.crm.quote.queue.importq.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.crm.quote.queue.importq.domain.ImportQuoteQueueItem;
import ekol.crm.quote.queue.importq.dto.ImportQueueSearchJson;
import ekol.crm.quote.queue.importq.dto.ImportQuoteJson;
import ekol.crm.quote.queue.importq.enums.Status;
import ekol.crm.quote.queue.importq.repository.QuoteImportQueueRepository;
import ekol.crm.quote.queue.importq.validation.ImportValidation;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ImportQueueService {
	
	private MongoTemplate mongoTemplate;
	private QuoteImportQueueRepository quoteImportQueueRepository;
	private ApplicationEventPublisher publisher;
	private ImportValidation importValidation;
	
	@Transactional
	public String addQueueItem(ImportQuoteJson importJson) {
		importValidation.validate(importJson);
		ImportQuoteQueueItem item = quoteImportQueueRepository.findByQuoteNumberAndExternalSystemName(importJson.getQuoteCode(), importJson.getExternalSystemName().toUpperCase());
		if(Objects.nonNull(item)){
			if(Status.FAILED == item.getStatus()) {
				requeue(item);
			}
			return item.getId();
		}
		item = quoteImportQueueRepository.save(ImportQuoteQueueItem	.builder()
													.status(Status.PENDING)
													.quoteNumber(importJson.getQuoteCode())
													.externalSystemName(importJson.getExternalSystemName().toUpperCase())
													.data(importJson).build());
		publisher.publishEvent(item);
		return item.getId();
	}
	
	@Transactional
	public void requeue(String id) {
		ImportQuoteQueueItem item = findById(id);
		requeue(item);
	}
	
	private void requeue(ImportQuoteQueueItem item) {
		item.setRetryCount(0);
		item.setStatus(Status.REQUEUED);
		quoteImportQueueRepository.save(item);
		publisher.publishEvent(item);
	}
	
	public ImportQuoteQueueItem findById(String id) {
		return Optional.ofNullable(quoteImportQueueRepository.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Queue Item with id {0} cannot be found.", id));
	}

	public Page<ImportQuoteQueueItem> search(ImportQueueSearchJson search) {
		Pageable paging = new PageRequest(search.getPage(), search.getPageSize(),Direction.DESC, "createdAt");
		Query query = new Query().with(paging);
		if(Objects.nonNull(search.getQuoteNumber())) {
			query.addCriteria(Criteria.where("quoteNumber").is(search.getQuoteNumber()));
		}
		if(StringUtils.isNotEmpty(search.getExternalSystemName())) {
			query.addCriteria(Criteria.where("externalSystemName").is(search.getExternalSystemName()));
		}
		if(!CollectionUtils.isEmpty(search.getStatus())) {
			query.addCriteria(Criteria.where("status").in(search.getStatus()));
		}
		if(Objects.nonNull(search.getMinCreateDate()) || Objects.nonNull(search.getMaxCreateDate())) {
			List<Criteria> ranges = new ArrayList<>();
			if(Objects.nonNull(search.getMinCreateDate())) {
				ranges.add(Criteria.where("createdAt").gte(search.getMinCreateDate()));
			}
			if(Objects.nonNull(search.getMaxCreateDate())) {
				ranges.add(Criteria.where("createdAt").lte(search.getMaxCreateDate().plusDays(1)));
			}
			query.addCriteria(Criteria.where(StringUtils.EMPTY).andOperator(ranges.stream().toArray(Criteria[]::new)));
		}
		
		return PageableExecutionUtils.getPage
				(mongoTemplate.find(query, ImportQuoteQueueItem.class), 
				paging, 
				()->mongoTemplate.count(query, ImportQuoteQueueItem.class));
	}
}
