package ekol.crm.quote.service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.client.*;
import ekol.crm.quote.config.*;
import ekol.crm.quote.domain.EmailJson;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.accountservice.Account;
import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.domain.dto.quote.QuoteJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.event.*;
import ekol.crm.quote.pdf.PdfService;
import ekol.crm.quote.repository.*;
import ekol.crm.quote.validator.QuoteValidatorFactory;
import ekol.exceptions.*;
import ekol.model.IdNamePair;
import ekol.model.User;
import ekol.resource.oauth2.SessionOwner;
import lombok.*;

@Getter(AccessLevel.PROTECTED)
@Service
public abstract class AbstractQuoteService {
	
	private static final String QUADRO = "QUADRO";
	
	private static final String QUOTE_INCREMENT_KEY = "increment_quoteNumber";
    private static final int QUOTE_INCREMENT_START = 10000;

	@Autowired
	private QuoteValidatorFactory quoteValidatorFactory;
	@Autowired
	private PdfService pdfService;
	@Autowired
	private QuoteRepository quoteRepository;
	@Autowired
	private AccountService accountService;
	@Autowired
	private KartoteksService kartoteksService;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private AuthorizationService authorizationService;
	@Autowired
	private SessionOwner sessionOwner;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private NoteCrudService noteCrudService;
	@Autowired
	private DocumentCrudService documentCrudService;
	@Autowired
	private ProductCrudService productCrudService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private QuoteIdMappingRepository quoteIdMappingRepository;
	@Autowired
	private QuoteOrderMappingRepository quoteOrderMappingRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomQuoteRepository customQuoteRepository;
	@Autowired
	private ExportCompanyNotificationMailList notifactionMailList;
	@Autowired
	private QuoteReceiveNotificationMailList quoteReceiveNotificationMailList;
	
	protected abstract void createPdf(Quote quote);
	
	public abstract void emailQuote(Quote quote, EmailJson request);
	
	protected User currentUser() {
		return sessionOwner.getCurrentUser();
	}
	
	private Long produceQuoteNumber(){
        redisTemplate.opsForValue().setIfAbsent(QUOTE_INCREMENT_KEY, String.valueOf(QUOTE_INCREMENT_START));
        redisTemplate.opsForValue().increment(QUOTE_INCREMENT_KEY, 1);
        return Long.valueOf(redisTemplate.opsForValue().get(QUOTE_INCREMENT_KEY));
    }

	@Transactional
	public Quote save(Quote quote) {
		quoteValidatorFactory.getService(quote.getClass()).validate(quote);
		quote.setNumber(produceQuoteNumber());
		quote.adjustName();
		
		Quote entity = updateSimple(quote);
		saveRelationsForQuote(entity, quote);
		
		publisher.publishEvent(QuoteEvent.with(entity, QuoteEventOperation.UPDATED));
		publisher.publishEvent(new QuoteSearchIndexEventMessage(entity.getId()));

		return fillNotesAndDocuments(entity);
	}
	
	@Transactional
	public void updateAccount(Collection<Long> ids, IdNamePair account) {
		Iterable<Quote> quotes = quoteRepository.findAll(ids);
		quotes.forEach(quote->{
			QuoteJson existed = quote.toJson();
			quote.setAccount(account);
			Quote saved = quoteRepository.save(quote);
			publisher.publishEvent(QuoteEvent.with(existed, saved, QuoteEventOperation.UPDATED));
			publisher.publishEvent(new QuoteSearchIndexEventMessage(saved.getId()));
		});
	}
	
	@Transactional
	public Quote updateSimple(Quote quote) {
		boolean inital = Objects.isNull(quote.getId());
		Quote entity = quoteRepository.save(quote);
		entity.setInitial(inital);
        return entity;
	}
	
