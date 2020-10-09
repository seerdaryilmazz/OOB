package ekol.crm.quote.service;

import java.math.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.product.SpotProductJson;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.BillingItem;
import ekol.crm.quote.domain.model.ServiceType;
import ekol.crm.quote.domain.model.quote.SpotQuote;
import ekol.crm.quote.repository.SpotQuoteRepository;
import ekol.exceptions.ValidationException;
import ekol.model.*;
import ekol.oneorder.configuration.ConfigurationApi;
import ekol.oneorder.configuration.dto.NumberOption;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CalculationService {

	private SpotQuoteRepository spoQuoteRepository;
	private BillingItemService billingItemService;
	private ConfigurationApi configurationApi;

    public List<BillingItem> determineBillingItems(QuoteJson request) {
        Set<BillingItem> billingItems = new LinkedHashSet<>();
        SpotQuoteJson quote = (SpotQuoteJson)request;
        SpotProductJson product = null;

        if(quote.getType() != QuoteType.SPOT){
            throw new ValidationException("Only spot quotes can have billing items");
        }
        if(!CollectionUtils.isEmpty(quote.getProducts())){
            if(quote.getProducts().size() != 1){
                throw new ValidationException("Spot quotes can have only one product");
            }
            product = (SpotProductJson) quote.getProducts().get(0);
        }

        // Begin : Billing items specific to product info
        String fromCountryCode = Optional.ofNullable(product)
                .map(SpotProductJson::getFromCountry)
                .map(IsoNamePair::getIso).orElse("");

        String toCountryCode = Optional.ofNullable(product)
                .map(SpotProductJson::getToCountry)
                .map(IsoNamePair::getIso).orElse("");

        String incotermExplanation = Optional.ofNullable(product)
                .map(SpotProductJson::getIncotermExplanation).orElse("");

        if(!quote.getServiceArea().getCode().equals("ROAD")){
            if(Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode) &&
                    !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode)){
                billingItems.add(billingItemService.getBillingItemByName(quote.getServiceArea().getCode() + "_IMPORT_CUSTOMS_CLEARANCE"));
            }
            if(Constants.INCOTERM_EXP_DOOR_TO_PORT.equalsIgnoreCase(incotermExplanation) ||
                    Constants.INCOTERM_EXP_PORT_TO_PORT.equalsIgnoreCase(incotermExplanation)){
                billingItems.add(billingItemService.getBillingItemByName(quote.getServiceArea().getCode() + "_EXW_COST"));
            }
        } else {
        	billingItems.addAll(determineProductBillingItemFromRule(product, quote.getSubsidiary(), quote.getPayWeight()));
        	Set<String> codes = Optional.ofNullable(quote.getVehicleRequirements())
        			.map(Collection::stream)
        			.orElseGet(Stream::empty)
        			.map(VehicleRequirementJson::getRequirement)
        			.map(i->MessageFormat.format("{0}_{1}_COST", quote.getServiceArea().getCode(), i.getCode()))
        			.collect(Collectors.toSet());
        	Optional.ofNullable(codes)
        		.filter(CollectionUtils::isNotEmpty)
        		.map(billingItemService::getBillingItemByName)
        		.ifPresent(billingItems::addAll);
//            if (!(Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) && !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode))) {
//                if (Utils.hashSet("EXW", "FOB", "FCA").contains(incoterm)) {
//                    billingItems.add(billingItemService.getBillingItemByName("AGENCY_COMMISSION_COST"));
//                }
//            }
        }

        //End


        // Billing items specific to customs info

        DeliveryType deliveryType = (product != null ? product.getDeliveryType() : null);

        CustomsClearanceType arrivalClearanceType= Optional.ofNullable(quote.getCustoms())
                .map(CustomsJson::getArrival)
                .map(CustomsPointJson::getClearanceType).orElse(null);

        ClearanceResponsible arrivalClearanceResponsible= Optional.ofNullable(quote.getCustoms())
                .map(CustomsJson::getArrival)
                .map(CustomsPointJson::getClearanceResponsible).orElse(null);

        CustomsClearanceType departureClearanceType= Optional.ofNullable(quote.getCustoms())
                .map(CustomsJson::getDeparture)
                .map(CustomsPointJson::getClearanceType).orElse(null);

        ClearanceResponsible departureClearanceResponsible= Optional.ofNullable(quote.getCustoms())
                .map(CustomsJson::getDeparture)
                .map(CustomsPointJson::getClearanceResponsible).orElse(null);



        Optional.ofNullable(arrivalClearanceType)
                .map(CustomsClearanceType::getBillingItemName)
                .ifPresent(billingItemName -> billingItems.add(billingItemService.getBillingItemByName(billingItemName)));

        Optional.ofNullable(departureClearanceType)
                .map(CustomsClearanceType::getBillingItemName)
                .ifPresent(billingItemName -> billingItems.add(billingItemService.getBillingItemByName(billingItemName)));

        
        if ((Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) || 
        		(!Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) && !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode))) &&
        		ClearanceResponsible.EKOL == arrivalClearanceResponsible){
        	billingItems.add(billingItemService.getBillingItemByName("ARRIVAL_CUSTOMS_COST"));
        }

        if(Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) && !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode)
                && ClearanceResponsible.EKOL == departureClearanceResponsible) {
            billingItems.add(billingItemService.getBillingItemByName("EXIT_CUSTOMS_COST_EXPORT"));
        }

        if((CustomsClearanceType.WHERE_CUSTOMER_PREFERRED == arrivalClearanceType
        		|| CustomsClearanceType.AT_DELIVERY_LOCATION == arrivalClearanceType)|| 
        		(CustomsClearanceType.WHERE_CUSTOMER_PREFERRED == departureClearanceType
        		|| CustomsClearanceType.AT_LOADING_LOCATION == departureClearanceType)){
        	billingItems.add(billingItemService.getBillingItemByName("ARRIVAL_CUSTOMS_AREA_DIFF"));

        }
        if((Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode) || 
        		(!Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) && !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode)))&&
        		ClearanceResponsible.EKOL == departureClearanceResponsible){
        	billingItems.add(billingItemService.getBillingItemByName("EXIT_CUSTOMS_COST"));

        }


        // todo: Yukarıdaki else kısmı silinip burası açılacak ama şimdilik böyle kalsın dendiği için böyle.
