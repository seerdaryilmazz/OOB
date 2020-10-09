package ekol.crm.quote.service;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ekol.crm.quote.client.PricingServiceClient;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.pricingservice.*;
import ekol.crm.quote.domain.dto.pricingservice.Calculation.QuoteInfo;
import ekol.crm.quote.domain.dto.product.SpotProductJson;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.enumaration.Currency;
import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.model.Price;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.BadRequestException;
import ekol.model.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CostBasedPricingService extends AbstractPricingService {
	
	private static final String FREIGHT_SUFFIX = "_FREIGHT";
	
	private PricingServiceClient pricingServiceClient;
	
	public CalculatedPriceJson[] calculatePrices(QuoteJson quote) {
		SpotQuoteJson spotQuote = SpotQuoteJson.class.cast(quote);
		SpotProductJson product = quote.getProducts().stream().map(SpotProductJson.class::cast).findFirst().orElseThrow(BadRequestException::new);
		Calculation request = new Calculation();
		request.setQuoteInfo(QuoteInfo.with(quote.getId(), quote.getNumber(), quote.getName(), quote.getAccount()));
		request.setCollectionTariff(Optional.of(product).map(SpotProductJson::getCollectionTariffRegion).map(CodeNamePair::getCode).orElse(null));
		request.setDeliveryTariff(Optional.of(product).map(SpotProductJson::getDeliveryTariffRegion).map(CodeNamePair::getCode).orElse(null));
		request.setFromCountry(product.getFromCountry());
		request.setFromPostal(product.getFromPoint().getName());
		request.setToCountry(product.getToCountry());
		request.setToPostal(product.getToPoint().getName());
		request.setSubsidiary(spotQuote.getSubsidiary());
		request.setWarehouse(Optional.ofNullable(product.getCollectionWarehouse()).orElseGet(product::getDeliveryWarehouse));
		request.setPayweight(spotQuote.getPayWeight());
		request.setGrossWeight(Optional.of(spotQuote).map(SpotQuoteJson::getMeasurement).map(MeasurementJson::getWeight).orElse(BigDecimal.ZERO));
		request.setBillingItems(spotQuote.getPrices().stream().map(PriceJson::getBillingItem).filter(t->!t.getName().endsWith(FREIGHT_SUFFIX)).map(i->IdCodeName.with(i.getId(), i.getCode(), i.getName())).collect(Collectors.toSet()));
		CalculationResult[] results = pricingServiceClient.calculate(request);
		return Stream.of(results).map(result->{
			CalculatedPriceJson price = new CalculatedPriceJson();
			price.setProfit(result.getProfit());
			price.setUnitPrice(mapCostBasedFreightDetail(result));
			price.setPrices(mapPrice(result, spotQuote.getPrices(), quote.getSubsidiary()));
			return price;
		}).toArray(CalculatedPriceJson[]::new);
	}
	
	private List<PriceJson> mapPrice(CalculationResult calculation, List<PriceJson> prices, IdNamePair subsidiary) {
		return prices.stream().map(PriceJson::toEntity).map(PriceJson::fromEntity).map(price->{
			String billingItem = price.getBillingItem().getName();
			PriceCalculationJson calc = new PriceCalculationJson();

			if(billingItem.endsWith(FREIGHT_SUFFIX)) {
				Optional<Money> chargeAmount = Optional.ofNullable(calculation).map(CalculationResult::getTotalFreightAmount);
				Optional<Money> calculatedAmount = Optional.ofNullable(calculation).map(CalculationResult::getFreight).map(Freight::getAmount);
				calculatedAmount.map(Money::getAmount).ifPresent(calc::setCalculatedAmount);
				calculatedAmount.map(Money::getAmount).ifPresent(calc::setProposedAmount);
				chargeAmount.map(qr->new MonetaryAmountJson(qr.getAmount(), Currency.valueOf(qr.getCurrency()))).ifPresent(price::setCharge);
			} else {
				Optional<QueryResult> billingItemResult = Optional.ofNullable(calculation.getBillingItems().get(billingItem));
				billingItemResult.map(QueryResult::getPrice).ifPresent(calc::setCalculatedAmount);
				billingItemResult.map(QueryResult::getPriceLimit).map(Range::getMinimum).ifPresent(calc::setMinAmount);
				billingItemResult.map(QueryResult::getPrice).ifPresent(calc::setProposedAmount);
				billingItemResult.map(qr->new MonetaryAmountJson(qr.getTotalAmount(), Currency.valueOf(qr.getCurrency()))).ifPresent(price::setCharge);
			}
			BigDecimal exchangeRate = getExchangeRateService().getExchangeRate(price.getCharge().getCurrency(), subsidiary.getId());
			price.setPriceExchanged(new MonetaryAmountJson(exchangeRate.multiply(price.getCharge().getAmount()), Currency.EUR));
			price.setPriceCalculation(calc);
			return price;
		}).collect(Collectors.toList());
	}
	
	private CostBasedFreightDetailJson mapCostBasedFreightDetail(CalculationResult price) {
		Optional<Freight> freightOptional = Optional.ofNullable(price).map(CalculationResult::getFreight);
		if(freightOptional.isPresent()) {
			Money collection = freightOptional.map(Freight::getCollection).map(result->Money.of(result.getUnitPrice(), result.getCurrency())).orElse(null);
			Money delivery = freightOptional.map(Freight::getDelivery).map(result->Money.of(result.getUnitPrice(), result.getCurrency())).orElse(null);
			Money linehaul = freightOptional.map(Freight::getLinehaul).map(result->Money.of(result.getUnitPrice(), result.getCurrency())).orElse(null);
			return CostBasedFreightDetailJson.with(collection, delivery, linehaul);
		}
		return null;
	}

	public void checkPriceValidity(String billingItemCode, Quote quote) {
		SpotQuote spotQuote = SpotQuote.class.cast(quote);
		List<Price> willValidated = new ArrayList<>();
		if(StringUtils.isEmpty(billingItemCode)){
			spotQuote.getPrices().forEach(price -> adjustWillValidatedPrices(spotQuote, willValidated, price));
		}else{
			spotQuote.getPrices().stream()
			.filter(p->Objects.equals(billingItemCode, getBillingItemService().getBillingItemByName(p.getBillingItem()).getCode()))
			.findFirst().ifPresent(price->adjustWillValidatedPrices(spotQuote, willValidated, price));
		}
//		if(!CollectionUtils.isEmpty(willValidated)){
//			validateSalesPrice(spotQuote, willValidated);
//		}
	}

	@Override
	protected boolean isCalculationRequired(String billingItemCode, Quote quote) {
		return QuoteType.SPOT == quote.getType();
	}
}