	@Transactional
	public Quote update(Long id, Quote quote) {
		if(!Objects.equals(id, quote.getId())) {
			throw new BadRequestException();
		}
		Quote existed = getById(id);
		customQuoteRepository.detach(existed);
		QuoteJson existedJson = existed.toJson();
		quote.adjustName();
		
		saveRelationsForQuote(existed, quote);
		quote.getQuoteAttribute().putAll(existed.getQuoteAttribute());
		quote.setMappedIds(existed.getMappedIds());
		quote.setOrders(existed.getOrders());
		Quote saved = null;
		if(isStatusChanging(quote, existed)) {
			saved = updateStatus(quote, existed.getStatus());
		} else {
			if(this.isValidationRequired(quote, existed)) {
				quoteValidatorFactory.getService(quote.getClass()).validate(quote);
			}
			saved = quoteRepository.save(quote);
		}
		publisher.publishEvent(QuoteEvent.with(existedJson, saved, QuoteEventOperation.UPDATED));
		publisher.publishEvent(new QuoteSearchIndexEventMessage(saved.getId()));
		return saved;
	}
	
	protected boolean isValidationRequired(Quote quote, Quote previous) {
		return Objects.equals(previous.getAccount(), quote.getAccount());
	}

	protected Quote updateStatus(Quote quote, QuoteStatus oldStatus) {
		if(QuoteStatus.OPEN != quote.getStatus() || QuoteStatus.CANCELED == quote.getStatus()) {
			quoteValidatorFactory.getService(quote.getClass()).ensureContactAndLocationsAreValid(quote);
		}
		if(QuoteStatus.WON == oldStatus) {
			quote.setHoldingForCompanyTransfer(Boolean.FALSE);
		}
		
		if (QuoteStatus.PDF_CREATED == quote.getStatus()) {
			createPdf(quote);
		} else if(QuoteStatus.OPEN == quote.getStatus()) {
			resetProducts(quote);
		} else if(Stream.of(QuoteStatus.WON, QuoteStatus.PARTIAL_WON).anyMatch(quote.getStatus()::equals)) {
			boolean holdingForCompanyTransfer = ensureQuoteIsUpdatableToWon(quote);
			quote.setHoldingForCompanyTransfer(holdingForCompanyTransfer);
		}
		return quoteRepository.save(quote);
	}
	
	public Quote getById(Long id) {
		Quote quote = quoteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quote with id {0} not found", id));
		fillNotesAndDocuments(quote);
		return quote;
	}

	private Quote getByNumber(Long number) {
		return quoteRepository.findByNumber(number).orElseThrow(() -> new ResourceNotFoundException("Quote with number {0} not found", number));
	}
	
	public Quote getByNumberWithExtra(Long number) {
		Quote quote = getByNumber(number);
        fillNotesAndDocuments(quote);
		return quote;
	}
	
	public Page<Quote> list(Pageable pageRequest) {
        return quoteRepository.findAll(pageRequest);
    }
	
	public Iterable<Quote> listByAccountId(Long accountId){
		return quoteRepository.findByAccountId(accountId);
	}
	
	public Iterable<Quote> listAll(Collection<Long> ids){
		return quoteRepository.findAll(ids);
	}
	
	public List<Quote> findByAttribute(String attributeKey, String attributeValue){
		return quoteRepository.findQuotesByQuoteAttributes(attributeKey, attributeValue);
	}
	
	@Transactional
	public void delete(Quote quote){
        quote.setDeleted(true);
        quoteRepository.save(quote);
    }
	
	@Transactional
	public List<Note> updateNotes(Long id, List<NoteJson> request){
		Quote quote = getById(id);
		quote.setNotes(noteCrudService.getByQuote(quote));
		return noteCrudService.save(quote, request);
	}

	@Transactional
	public List<Document> updateDocuments(Long id, List<DocumentJson> request){
		Quote quote = getById(id);
		quote.setDocuments(documentCrudService.getByQuote(quote));
		return documentCrudService.save(quote, request);
	}
	