//        if ((Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode) && ClearanceResponsible.EKOL == departureClearanceResponsible) ||
//                (!Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) && !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode))) {
//            billingItems.add(BillingItem.EXIT_CUSTOMS_COST);
//        }

        if (StringUtils.isNotBlank(fromCountryCode) && StringUtils.isNotBlank(toCountryCode)
        		&& !Constants.COUNTRY_CODE_TR.equalsIgnoreCase(fromCountryCode) && Constants.COUNTRY_CODE_TR.equalsIgnoreCase(toCountryCode) 
        		&& DeliveryType.CUSTOMER_ADDRESS == deliveryType) {
        	billingItems.add(billingItemService.getBillingItemByName("DELIVERY_COST"));
        }

        //End

        if(!CollectionUtils.isEmpty(quote.getLoads())){
            billingItems.addAll(quote.getLoads().stream()
                    .filter(load -> load.getType().getBillingItemName() != null)
                    .map(load ->  billingItemService.getBillingItemByName(load.getType().getBillingItemName())).collect(Collectors.toList()));
        }

        if(!CollectionUtils.isEmpty(quote.getServices())){
        	billingItems.addAll(quote.getServices().stream().map(ServiceJson::getType).map(ServiceType::getBillingItem).filter(Objects::nonNull).collect(Collectors.toList()));
        }

        // Sort by billing item code
        return billingItems.stream().sorted(Comparator.comparing(BillingItem::getCode)).collect(Collectors.toList());
    }
    
    private Collection<BillingItem> determineProductBillingItemFromRule(SpotProductJson product, IdNamePair subsidiary, BigDecimal payweight){
    	if(Objects.isNull(product)) {
    		return Collections.emptySet();
    	}
    	Map<String,Object> productMap = new LinkedHashMap<>();
        productMap.put("fromCountry", product.getFromCountry().getIso());
        productMap.put("toCountry", product.getToCountry().getIso());
        productMap.put("incotermExplanation", product.getIncotermExplanation());
        productMap.put("incoterm", product.getIncoterm());
        productMap.put("serviceArea", product.getServiceArea().getCode());
        productMap.put("payweight", Optional.ofNullable(payweight).map(BigDecimal::longValue).orElse(null));
        List<Integer>[] ruleResults = (List<Integer>[])configurationApi.evaluate("RULE_FOR_ROAD_PRODUCT_BILLING_ITEMS", subsidiary.getId(), productMap, List[].class);
        if(ArrayUtils.isNotEmpty(ruleResults)) {
        	Set<String> codes = Stream.of(ruleResults).flatMap(Collection::stream).map(String::valueOf).collect(Collectors.toSet());
        	return billingItemService.getBillingItemByCode(codes);
        }
        return Collections.emptySet();
    }

    public PayWeightCalculationJson calculatePayWeight(PayWeightCalculationParams request) {
    	boolean isSpeedy = Optional.of(request).map(PayWeightCalculationParams::getSpeedyService).orElse(Boolean.FALSE).booleanValue()
    			|| Optional.of(request).map(PayWeightCalculationParams::getSpeedyvanService).orElse(Boolean.FALSE).booleanValue();
		BigDecimal ldmCoefficient = isSpeedy ? Constants.LDM_COEFFICIENT_SPEEDY : Constants.LDM_COEFFICIENT;
		BigDecimal volumeCoefficient = isSpeedy ? Constants.VOLUME_COEFFICIENT_SPEEDY : Constants.VOLUME_COEFFICIENT;
    	SpotQuote spotQuote = Optional.of(request).map(PayWeightCalculationParams::getQuoteId).flatMap(spoQuoteRepository::findById).orElse(null);
    	if(null != spotQuote) {
    		boolean wasSpeedy = Optional.of(spotQuote)
    			.map(SpotQuote::getServices)
    			.map(Collection::stream)
    			.orElseGet(Stream::empty)
    			.map(ekol.crm.quote.domain.model.Service::getType)
    			.anyMatch(i->ServiceTypeCategory.MAIN ==i.getCategory() && (i.getCode().equals("SPEEDY") || i.getCode().equals("SPEEDY_VAN")));
    		if(wasSpeedy == isSpeedy) {
    			ldmCoefficient = spotQuote.getPayWeightCalculation().getLdmCoefficient();
    			volumeCoefficient = spotQuote.getPayWeightCalculation().getVolumeCoefficient();
    		}
    	}
    	
    	IdNamePair subsidiary = Optional.of(request).map(PayWeightCalculationParams::getSubsidiary).orElseThrow(()->new ValidationException("Please select Owner Subsidiary first!"));
		BigDecimal weight = Optional.of(request).map(PayWeightCalculationParams::getWeight).orElse(BigDecimal.ZERO);
		BigDecimal ldm = Optional.of(request).map(PayWeightCalculationParams::getLdm).orElse(BigDecimal.ZERO);
		BigDecimal volume = Optional.of(request).map(PayWeightCalculationParams::getVolume).orElse(BigDecimal.ZERO);

		BigDecimal optionValue = Optional.ofNullable(configurationApi.getNumber("MINIMUM_PAYWEIGHT", subsidiary.getId()))
				.map(NumberOption::getValue)
				.orElse(new BigDecimal("100"));

		BigDecimal calculatedPayweight = Stream.of(weight, ldm.multiply(ldmCoefficient), volume.multiply(volumeCoefficient))
				.max(BigDecimal::compareTo)
				.orElse(BigDecimal.ZERO);

		BigDecimal pw = optionValue.compareTo(calculatedPayweight) >= 0 
				? optionValue
				: calculatedPayweight.setScale(-2, RoundingMode.UP); 
		return PayWeightCalculationJson.builder()
				.payWeight(pw)
				.volumeCoefficient(volumeCoefficient)
				.ldmCoefficient(ldmCoefficient)
				.build();
       
    }
    
    public BigDecimal calculateChargeableWeight(ChargeableWeightParams request) {
    	
    	BigDecimal weight = request.getWeight() != null ? request.getWeight() : BigDecimal.ZERO;
    	BigDecimal volume = request.getVolume() != null ? request.getVolume() : BigDecimal.ZERO;
    	
    	BigDecimal result = volume.multiply(new BigDecimal("1000")).divide(new BigDecimal("6"), 2, RoundingMode.HALF_UP);
    	
    	return result.max(weight).setScale(0, RoundingMode.HALF_UP);
    }

}

