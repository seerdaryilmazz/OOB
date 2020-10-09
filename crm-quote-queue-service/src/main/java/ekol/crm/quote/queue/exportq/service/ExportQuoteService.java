package ekol.crm.quote.queue.exportq.service;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.retry.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.queue.exportq.connection.ExternalSystemConnectionProperties;
import ekol.crm.quote.queue.exportq.domain.ExportQuoteQueueItem;
import ekol.crm.quote.queue.exportq.enums.*;
import ekol.crm.quote.queue.exportq.exception.ExportFailureException;
import ekol.exceptions.ResourceNotFoundException;
import lombok.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExportQuoteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportQuoteService.class);

	@Value("${oneorder.dataExportEnabled:true}")
	private boolean dataExportEnabled;

	@NonNull
	private ExternalSystemConnectionProperties properties;

	@NonNull
	private ExportQueueService quoteQueueService;

	@NonNull
	private ApplicationEventPublisher publisher;
	
	private RestTemplate restTemplate = null;
	
	@PostConstruct
	private void initRestTemplate() {
		restTemplate = new RestTemplateBuilder()
				.setConnectTimeout(properties.getConnectTimeout())
				.setReadTimeout(properties.getReadTimeout())
				.errorHandler(new ExternalServerErrorHandler())
				.build();
	}

	@Transactional
	@Retryable(	value= ExportFailureException.class,
				maxAttemptsExpression="${oneorder.consecutiveErrorThreshold:10}", 
				backoff=@Backoff( delayExpression="${oneorder.backOff.delay:1000}", multiplierExpression="${oneorder.backOff.multiplier:2}"))
	public void export(String id) {
		ExportQuoteQueueItem queueItem = null;
		try {
			queueItem = quoteQueueService.findById(id);
		} catch(ResourceNotFoundException nfe) {
			LOGGER.error("QueueItem not found. Id: {}", id);
			throw new ExportFailureException();
		}
		if (Stream.of(Status.SUCCESSFUL,Status.OUT_OF_DATE).anyMatch(queueItem.getStatus()::equals)) {
			return;
		}
		try {
			if (dataExportEnabled) {
				boolean outOfDate = false;
				if(Objects.equals(queueItem.getQuote().getDmlType(), DmlType.UPDATE.getCode())){
					ExportQuoteQueueItem latestRevision = quoteQueueService.findLatestRevisionByOrderNumber(queueItem.getQuoteNumber());
					outOfDate = Objects.nonNull(latestRevision) && latestRevision.getQuote().getRevisionNumber() > queueItem.getQuote().getRevisionNumber();
				}

				if(!outOfDate) {
					postToExternalServer(queueItem);
					queueItem.setStatus(Status.SUCCESSFUL);
				} else {
					queueItem.setStatus(Status.OUT_OF_DATE);
				}

			} else {
				queueItem.setStatus(Status.SKIPPED);
			}
			quoteQueueService.save(queueItem);
		} catch (RuntimeException e) {
			String message = MessageFormat.format("Error exporting queue item id: {0}, quote id: {1}, quote number: {2}",
					queueItem.getId(),
					queueItem.getQuoteId(),
					queueItem.getQuoteNumber());
			LOGGER.error(message, e);
			handleExportOrderError(queueItem);
		}
	}

	@Recover
	public void recoverExport(ExportFailureException throwable, String id) {
		LOGGER.warn("consecutivefailure: {}", Calendar.getInstance());
		ExportQuoteQueueItem queueItem = quoteQueueService.findById(id);
		queueItem.setStatus(Status.CONSECUTIVE_FAILURE);
		quoteQueueService.save(queueItem);
	}

	private void handleExportOrderError(ExportQuoteQueueItem queueItem) {
		queueItem.increaseRetryCount();
		queueItem.setStatus(Status.FAILED);
		quoteQueueService.save(queueItem);
		throw new ExportFailureException();
	}
	
	private void postToExternalServer(ExportQuoteQueueItem exchangeData) {
		restTemplate.exchange(properties.getEndpoint(), HttpMethod.POST,
				new HttpEntity<>(exchangeData.getQuote(), createHttpHeaders(properties)), String.class);
	}


	private MultiValueMap<String, String> createHttpHeaders(ExternalSystemConnectionProperties properties) {
		MultiValueMap<String, String> headers = new HttpHeaders();
		String authHeader = "Basic " + new String(Base64.encodeBase64(
				(properties.getUsername() + ":" + properties.getPassword()).getBytes(StandardCharsets.US_ASCII)));
		headers.add("Authorization", authHeader);
		return headers;

	}
}