	protected boolean isStatusChanging(Quote quote, Quote existed) {
		return quote.getStatus() != existed.getStatus();
	}

	protected void resetProducts(Quote quote) {
		Optional.ofNullable(quote.getProducts())
			.map(Collection::stream)
			.orElseGet(Stream::empty)
			.parallel()
			.forEach(product->{
				product.setStatus(ProductStatus.OPEN);
				product.setLostReason(null);
			});
	}
	
	protected Quote fillNotesAndDocuments(Quote quote){
        quote.setNotes(noteCrudService.getByQuote(quote));
        quote.setDocuments(documentCrudService.getByQuote(quote));
        return quote;
    }
	
	protected void saveRelationsForQuote(Quote existed, Quote quote) {
		quote.setProducts(getProductCrudService().save(quote, existed.getProducts(), quote.getProducts()));
	}
	
	protected void sendEmail(Quote quote, EmailMessage emailMessage) {
		String emailId = getEmailService().sendMail(emailMessage);
		getDocumentCrudService().getByQuote(quote)
		.stream()
		.max((x, y) -> x.getCreateDate().getDateTime().compareTo(y.getCreateDate().getDateTime()))
		.ifPresent(doc->{
			Set<String> sentEmailIds = Optional.ofNullable(doc.getEmails()).orElseGet(HashSet::new);
			sentEmailIds.add(emailId);
			doc.setEmails(sentEmailIds);
			getDocumentCrudService().save(doc);
		});
	}
	
	@Transactional
	public Quote updateApplicationIdMapping(Long quoteNumber, QuoteIdMappingJson idMappingJson) {
        idMappingJson.setApplication(idMappingJson.getApplication().toUpperCase());

        Quote quote = getByNumber(quoteNumber);
        List<QuoteIdMapping> mappings = quoteIdMappingRepository.findByQuoteNumberAndApplication(quoteNumber, idMappingJson.getApplication());

        mappings.forEach(t->t.setDeleted(true));
        quoteIdMappingRepository.save(mappings);

        QuoteIdMapping item = QuoteIdMapping.withApplication(idMappingJson.getApplication(), idMappingJson.getApplicationId());
        item.setQuote(quote);
        
        quote.getMappedIds().clear();
        quote.getMappedIds().add(quoteIdMappingRepository.save(item));

        publisher.publishEvent(new QuoteSearchIndexEventMessage(quote.getId()));

        if (QUADRO.equals(item.getApplication())) {
        	String mailBody = pdfService.createMailForSpotQuote(SpotQuote.class.cast(quote), 1L);
        	if(StringUtils.isNotEmpty(mailBody)) {
        		User user = userService.findUser(quote.getCreatedBy());
        		List<String> cc = new ArrayList<>();
        		if("SEA".equals(quote.getServiceArea())) {
        			cc.addAll(quoteReceiveNotificationMailList.getSea());
        		} else if("AIR".equals(quote.getServiceArea())) {
        			cc.addAll(quoteReceiveNotificationMailList.getAir());
        		}
				EmailMessage emailMessage = new EmailMessage();
        		emailMessage.setTo(Arrays.asList(user.getEmail()));
        		emailMessage.setCc(cc);
        		emailMessage.setSubject(MessageFormat.format("Quote {0} has been transferred to Quadro", String.valueOf(quote.getNumber())));
        		emailMessage.setBody(mailBody);
        		emailMessage.setHtml(Boolean.TRUE);
        		publisher.publishEvent(emailMessage);
        	}
        }
        return quote;
    }
	
	protected boolean ensureQuoteIsUpdatableToWon(Quote quote) {
		return ensureCompanyAndLocations(quote);
	}
	
	public boolean ensureCompanyAndLocations(Quote quote) {
		CompanyAndLocation queryResult = findAccountCompanyAndCompanyLocation(quote);
		return getHoldingForCompanyTransfer(queryResult, quote.getSubsidiary().getId());
	}

