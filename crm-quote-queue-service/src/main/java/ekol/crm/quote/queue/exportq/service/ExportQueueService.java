package ekol.crm.quote.queue.exportq.service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.crm.quote.queue.common.dto.*;
import ekol.crm.quote.queue.common.service.client.*;
import ekol.crm.quote.queue.exportq.domain.ExportQuoteQueueItem;
import ekol.crm.quote.queue.exportq.enums.*;
import ekol.crm.quote.queue.exportq.repository.ExportQuotQueueRepository;
import ekol.exceptions.*;
import ekol.model.*;
import ekol.oneorder.configuration.ConfigurationApi;
import ekol.oneorder.configuration.dto.ListOption;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ExportQueueService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportQueueService.class);

	private MongoTemplate mongoTemplate;
	private ExportQuotQueueRepository quoteExportQueueRepository;
	private AccountServiceClient accountServiceClient;
	private QuoteServiceClient quoteServiceClient;
	private QuoteConverter quoteConverter;
	private ApplicationEventPublisher publisher;
	private KartoteksServiceClient kartoteksServiceClient;
	private ConfigurationApi configurationApi;

	
	@Transactional
	public void addQueueItemByNumber(Long quoteNumber) {
		addQueueItem(quoteServiceClient.getQuote(quoteNumber));
	}
	
	
	private DmlType adjustDmlType(Supplier<List<ExportQuoteQueueItem>> exportQueueItemSupplier) {
		List<ExportQuoteQueueItem> items = exportQueueItemSupplier.get();
		if(CollectionUtils.isEmpty(items)) {
			return DmlType.INSERT;
		} else{
			return DmlType.UPDATE;
		}
	}
	
	@Transactional
	public void addQueueItem(QuoteJson quote) {
		if (!"SPOT".equalsIgnoreCase(quote.getType().getCode())) {
			return;
		}
		if(isPastRecord(quote)) {
			return;
		}
		if(!this.isEligible(quote)) {
			return;
		}
		if (quote.getHoldingForCompanyTransfer() != null && quote.getHoldingForCompanyTransfer().booleanValue()) {
			return;
		}
		if(!locationIsBelongToCompany(quote)) {
			return;
		}
		DmlType operation = adjustDmlType(()->quoteExportQueueRepository.findByQuoteNumber(quote.getNumber()));
		if(operation == DmlType.INSERT && !Objects.equals("WON", quote.getStatus().getCode())) {
			return;
		} 
		if(!Objects.equals("CANCELED", quote.getStatus().getCode()) 
				&& !Objects.equals("WON", quote.getStatus().getCode()) 
				&& !Objects.equals("OPEN", quote.getStatus().getCode()) 
				&& !Objects.equals("LOST", quote.getStatus().getCode())) {
			return;
		}

		int revisionNumber = 1 + Optional.ofNullable(quoteExportQueueRepository.findFirstByQuoteNumberOrderByQuoteRevisionNumberDesc(quote.getNumber())).map(ExportQuoteQueueItem::getQuote).map(ekol.crm.quote.queue.exportq.dto.QuoteJson::getRevisionNumber).orElse(-1);
		ExportQuoteQueueItem item = quoteExportQueueRepository.save(
				ExportQuoteQueueItem.builder()
				.operation(operation)
				.quoteId(quote.getId())
				.quoteNumber(quote.getNumber())
				.quote(quoteConverter.convert(quote, operation, revisionNumber))
				.status(Status.PENDING)
				.build());
		
		publisher.publishEvent(item);
	}

	@Transactional
	public void requeue(String id) {
		ExportQuoteQueueItem item = findById(id);
		requeue(item);
	}
	
	@Transactional
	public void requeue(ExportQuoteQueueItem item) {
		if(item.getStatus() == Status.OUT_OF_DATE) {
			throw new BadRequestException("Out of dated quote can not be exported");
		} else if(item.getStatus() == Status.PENDING){
			throw new BadRequestException("Queue item is {0} status that can not execute again manually", item.getStatus().name());
		} else if(item.getStatus() == Status.SUCCESSFUL && findByQuoteNumber(item.getQuoteNumber()).stream().anyMatch(i->i.getStatus() == Status.SUCCESSFUL && i.getLastUpdated().isAfter(item.getLastUpdated()))) {
			throw new BadRequestException("successfully exported quote is exist after this this item, it can not be export again");
		}
		item.setRetryCount(0);
		item.setStatus(Status.REQUEUED);
		quoteExportQueueRepository.save(item);
		publisher.publishEvent(item);
	}
	
	@Transactional
	public void requeue(long daysBefore) {
		QuoteExportSearchJson query = new QuoteExportSearchJson();
		query.setStatus(Arrays.asList(Status.CONSECUTIVE_FAILURE));
		query.setMinCreateDate(LocalDate.now().minusDays(daysBefore));
		query.setPageSize(100);
		Page<ExportQuoteQueueItem> result = search(query);
		for (ExportQuoteQueueItem failedItem : result.getContent()) {
			ExportQuoteQueueItem maxRevisionedId = quoteExportQueueRepository.findFirstByQuoteNumberOrderByQuoteRevisionNumberDesc(failedItem.getQuoteNumber());
			if(maxRevisionedId.getQuote().getRevisionNumber() > failedItem.getQuote().getRevisionNumber()) {
				failedItem.setStatus(Status.OUT_OF_DATE);
				save(failedItem);
			}  else {
				requeue(failedItem);
			}
		}
	}

	public List<ExportQuoteQueueItem> findAll() {
		return quoteExportQueueRepository.findAll();
	}

	public List<ExportQuoteQueueItem> findByQuoteId(Long quoteId) {
		return Optional.ofNullable(quoteExportQueueRepository.findByQuoteId(quoteId)).orElseThrow(
				() -> new ResourceNotFoundException("Queue Item with Quote id {0} cannot be found.", quoteId));
	}

	public List<ExportQuoteQueueItem> findByQuoteNumber(Long quoteNumber) {
		return Optional.ofNullable(quoteExportQueueRepository.findByQuoteNumber(quoteNumber)).orElseThrow(
				() -> new ResourceNotFoundException("Queue Item with Quote number {0} cannot be found.", quoteNumber));
	}

	public ExportQuoteQueueItem findById(String id) {
		return Optional.ofNullable(quoteExportQueueRepository.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Queue Item with id {0} cannot be found.", id));
	}

	public ExportQuoteQueueItem save(ExportQuoteQueueItem item) {
		return quoteExportQueueRepository.save(item);
	}

	public ExportQuoteQueueItem findLatestRevisionByOrderNumber(Long quoteNumber) {
		return quoteExportQueueRepository.findFirstByQuoteNumberOrderByQuoteRevisionNumberDesc(quoteNumber);
	}

	public Page<ExportQuoteQueueItem> search(QuoteExportSearchJson search) {
		Pageable paging = new PageRequest(search.getPage(), search.getPageSize(),Direction.DESC, "createdAt");
		Query query = new Query().with(paging);
		if(Objects.nonNull(search.getQuoteNumber())) {
			query.addCriteria(Criteria.where("quoteNumber").is(search.getQuoteNumber()));
		}
		if(!CollectionUtils.isEmpty(search.getStatus())) {
			query.addCriteria(Criteria.where("status").in(search.getStatus()));
		}
		if(Objects.nonNull(search.getRevisionNumber())) {
			query.addCriteria(Criteria.where("quote.revisionNumber").in(search.getRevisionNumber()));
		}
		if(Objects.nonNull(search.getMinCreateDate()) || Objects.nonNull(search.getMaxCreateDate())) {
			List<Criteria> ranges = new ArrayList<>();
			if(Objects.nonNull(search.getMinCreateDate())) {
				ranges.add(Criteria.where("createdAt").gte(search.getMinCreateDate()));
			}
			if(Objects.nonNull(search.getMaxCreateDate())) {
				ranges.add(Criteria.where("createdAt").lte(search.getMaxCreateDate().plusDays(1)));
			}
			Criteria range = ranges.get(0);
			for (int i = 1; i < ranges.size(); i++) {
				range = range.andOperator(ranges.get(i));
				
			}
			query.addCriteria(range);
		}
		
		return PageableExecutionUtils.getPage
				(mongoTemplate.find(query, ExportQuoteQueueItem.class), 
				paging, 
				()->mongoTemplate.count(query, ExportQuoteQueueItem.class));
	}
	
	private boolean isPastRecord(QuoteJson quote) {
		return quote.getCreatedAt().getDateTime().toLocalDate().isAfter(quote.getValidityStartDate());
		
	}
	
	private boolean isEligible(QuoteJson quote) {
		ListOption option = configurationApi.getList("SERVICE_AREA_QUOTE_EXPORTED", quote.getSubsidiary().getId());
		if(Objects.isNull(option) || Objects.isNull(option.getValue())) {
			return false;
		}
		return Stream.of(option.getValue())
				.map(CodeNamePair::getCode)
				.anyMatch(quote.getServiceArea().getCode()::equalsIgnoreCase);
	}
	
	private boolean locationIsBelongToCompany(QuoteJson quote) {
		try {
			AccountJson account = accountServiceClient.find(quote.getAccount().getId());
			if(Objects.isNull(account)) {
				return false;
			}
			CompanyJson accountCompany = kartoteksServiceClient.findCompanyById(account.getCompany().getId());
			if(Objects.isNull(accountCompany)) {
				return false;
			}
			if(accountCompany.getCompanyLocations().parallelStream().map(IdNamePair::getId).noneMatch(quote.getAccountLocation().getId()::equals)) {
				return false;
			}
			
			CompanyJson invoiceCompany = kartoteksServiceClient.findCompanyById(quote.getPaymentRule().getInvoiceCompany().getId());
			if(Objects.isNull(invoiceCompany)) {
				return false;
			}
			if(invoiceCompany.getCompanyLocations().parallelStream().map(IdNamePair::getId).noneMatch(quote.getPaymentRule().getInvoiceLocation().getId()::equals)) {
				return false;
			}
			return true;
		} catch(Exception e) {
			LOGGER.error("validateLocationIsBelongToCompany error", e);
		}
		return false;
	}
}
