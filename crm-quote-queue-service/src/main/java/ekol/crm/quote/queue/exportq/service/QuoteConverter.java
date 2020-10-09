package ekol.crm.quote.queue.exportq.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import ekol.crm.quote.queue.common.dto.BillingItemJson;
import ekol.crm.quote.queue.common.dto.ExternalSystemIdJson;
import ekol.crm.quote.queue.common.dto.IsoNamePair;
import ekol.crm.quote.queue.common.dto.PlaceJson;
import ekol.crm.quote.queue.common.dto.ServiceTypeJson;
import ekol.crm.quote.queue.common.dto.UserJson;
import ekol.crm.quote.queue.common.dto.WarehouseJson;
import ekol.crm.quote.queue.common.service.client.AccountServiceClient;
import ekol.crm.quote.queue.common.service.client.LocationServiceClient;
import ekol.crm.quote.queue.common.service.client.UserServiceClient;
import ekol.crm.quote.queue.exportq.dto.AccountJson;
import ekol.crm.quote.queue.exportq.dto.ContainerRequirementJson;
import ekol.crm.quote.queue.exportq.dto.CustomsJson;
import ekol.crm.quote.queue.exportq.dto.CustomsPointJson;
import ekol.crm.quote.queue.exportq.dto.DimensionJson;
import ekol.crm.quote.queue.exportq.dto.LoadJson;
import ekol.crm.quote.queue.exportq.dto.MeasurementJson;
import ekol.crm.quote.queue.exportq.dto.MonetaryAmountJson;
import ekol.crm.quote.queue.exportq.dto.PackageJson;
import ekol.crm.quote.queue.exportq.dto.PaymentRuleJson;
import ekol.crm.quote.queue.exportq.dto.PriceJson;
import ekol.crm.quote.queue.exportq.dto.ProductJson;
import ekol.crm.quote.queue.exportq.dto.QuoteJson;
import ekol.crm.quote.queue.exportq.dto.VehicleRequirementJson;
import ekol.crm.quote.queue.exportq.enums.DmlType;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class QuoteConverter {
	
    private static final Logger LOG = LoggerFactory.getLogger(QuoteConverter.class);
    
    private static final String QUADRO = "QUADRO";

	@Value("${oneorder.gateway-basic-auth}")
	private String gatewayUrl;

	@NonNull
	private AccountServiceClient accountServiceClient;

	@NonNull
	private UserServiceClient userServiceClient;

	@NonNull
	private LocationServiceClient locationServiceClient;

	public QuoteJson convert(ekol.crm.quote.queue.common.dto.QuoteJson quote, DmlType operation, int revisionNumber) {
		QuoteJson json = new QuoteJson();
		json.setDmlType(operation.getCode());
		json.setNumber(quote.getNumber());
		json.setName(quote.getName());
		json.setAccount(convertAccount(quote.getAccount(), quote.getAccountLocation()));
		json.setSubsidiary(quote.getSubsidiary());
		json.setServiceArea(quote.getServiceArea().getCode());
		json.setType(quote.getType().getCode());
		json.setProducts(quote.getProducts().stream().map(this::convertProduct).collect(toList()));
		json.setStatus(quote.getStatus().getCode());
		json.setCreatedBy(convertUser(quote.getCreatedBy()));
		json.setCreatedAt(convertDateToString(quote.getCreatedAt().getDateTime().toLocalDate()));
		json.setResultDate(convertDateToString(quote.getLastUpdated().getDateTime().toLocalDate()));
		json.setValidityStartDate(convertDateToString(quote.getValidityStartDate()));
		json.setValidityEndDate(convertDateToString(quote.getValidityEndDate()));
		json.setContact(quote.getContact().getName());
		json.setTotalMeasurement(convertMeasurement(quote.getMeasurement()));
		json.setTotalPayWeight(quote.getPayWeight());
		setTotalQuantity(quote, json);
		json.setCustoms(convertCustoms(quote.getCustoms()));
		json.setPackages(Optional.ofNullable(quote.getPackages()).orElseGet(Collections::emptyList).stream().map(this::convertPackage).collect(toList()));
		json.setLoads(Optional.ofNullable(quote.getLoads()).orElseGet(Collections::emptyList).stream().map(this::convertLoad).collect(toList()));
		json.setServices(Optional.ofNullable(quote.getServices()).orElseGet(Collections::emptyList).stream().map(this::convertService).collect(toList()));
		json.setVehicleRequirements(Optional.ofNullable(quote.getVehicleRequirements()).orElseGet(Collections::emptyList).stream().map(this::convertVehicleReqirement).collect(toList()));
		json.setContainerRequirements(Optional.ofNullable(quote.getContainerRequirements()).orElseGet(Collections::emptyList).stream().map(this::convertContainerRequirement).collect(toList()));
		json.setPrices(Optional.ofNullable(quote.getPrices()).orElseGet(Collections::emptyList).stream().map(this::convertPrice).collect(toList()));
		json.setPaymentRule(convertPaymentRule(quote.getPaymentRule()));
		json.setRevisionNumber(revisionNumber);
		setAdrFields(quote, json);
		setFrigoFields(quote, json);
		setLongGoodsFields(quote, json);
		setOversizeGoodsFields(quote, json);
		setFragileGoodsFields(quote, json);
		setFoodProductFields(quote, json);
		setPalletizationFields(quote, json);
		setHealthCertificationFields(quote, json);
		setAtaCarnetFields(quote, json);
		setTransitTradeFields(quote, json);
		setCadFields(quote, json);
		setServiceType(quote, json);
		setLoadingAndDeliveryFields(quote, json);
		json.setChargeableWeight(quote.getChargeableWeight());
		return json;
	}

	private void setTotalQuantity(ekol.crm.quote.queue.common.dto.QuoteJson quote, QuoteJson target) {
		Integer totalQuantity = 0;
		if (!CollectionUtils.isEmpty(quote.getPackages())) {
			for (ekol.crm.quote.queue.common.dto.PackageJson packages : quote.getPackages()) {
				totalQuantity += packages.getQuantity();
			}
			target.setTotalQuantity(totalQuantity);
		}else{
			target.setTotalQuantity(quote.getQuantity());
		}
	}

	private void setAdrFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.LoadJson load = findLoadByType(source, "ADR");
		if (load != null) {
			target.setHasDangerousGoods("Y");
			target.setAdrType(load.getRiskFactor());
		} else {
			target.setHasDangerousGoods("N");
			target.setAdrType(null);
		}
	}

	private void setFrigoFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.LoadJson load = findLoadByType(source, "FRIGO");
		if (load != null) {
			target.setHasFrigoGoods("Y");
			target.setFrigoMinTemperature(load.getMinTemperature());
			target.setFrigoMaxTemperature(load.getMaxTemperature());
		} else {
			target.setHasFrigoGoods("N");
			target.setFrigoMinTemperature(null);
			target.setFrigoMaxTemperature(null);
		}
	}

	private void setLongGoodsFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.LoadJson load = findLoadByType(source, "LONG");
		if (load != null) {
			target.setHasLongGoods("Y");
		} else {
			target.setHasLongGoods("N");
		}
	}

	private void setOversizeGoodsFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.LoadJson load = findLoadByType(source, "ROAD_OVERSIZE");
		if (load != null) {
			target.setHasOversizeGoods("Y");
		} else {
			target.setHasOversizeGoods("N");
		}
	}

	private void setFragileGoodsFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.LoadJson load = findLoadByType(source, "FRAGILE");
		if (load != null) {
			target.setHasFragileGoods("Y");
		} else {
			target.setHasFragileGoods("N");
		}
	}

	private void setFoodProductFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.LoadJson load = findLoadByType(source, "FOOD");
		if (load != null) {
			target.setHasFoodProduct("Y");
		} else {
			target.setHasFoodProduct("N");
		}
	}

	private ekol.crm.quote.queue.common.dto.LoadJson findLoadByType(ekol.crm.quote.queue.common.dto.QuoteJson quote, String type) {
		ekol.crm.quote.queue.common.dto.LoadJson result = null;
		if (!CollectionUtils.isEmpty(quote.getLoads())) {
			for (ekol.crm.quote.queue.common.dto.LoadJson load : quote.getLoads()) {
				if (load.getType().getCode().equals(type)) {
					result = load;
					break;
				}
			}
		}
		return result;
	}

	private void setPalletizationFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.ServiceJson service = findServiceByCode(source, "ROAD_PALLETIZATION");
		if (service != null) {
			target.setHasPalletization("Y");
		} else {
			target.setHasPalletization("N");
		}
	}

	private void setHealthCertificationFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.ServiceJson service = findServiceByCode(source, "HEALTH_CERTIFICATION");
		if (service != null) {
			target.setHasHealthCertification("Y");
		} else {
			target.setHasHealthCertification("N");
		}
	}

	private void setAtaCarnetFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.ServiceJson service = findServiceByCode(source, "ATA_CARNET");
		if (service != null) {
			target.setHasAtaCarnet("Y");
		} else {
			target.setHasAtaCarnet("N");
		}
	}

	private void setTransitTradeFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.ServiceJson service = findServiceByCode(source, "TRANSIT_TRADE");
		if (service != null) {
			target.setHasTransitTrade("Y");
		} else {
			target.setHasTransitTrade("N");
		}
	}

	private void setCadFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.ServiceJson service = findServiceByCode(source, "CAD");
		if (service != null) {
			target.setHasCAD("Y");
		} else {
			target.setHasCAD("N");
		}
	}

	private ekol.crm.quote.queue.common.dto.ServiceJson findServiceByCode(ekol.crm.quote.queue.common.dto.QuoteJson quote, String code) {
		ekol.crm.quote.queue.common.dto.ServiceJson result = null;
		if (!CollectionUtils.isEmpty(quote.getServices())) {
			for (ekol.crm.quote.queue.common.dto.ServiceJson service : quote.getServices()) {
				if (service.getType().getCode().equals(code)) {
					result = service;
					break;
				}
			}
		}
		return result;
	}

	private void setServiceType(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {
		ekol.crm.quote.queue.common.dto.ServiceJson service = findFirstServiceByCategory(source, "MAIN");
		if (service != null) {
			target.setServiceType(service.getType().getCode());
		}
	}

	private ekol.crm.quote.queue.common.dto.ServiceJson findFirstServiceByCategory(ekol.crm.quote.queue.common.dto.QuoteJson quote, String category) {
		ekol.crm.quote.queue.common.dto.ServiceJson result = null;
		if (!CollectionUtils.isEmpty(quote.getServices())) {
			for (ekol.crm.quote.queue.common.dto.ServiceJson service : quote.getServices()) {
				if (service.getType().getCategory().equals(category)) {
					result = service;
					break;
				}
			}
		}
		return result;
	}

	private void setLoadingAndDeliveryFields(ekol.crm.quote.queue.common.dto.QuoteJson source, QuoteJson target) {

		if (!CollectionUtils.isEmpty(source.getProducts()) && source.getProducts().size() == 1) {

			ekol.crm.quote.queue.common.dto.ProductJson product = source.getProducts().get(0);

			if (product.getLoadingCompany() != null) {
				target.setLoadingCompanyId(product.getLoadingCompany().getId());
				target.setLoadingCompanyName(product.getLoadingCompany().getName());
			}
			if (product.getLoadingLocation() != null) {
				target.setLoadingLocationId(product.getLoadingLocation().getId());
				target.setLoadingLocationName(product.getLoadingLocation().getName());
			}
			if (product.getDeliveryCompany() != null) {
				target.setDeliveryCompanyId(product.getDeliveryCompany().getId());
				target.setDeliveryCompanyName(product.getDeliveryCompany().getName());
			}
			if (product.getDeliveryLocation() != null) {
				target.setDeliveryLocationId(product.getDeliveryLocation().getId());
				target.setDeliveryLocationName(product.getDeliveryLocation().getName());
			}

			if (!CollectionUtils.isEmpty(target.getProducts()) && target.getProducts().size() == 1) {
				ProductJson targetProduct = target.getProducts().get(0);
				targetProduct.setLoadingCompany(null);
				targetProduct.setLoadingLocation(null);
				targetProduct.setDeliveryCompany(null);
				targetProduct.setDeliveryLocation(null);
			}
		}
	}

	private AccountJson convertAccount(IdNamePair account, IdNamePair accountLocation) {
		if(Objects.isNull(account)) {
			return null;
		}
		ekol.crm.quote.queue.common.dto.AccountJson obj = accountServiceClient.find(account.getId());
		AccountJson json = new AccountJson();
		json.setId(obj.getId());
		json.setName(obj.getName());
		json.setLocation(accountLocation);
		json.setCompany(obj.getCompany());
		json.setAccountOwner(convertUser(obj.getAccountOwner()));
		return json;
	}

	private UserJson convertUser(String userName) {
		if(StringUtils.isEmpty(userName)){
			return null;
		}
		return userServiceClient.findUser(userName);
	}

	private ProductJson convertProduct(ekol.crm.quote.queue.common.dto.ProductJson product) {
		
		ProductJson json = new ProductJson();
		json.setFromCountry(product.getFromCountry());
		json.setFromPoints(this.convertCountryPoint(product.getFromPoint()));
		json.setToCountry(product.getToCountry());
		json.setToPoints(this.convertCountryPoint(product.getToPoint()));
		json.setShipmentLoadingType(product.getShipmentLoadingType());
		json.setIncoterm(product.getIncoterm());
		json.setIncotermExplanation(product.getIncotermExplanation());
		json.setStatus(product.getStatus().getCode());
		json.setEarliestReadyDate(convertDateToString(product.getEarliestReadyDate()));
		json.setLatestReadyDate(convertDateToString(product.getLatestReadyDate()));
		json.setLoadingType(product.getLoadingType() != null ? product.getLoadingType().getCode() : null);
		json.setLoadingCompany(product.getLoadingCompany());
		json.setLoadingLocation(product.getLoadingLocation());
		json.setDeliveryType(product.getDeliveryType() != null ? product.getDeliveryType().getCode() : null);
		json.setDeliveryCompany(product.getDeliveryCompany());
		json.setDeliveryLocation(product.getDeliveryLocation());
		json.setVehicleCount(product.getVehicleCount());
		return json;
	}
	
	private List<IdNamePair> convertCountryPoint(IdNamePair countryPoint) {
		return Optional.ofNullable(countryPoint)
				.map(IdNamePair::getId)
				.map(accountServiceClient::findCountryPoint)
				.map(t->IdNamePair.with(t.getId(), t.getCode()))
				.map(Arrays::asList)
				.orElseGet(Collections::emptyList);
	}

	private MeasurementJson convertMeasurement(ekol.crm.quote.queue.common.dto.MeasurementJson measurement) {
		if(measurement == null){
			return null;
		}
		MeasurementJson json = new MeasurementJson();
		json.setWeight(measurement.getWeight());
		json.setLdm(measurement.getLdm());
		json.setVolume(measurement.getVolume());
		return json;
	}

	private DimensionJson convertDimension(ekol.crm.quote.queue.common.dto.DimensionJson dimension) {
		if(dimension == null){
			return null;
		}
		DimensionJson json = new DimensionJson();
		json.setWidth(dimension.getWidth());
		json.setLength(dimension.getLength());
		json.setHeight(dimension.getHeight());
		return json;
	}

	private CustomsJson convertCustoms(ekol.crm.quote.queue.common.dto.CustomsJson customs) {
		if(customs == null){
			return null;
		}
		CustomsJson json = new CustomsJson();
		json.setArrival(convertCustomsPoint(customs.getArrival(), true));
		json.setDeparture(convertCustomsPoint(customs.getDeparture(), false));
		return json;
	}

	private CustomsPointJson convertCustomsPoint(ekol.crm.quote.queue.common.dto.CustomsPointJson customsPoint, boolean isArrivalCustoms) {
		if(Objects.isNull(customsPoint)) {
			return null;
		}
		
		Optional<WarehouseJson> warehouse = Optional.ofNullable(customsPoint.getLocation()).map(IdNamePair::getId).map(locationServiceClient::getCustomsByCompanyLocation);

		CustomsPointJson json = new CustomsPointJson();
		warehouse.ifPresent(l->json.setCustomsLocationId(l.getCompanyLocation().getId()));

		Optional<PlaceJson> place = Optional.ofNullable(customsPoint.getLocation()).map(IdNamePair::getId).map(locationServiceClient::getPlaceByCompanyLocation);

		if (place.isPresent()) {
			json.setCustomsLocationCountry(place.get().getEstablishment().getAddress().getCountry().getIso());
			json.setCustomsLocationPostal(place.get().getEstablishment().getAddress().getPostalCode().substring(0,2));
		}
		else {
			json.setCustomsLocationCountry(Optional.ofNullable(customsPoint.getCustomsLocationCountry()).map(IsoNamePair::getIso).orElse(null));
			json.setCustomsLocationPostal(Optional.ofNullable(customsPoint.getCustomsLocationPostal()).map(IdNamePair::getName).orElse(null));
		}

		json.setClearanceType(customsPoint.getClearanceType().getCode());
		json.setClearanceResponsible(customsPoint.getClearanceResponsible().getCode());
		json.setCustomsOfficeId(Optional.ofNullable(customsPoint.getOffice()).orElseGet(IdNamePair::new).getId());

		if(isArrivalCustoms) {
			warehouse.ifPresent(l->json.setExternalCustomsLocationCode(l.getExternalIds()
					.parallelStream()
					.filter(q->Objects.equals(QUADRO, q.getExternalSystem()))
					.findFirst()
					.orElseGet(ExternalSystemIdJson::new)
					.getExternalId()));
		}
		Optional.ofNullable(customsPoint.getOffice()).map(this::retrieveCustomsOfficeExternalCode).ifPresent(json::setExternalCustomsOfficeCode);
		return json;
	}

	private String retrieveCustomsOfficeExternalCode(IdNamePair customsOffice) {
		if(Objects.isNull(customsOffice)) {
			return null;
		}
		try {
			return locationServiceClient.getCustomsOffice(customsOffice.getId())
					.getExternalIds()
					.parallelStream()
					.filter(q->Objects.equals(QUADRO, q.getExternalSystem()))
					.findFirst()
					.orElseGet(ExternalSystemIdJson::new)
					.getExternalId();
		} catch(Exception e) {
			LOG.warn(e.getMessage());
		}
		return null;
	}

	private PackageJson convertPackage(ekol.crm.quote.queue.common.dto.PackageJson quotePackage) {
		PackageJson json = new PackageJson();
		json.setType(quotePackage.getType());
		json.setQuantity(quotePackage.getQuantity());
		json.setDimension(convertDimension(quotePackage.getDimension()));
		json.setMeasurement(convertMeasurement(quotePackage.getMeasurement()));
		json.setStackabilityType(quotePackage.getStackabilityType());
		if (quotePackage.getHangingGoodsCategory() != null) {
			json.setCategory(quotePackage.getHangingGoodsCategory().getCode());
		} else {
			json.setCategory(null);
		}
		return json;
	}

	private ServiceTypeJson convertService(ekol.crm.quote.queue.common.dto.ServiceJson service) {
		ServiceTypeJson json = new ServiceTypeJson();
		json.setCode(service.getType().getCode());
		json.setName(service.getType().getName());
		json.setCategory(service.getType().getCategory());
		return json;
	}

	private LoadJson convertLoad(ekol.crm.quote.queue.common.dto.LoadJson load) {
		LoadJson json = new LoadJson();
		json.setType(Optional.ofNullable(load.getType()).map(CodeNamePair::getCode).orElse(null));
		json.setRiskFactor(load.getRiskFactor());
		json.setMinTemperature(load.getMinTemperature());
		json.setMaxTemperature(load.getMaxTemperature());
		return json;
	}

	private VehicleRequirementJson convertVehicleReqirement(ekol.crm.quote.queue.common.dto.VehicleRequirementJson vehicleRequirement) {
		VehicleRequirementJson json = new VehicleRequirementJson();
		json.setRequirement(Optional.ofNullable(vehicleRequirement.getRequirement()).map(CodeNamePair::getCode).orElse(null));
		json.setOperationType(Optional.ofNullable(vehicleRequirement.getOperationType()).map(CodeNamePair::getCode).orElse(null));
		return json;
	}

	private ContainerRequirementJson convertContainerRequirement(ekol.crm.quote.queue.common.dto.ContainerRequirementJson containerRequirement) {
		ContainerRequirementJson json = new ContainerRequirementJson();
		json.setVolume(Optional.ofNullable(containerRequirement.getVolume()).map(CodeNamePair::getCode).map(t->t.replace("'", "")).orElse(null));
		json.setType(Optional.ofNullable(containerRequirement.getType()).map(CodeNamePair::getCode).orElse(null));
		json.setQuantity(containerRequirement.getQuantity());
		return json;
	}

	private PriceJson convertPrice(ekol.crm.quote.queue.common.dto.PriceJson price) {
		PriceJson json = new PriceJson();
		json.setType(Optional.ofNullable(price.getType()).map(CodeNamePair::getCode).orElse(null));
		json.setBillingItem(Optional.ofNullable(price.getBillingItem()).map(BillingItemJson::getCode).orElse(null));
		json.setAddToFreight(price.isAddToFreight());
		json.setCharge(convertMonetaryAmount(price.getCharge()));
		return json;
	}


	private MonetaryAmountJson convertMonetaryAmount(ekol.crm.quote.queue.common.dto.MonetaryAmountJson monetaryAmount) {
		if(monetaryAmount == null){
			return null;
		}
		MonetaryAmountJson json = new MonetaryAmountJson();
		json.setAmount(monetaryAmount.getAmount());
		json.setCurrency(Optional.ofNullable(monetaryAmount.getCurrency()).map(CodeNamePair::getCode).orElse(null));
		return json;
	}

	private PaymentRuleJson convertPaymentRule(ekol.crm.quote.queue.common.dto.PaymentRuleJson paymentRule) {
		if(paymentRule == null){
			return null;
		}
		
		PaymentRuleJson json = new PaymentRuleJson();
		json.setInvoiceCompany(paymentRule.getInvoiceCompany());
		json.setInvoiceLocation(paymentRule.getInvoiceLocation());
		json.setType(Optional.ofNullable(paymentRule.getType()).map(CodeNamePair::getCode).orElse(null));
		json.setPaymentDueDays(paymentRule.getPaymentDueDays());
		if(Objects.nonNull(paymentRule.getOwnerCompany())){
			if(paymentRule.getOwnerCompany().getType().equals("AIRLINE")) {
				json.setAirlineCompanyCode(Optional.ofNullable(paymentRule.getOwnerCompany().getCode()).orElse(null));
			}
			if(paymentRule.getOwnerCompany().getType().equals("SHIPOWNER")) {
				json.setShipOwnerCompanyCode(Optional.ofNullable(paymentRule.getOwnerCompany().getCode()).orElse(null));
			}
		}
		return json;
	}

	private static String convertDateToString(LocalDate date){
		return Optional.ofNullable(date).map(localDate -> localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).orElseGet(null);
	}

}
