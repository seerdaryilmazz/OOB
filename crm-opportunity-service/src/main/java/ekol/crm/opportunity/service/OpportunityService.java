package ekol.crm.opportunity.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import ekol.crm.opportunity.domain.dto.*;
import ekol.crm.opportunity.domain.enumaration.*;
import ekol.crm.opportunity.domain.model.*;
import ekol.crm.opportunity.event.*;
import ekol.crm.opportunity.repository.OpportunityRepository;
import ekol.crm.opportunity.validation.*;
import ekol.exceptions.*;
import ekol.model.*;
import ekol.resource.validation.OneOrderValidation;
import lombok.AllArgsConstructor;

/**
 * Created by Dogukan Sahinturk on 18.11.2019
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OpportunityService {

    private OpportunityRepository opportunityRepository;
    private StringRedisTemplate redisTemplate;
    private NoteCrudService noteCrudService;
    private DocumentCrudService documentCrudService;
    private ApplicationEventPublisher eventPublisher;
    private CrmQuoteService crmQuoteService;

    private static final String OPPORTUNITY_INCREMENT_KEY = "increment_opportunityNumber";
    private static final int OPPORTUNITY_INCREMENT_START = 10000;

    public OpportunityJson getOpportunityById(Long id){
        Opportunity opportunity = getByIdOrThrowException(id);
        fillNotesAndDocuments(opportunity);
        return OpportunityJson.fromEntity(opportunity);
    }

    public Opportunity getByIdOrThrowException(Long id){
        return opportunityRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Opportunity with id {0} is not found.", id));
    }

    private Long produceAgreementNumber(){
        redisTemplate.opsForValue().setIfAbsent(OPPORTUNITY_INCREMENT_KEY, String.valueOf(OPPORTUNITY_INCREMENT_START));
        redisTemplate.opsForValue().increment(OPPORTUNITY_INCREMENT_KEY, 1);
        return Long.valueOf(redisTemplate.opsForValue().get(OPPORTUNITY_INCREMENT_KEY));
    }
    
	@Transactional
	public void updateAccount(Collection<Long> ids, IdNamePair account) {
		Iterable<Opportunity> opportunities = opportunityRepository.findAll(ids);
		opportunities.forEach(opportunity ->{
			opportunity.setAccount(account);
			Opportunity savedOpportunity = opportunityRepository.save(opportunity);
			eventPublisher.publishEvent(OpportunityEventData.with(savedOpportunity, Operation.UPDATED));
		});
	}

    @Transactional
    public Opportunity save(OpportunityJson request) {
        Opportunity opportunity = request.toEntity();
        if(opportunity.getId() == null && opportunity.getNumber() == null){
            opportunity.setNumber(produceAgreementNumber());
        }else {
            Opportunity existedOpportunity = getByIdOrThrowException(opportunity.getId());
            opportunity.getOpportunityAttribute().putAll(existedOpportunity.getOpportunityAttribute());
        }
        
        saveRelationsForOpportunity(opportunity);
        Opportunity savedOpportunity = opportunityRepository.save(opportunity);
        fillNotesAndDocuments(savedOpportunity);
        eventPublisher.publishEvent(OpportunityEventData.with(savedOpportunity, Operation.UPDATED));
        return savedOpportunity;
    }

    private void saveRelationsForOpportunity(Opportunity opportunity) {
        if(Optional.ofNullable(opportunity.getProducts()).isPresent()) {
            opportunity.getProducts().forEach(product -> product.setOpportunity(opportunity));
        }
        if(Optional.ofNullable(opportunity.getCloseReason()).isPresent()){
            opportunity.getCloseReason().setOpportunity(opportunity);
        }
    }

    private void fillNotesAndDocuments(Opportunity opportunity){
        opportunity.setNotes(noteCrudService.getByOpportunity(opportunity));
        opportunity.setDocuments(documentCrudService.getByOpportunity(opportunity));
    }

    public List<Note> updateNotes(Long id, List<NoteJson> request) {
        Opportunity opportunity = getByIdOrThrowException(id);
        opportunity.setNotes(noteCrudService.getByOpportunity(opportunity));
        return noteCrudService.save(opportunity, request);
    }

    public List<Document> updateDocuments(Long id, List<DocumentJson> request) {
        Opportunity opportunity = getByIdOrThrowException(id);
        opportunity.setDocuments(documentCrudService.getByOpportunity(opportunity));
        return documentCrudService.save(opportunity, request);
    }

    public Page<Opportunity> listByAccountIdStatusIsNot(Pageable pageable, Long accountId, OpportunityStatus status){
        return opportunityRepository.findByAccountIdAndStatusIsNot(pageable, accountId, status);
    }

    public Page<Opportunity> retrieveOpportunities(Pageable pageable, Long accountId, boolean ignoreCanceled){
        OpportunityStatus opportunityStatus = ignoreCanceled ? OpportunityStatus.CANCELED : null;
        return opportunityRepository.findAllByAccountId(pageable, accountId, opportunityStatus);
    }
    
    public List<Opportunity> getByAccountId(Long accountId){
    	return opportunityRepository.findByAccountId(accountId);
    }

    @Transactional
    public OpportunityJson calculateQuotedTurnover(QuoteJson quote, OpportunityJson opportunityJson) {
        if (Objects.nonNull(quote.getPreviousData())) {
            if (quote.getStatus().getCode().equals("CANCELED")) {
                opportunityJson.setQuotedTurnoverPerYear(opportunityJson.getQuotedTurnoverPerYear().subtractAmount(getQuotedTurnover(quote)));
                save(opportunityJson);
            } else {
                BigDecimal difference = getQuotedTurnover(quote).subtract(getQuotedTurnover(quote.getPreviousData()));
                if (difference.compareTo(BigDecimal.ZERO) != 0) {
                    opportunityJson.setQuotedTurnoverPerYear(opportunityJson.getQuotedTurnoverPerYear().addAmount(difference));
                }
            }
        } else {
            opportunityJson.setQuotedTurnoverPerYear(opportunityJson.getQuotedTurnoverPerYear().addAmount(getQuotedTurnover(quote)));
        }
        return opportunityJson;
    }

    private BigDecimal getQuotedTurnover(QuoteJson quote) {
        BigDecimal amount = BigDecimal.ZERO;
        if (quote.getType().getCode().equals("SPOT")) {
            List<PriceJson> prices = quote.getPrices();
            for (PriceJson priceJson : prices) {
                if (priceJson.getType() == QuotePriceType.INCOME) {
                    amount = amount.add(Optional.of(priceJson.getPriceExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
                } else if (priceJson.getType() == QuotePriceType.EXPENSE) {
                    amount = amount.subtract(Optional.of(priceJson.getPriceExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
                }
            }
        } else {
            List<QuoteProductJson> products = quote.getProducts();
            for (QuoteProductJson product : products) {
                amount = amount.add(Optional.ofNullable(product.getExpectedTurnoverExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
            }
        }
        return amount;
    }

    public OpportunityJson calculateCommittedTurnover(QuoteJson quote, String status, OpportunityJson opportunityJson){
        BigDecimal amountToBeAdded = BigDecimal.ZERO;
        if("SPOT".equals(quote.getType().getCode())){
            List<PriceJson> prices = quote.getPrices();
            for (PriceJson priceJson : prices) {
                if (priceJson.getType() == QuotePriceType.INCOME) {
                    amountToBeAdded = amountToBeAdded.add(Optional.ofNullable(priceJson.getPriceExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
                } else if (priceJson.getType() == QuotePriceType.EXPENSE) {
                    amountToBeAdded = amountToBeAdded.subtract(Optional.ofNullable(priceJson.getPriceExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
                }
            }
        }else {
            if(status.equals("WON")){
                List<QuoteProductJson> products = quote.getProducts();
                for (QuoteProductJson product : products) {
                    if(product.getStatus().getCode().equals("WON")){
                        amountToBeAdded = amountToBeAdded.add(
                                Optional.ofNullable(product.getExpectedTurnoverExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
                    }
                }
            }else if(status.equals("OPEN")){
                List<QuoteProductJson> oldProducts = quote.getPreviousData().getProducts();
                for(QuoteProductJson oldProduct : oldProducts){
                    if(oldProduct.getStatus().getCode().equals("WON")){
                        amountToBeAdded = amountToBeAdded.add(
                                Optional.ofNullable(oldProduct.getExpectedTurnoverExchanged()).map(MonetaryAmountJson::getAmount).orElse(BigDecimal.ZERO));
                    }
                }
            }
        }
        if(status.equals("WON")){
            opportunityJson.setCommittedTurnoverPerYear(opportunityJson.getCommittedTurnoverPerYear().addAmount(amountToBeAdded));
        }else if(status.equals("OPEN")){
            opportunityJson.setCommittedTurnoverPerYear(opportunityJson.getCommittedTurnoverPerYear().subtractAmount(amountToBeAdded));
        }
        return opportunityJson;
    }

    @Transactional
    public void deleteOpportunity(Opportunity opportunity){
        opportunity.setDeleted(true);
        opportunityRepository.save(opportunity);
    }
    
    @Transactional
    public void deleteOpportunity(Iterable<Opportunity> opportunities){
    	opportunities.forEach(t->t.setDeleted(true));
    	opportunityRepository.save(opportunities);
    }
    
    @Transactional
    public Opportunity reopen(Long id) {
		Opportunity opportunity = getByIdOrThrowException(id);

		if (Stream.of(OpportunityStatus.WITHDRAWN, OpportunityStatus.REJECTED).anyMatch(opportunity.getStatus()::equals)) {
			opportunity.setStatus(OpportunityStatus.PROSPECTING);
		} else if (Stream.of(OpportunityStatus.CLOSED).anyMatch(opportunity.getStatus()::equals)) {
			List<QuoteJson> quotes = crmQuoteService.getQuotesCreatedFromOpportunity(opportunity.getId(), true);
			if (quotes.stream().map(QuoteJson::getStatus).map(CodeNamePair::getCode).anyMatch(status->!Objects.equals("CANCELED", status))) {
				opportunity.setStatus(OpportunityStatus.QUOTED);
			} else {
				opportunity.setStatus(OpportunityStatus.PROSPECTING);
			}
		} else throw new BadRequestException("Opportunity can not be reopened");
		return save(OpportunityJson.fromEntity(opportunity));
	}
}
