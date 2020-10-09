package ekol.crm.quote.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.repository.*;
import ekol.crm.quote.validator.PriceValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PriceCrudService {

    private PriceRepository repository;
    private PriceValidator validator;
    private PriceDiscountCrudService quotePriceDiscountCrudService;
    private PriceAuthorizationRepository priceAuthorizationRepository;

    @Transactional
    public Set<Price> save(Quote quote, Set<Price> existed, Set<Price> request){
    	Set<Price> prices = request.stream().map(price->this.saveOrUpdate(quote, price)).collect(Collectors.toSet());
    	Set<Long> deleteIds = SetUtils.difference(
    			existed.parallelStream().map(Price::getId).collect(Collectors.toSet()), 
    			request.parallelStream().map(Price::getId).collect(Collectors.toSet())
    			);
    	this.delete(existed.parallelStream().filter(price->deleteIds.contains(price.getId())).collect(Collectors.toSet()));
    	return prices;
    }
    
    public Price saveOrUpdate(Quote quote, Price price) {
    	price.setQuote(quote);
		this.validator.validate(price);

		PriceAuthorization auth = price.getAuthorization();
		price.setAuthorization(null);

		Price saved = repository.save(price);
		Optional.ofNullable(price.getPriceCalculation())
			.map(PriceCalculation::getDiscounts)
			.ifPresent(discounts->this.quotePriceDiscountCrudService.save(saved, discounts));
		this.updatePriceAuthorization(saved, auth);
		
		saved.setPriceCalculation(price.getPriceCalculation());
		return saved;
    }
    
    private void updatePriceAuthorization(Price price, PriceAuthorization authorization) {
    	if(Objects.isNull(authorization)) {
    		Optional.of(price)
    			.map(Price::getId)
    			.map(priceAuthorizationRepository::findByPriceId)
    			.ifPresent(priceAuthorizationRepository::delete);
    	} else {
    		if(authorization.isNew()) {
    			priceAuthorizationRepository.deleteByPriceId(price.getId());
    		}
    		authorization.setPrice(price);
    		price.setAuthorization(priceAuthorizationRepository.save(authorization));
    	}
    }
    
    @Transactional
    public void delete(Set<Price> prices){
    	prices.forEach(this::delete);
    }

    @Transactional
    public void delete(Price price){
        price.setDeleted(true);
        quotePriceDiscountCrudService.deleteForQuotePrice(price);
        repository.save(price);
    }

    @Transactional
    public void deleteForQuote(Quote quote){
        List<Price> prices = repository.findByQuote(quote);
        prices.forEach(this::delete);
    }
}
