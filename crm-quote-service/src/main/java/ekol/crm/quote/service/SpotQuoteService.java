package ekol.crm.quote.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.client.FileServiceClient;
import ekol.crm.quote.domain.EmailJson;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.event.dto.Contact;
import ekol.crm.quote.repository.SpotQuoteRepository;
import ekol.crm.quote.util.Utils;
import ekol.exceptions.*;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.StringIdNamePair;

@Service
public class SpotQuoteService extends AbstractQuoteService  {

	@Autowired
	private SpotQuoteRepository spotQuoteRepository;
	@Autowired
	private FileServiceClient fileServiceClient;
	@Autowired
	private PriceCalculationService priceCalculationService;
	@Autowired
	private CustomsCrudService customsCrudService;
	@Autowired
	private VehicleRequirementCrudService vehicleRequirementCrudService;
	@Autowired
	private ContainerRequirementCrudService containerRequirementCrudService;
	@Autowired
	private PaymentRuleCrudService paymentRuleCrudService;
	@Autowired
	private PackageCrudService packageCrudService;
	@Autowired
	private LoadCrudService loadCrudService;
	@Autowired
	private ServiceCrudService serviceCrudService;
	@Autowired
	private PriceCrudService priceCrudService;
	
	@Override
	protected Quote updateStatus(Quote quote, QuoteStatus oldStatus) {
		validatePriceAuthorization(quote);
		return super.updateStatus(quote, oldStatus);
	}
	
	public Quote getByReferrerTask(String referrerTaskId) {
		Quote quote =  spotQuoteRepository.findByReferrerTaskId(referrerTaskId).orElseThrow(()->new ResourceNotFoundException("Spot quote with referrer task id {0} not found", referrerTaskId));
		fillNotesAndDocuments(quote);
		return quote;
	}
	
	private void validatePriceAuthorization(Quote quote) {
		if (QuoteStatus.PDF_CREATED == quote.getStatus() 
				&& SpotQuote.class.cast(quote).getPrices().stream().map(Price::getAuthorization).filter(Objects::nonNull).map(PriceAuthorization::getCloseStatus).anyMatch(PriceAuthorizationStatus.REQUESTED::equals)) {
			throw new ValidationException("PDF can't  be created since Price Request transaction has not been completed yet");
		}
	}

	@Override
	protected void saveRelationsForQuote(Quote existed, Quote quote) {
		SpotQuote existedSpotQuote = SpotQuote.class.cast(existed);
		SpotQuote spotQuote = SpotQuote.class.cast(quote);

		spotQuote.setProducts(getProductCrudService().save(quote, existed.getProducts(), quote.getProducts()));
		if (isPriceValidityCheckRequired(existed, quote)) {
			priceCalculationService.checkPriceValidity(null, spotQuote);
		}
		if(spotQuote.getServiceArea().equals("ROAD")){
			spotQuote.setCustoms(customsCrudService.save(spotQuote.getCustoms()));
			spotQuote.setVehicleRequirements(vehicleRequirementCrudService.save(quote, existedSpotQuote.getVehicleRequirements(), spotQuote.getVehicleRequirements()));
		}else if(spotQuote.getServiceArea().equals("SEA")){
			spotQuote.setContainerRequirements(containerRequirementCrudService.save(quote, existedSpotQuote.getContainerRequirements(), spotQuote.getContainerRequirements()));
		}
		spotQuote.setPaymentRule(paymentRuleCrudService.save(spotQuote.getPaymentRule()));
		spotQuote.setPackages(packageCrudService.save(spotQuote, existedSpotQuote.getPackages(), spotQuote.getPackages()));
		spotQuote.setLoads(loadCrudService.save(spotQuote, existedSpotQuote.getLoads(), spotQuote.getLoads()));
		spotQuote.setServices(serviceCrudService.save(spotQuote, existedSpotQuote.getServices(), spotQuote.getServices()));
		spotQuote.setPrices(priceCrudService.save(spotQuote, existedSpotQuote.getPrices(), spotQuote.getPrices()));
	}
	