	public boolean getHoldingForCompanyTransfer(CompanyAndLocation companyAndLocation, Long subsidiaryId) {
		boolean locationsOk = true;
		if (!doesCompanyLocationHaveMapping(companyAndLocation.getLocation()) && !doesCompanyLocationHaveAtLeastOneImportQueueItem(companyAndLocation.getCompany(), companyAndLocation.getLocation())) {
			locationsOk = false;
			// trigger "send to export queue"
			getPublisher().publishEvent(new ForceLocationExportEventMessage(companyAndLocation.getLocation().getId()));
			// send notification email
			Optional.ofNullable(createCompanyExportNotificationEmail(companyAndLocation.getCompany().getName(), subsidiaryId)).ifPresent(getPublisher()::publishEvent);
		}

		return !locationsOk;
	}
	
	public CompanyAndLocation findAccountCompanyAndCompanyLocation(Quote quote) {
		Account account = Optional.ofNullable(getAccountService().findAccountById(quote.getAccount().getId(), true)).orElseThrow(()->new BadRequestException("Account is deleted."));
		Company accountCompany = Optional.ofNullable(getKartoteksService().findCompanyById(account.getCompany().getId(), true)).orElseThrow(()->new BadRequestException("Account company is deleted."));
		CompanyLocation accountCompanyLocation = Optional.ofNullable(getKartoteksService().findLocationById(quote.getAccountLocation().getId(), true)).filter(CompanyLocation::getActive).orElseThrow(()->new BadRequestException("Account company location is deleted or not active."));
		return CompanyAndLocation.with(accountCompany, accountCompanyLocation);
	}
	
	protected boolean doesCompanyLocationHaveMapping(CompanyLocation companyLocation) {
		return Optional.ofNullable(companyLocation.getMappedIds())
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.parallel()
				.anyMatch(item->Objects.equals(QUADRO, item.getApplication()) && StringUtils.isNotBlank(item.getApplicationLocationId()));
	}

	protected boolean doesCompanyLocationHaveAtLeastOneImportQueueItem(Company company, CompanyLocation companyLocation) {
		return Objects.nonNull(getKartoteksService().findLastImportQueueItemForLocation(QUADRO, company.getId(), companyLocation.getId(), true));
	}
	
	private EmailMessage createCompanyExportNotificationEmail(String companyName, Long subsidiaryId) {

		String countryIso="TR";
		if(Objects.nonNull(subsidiaryId)) {
			countryIso = Optional.ofNullable(subsidiaryId).map(getAuthorizationService()::findCountryOfSubsidiary).map(ekol.crm.quote.domain.dto.kartoteksservice.Country::getIso).orElse(countryIso);
		}
		List<String> mailGroup = Objects.equals(countryIso,"TR") ? notifactionMailList.getTr() : notifactionMailList.getOther();
		if(CollectionUtils.isEmpty(mailGroup)){
			return null;
		}
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setTo(mailGroup);
		emailMessage.setSubject("company card notification");
		emailMessage.setBody(companyName + " company card has inserted to task table to create or match on Quadro. Please check as soon as possible!");
		emailMessage.setHtml(Boolean.FALSE);
		return emailMessage;
	}
	
	@Transactional
	public Quote updateOrderMapping(Long number, QuoteOrderMappingJson request) {
		Quote quote = getByNumber(number); 
		QuoteOrderMapping mapping = quoteOrderMappingRepository.findByQuoteNumberAndOrderNumber(number, request.getOrderNumber()).orElseGet(QuoteOrderMapping::new);
		mapping.setQuote(quote);
		mapping.setOrderNumber(request.getOrderNumber());
		mapping.setOrderRelation(request.getRelation());
		mapping.setOrderStatus(request.getOrderStatus());
		quoteOrderMappingRepository.save(mapping);
		return quote;
	}
}