	@Override
	public boolean ensureCompanyAndLocations(Quote quote) {
		Long subsidiaryId = quote.getSubsidiary().getId();
		CompanyAndLocation queryResult = findAccountCompanyAndCompanyLocation(quote);
		boolean result = getHoldingForCompanyTransfer(queryResult, subsidiaryId);
		
		SpotQuote spotQuote = SpotQuote.class.cast(quote);
		if(!result && !Objects.equals(queryResult.getLocation().getId(), spotQuote.getPaymentRule().getInvoiceLocation().getId())){
			Company invoiceCompany = Optional.ofNullable(getKartoteksService().findCompanyById(spotQuote.getPaymentRule().getInvoiceCompany().getId(), true)).orElseThrow(()->new BadRequestException("Invoice company is deleted."));
			CompanyLocation invoiceCompanyLocation = Optional.ofNullable(getKartoteksService().findLocationById(spotQuote.getPaymentRule().getInvoiceLocation().getId(), true)).filter(CompanyLocation::getActive).orElseThrow(()->new BadRequestException("Invoice company location is deleted or not active."));
			return getHoldingForCompanyTransfer(CompanyAndLocation.with(invoiceCompany, invoiceCompanyLocation), subsidiaryId);
		}
		
		return result;
	}

	@Override
	protected void createPdf(Quote quote) {
		SpotQuote spotQuote = SpotQuote.class.cast(quote);
		File multipartFile = getPdfService().createPdfForSpotQuote(spotQuote, spotQuote.getPdfLanguage().getId());

		//File upload for pdf
		StringIdNamePair fileEntry = fileServiceClient.upload(multipartFile);

		//Pdf save
		DocumentJson documentJson = new DocumentJson();
		documentJson.setIneffaceable(true);
		documentJson.setDocumentId(fileEntry.getId());
		documentJson.setDocumentName(fileEntry.getName());
		documentJson.setCreateDate(new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime()));
		documentJson.setCreatedBy(currentUser().getUsername());

		getDocumentCrudService().savePdf(spotQuote, Arrays.asList(documentJson));
	}

	private boolean isPriceValidityCheckRequired(Quote existingQuote, Quote updatedQuote) {
		return !isStatusChanging(existingQuote, updatedQuote) && QuoteStatus.OPEN == updatedQuote.getStatus();
	}

	@Override
	public void emailQuote(Quote quote, EmailJson request) {
		if (CollectionUtils.isEmpty(request.getTo())) {
			throw new BadRequestException("At least one to address must be specified.");
		}

		SpotQuote spotQuote = SpotQuote.class.cast(quote);

		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setReplyTo(currentUser().getEmail());
		emailMessage.setTo(request.getTo());
		emailMessage.setCc(request.getCc());
		emailMessage.setBcc(request.getBcc());
		emailMessage.setSubject(request.getSubject());
		emailMessage.setBody(request.getBody());
		emailMessage.setHtml(Boolean.FALSE);
		emailMessage.setAttachments(new ArrayList<>());
		try {
			byte[] pdfFileContent = getPdfService().readPdfForSpotQuote(spotQuote);
			byte[] encodedPdfFileContent = Base64.encodeBase64(pdfFileContent);

			EmailMessage.Attachment attachment = new EmailMessage.Attachment();
			attachment.setName(Utils.generatePdfFileName(spotQuote));
			attachment.setBase64EncodedContent(new String(encodedPdfFileContent, StandardCharsets.UTF_8));

			emailMessage.getAttachments().add(attachment);

		} catch(Exception e) {
			throw new ApplicationException("Could prepare attachment");
		}
		sendEmail(quote, emailMessage);
	}
	
	@Transactional
    public void updateQuoteContactName(Contact contact) {
    	List<SpotQuote> quotes = spotQuoteRepository.findByContactId(contact.getCompanyContactId());
    	quotes.forEach(quote-> quote.getContact().setName(contact.getFullname()));
    	spotQuoteRepository.save(quotes);
    }
	
	public List<SpotQuote> findAll(Long potentialId) {
		return spotQuoteRepository.findByPotentialId(potentialId);
	}
}
