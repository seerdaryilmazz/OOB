package ekol.orders.search.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;
import ekol.model.CodeNamePair;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.order.domain.Appointment;
import ekol.orders.order.domain.CompanyType;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.ShipmentHandlingParty;
import ekol.orders.order.domain.dto.response.kartoteks.CompanyResponse;
import ekol.orders.order.domain.dto.response.kartoteks.Country;
import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import ekol.orders.order.domain.dto.response.kartoteks.PostalAddress;
import ekol.orders.order.domain.dto.response.location.CustomsOfficeLocationResponse;
import ekol.orders.order.domain.dto.response.location.CustomsOfficeResponse;
import ekol.orders.order.service.KartoteksServiceClient;
import ekol.orders.order.service.LocationServiceClient;
import ekol.orders.search.domain.DateTimeDocument;
import ekol.orders.search.domain.DateTimeWindowDocument;
import ekol.orders.search.domain.HandlingPartySearchDocument;
import ekol.orders.search.domain.ShipmentSearchDocument;
import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class ShipmentIndexDocumentConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentIndexDocumentConverter.class);

	private KartoteksServiceClient kartoteksServiceClient;
	private LocationServiceClient locationService;

	public ShipmentSearchDocument convert(OrderShipment shipment){
		ShipmentSearchDocument document = new ShipmentSearchDocument();
		try {
			document.setId(shipment.getId().toString());
			document.setShipmentCode(shipment.getCode());
			document.setCustomerId(shipment.getOrder().getCustomer().getId());
			document.setCustomerName(nameOrShortName(shipment.getOrder().getCustomer(), CompanyType.COMPANY));
			if(shipment.getOrder().getOriginalCustomer() != null){
				document.setOriginalCustomerId(shipment.getOrder().getOriginalCustomer().getId());
				document.setOriginalCustomerName(nameOrShortName(shipment.getOrder().getOriginalCustomer(), CompanyType.COMPANY));
			}

			document.setTemplateId(shipment.getOrder().getTemplateId());
			document.setOrderId(shipment.getOrder().getId());
			document.setOrderCode(shipment.getOrder().getCode());
			document.setSubsidiary(shipment.getOrder().getSubsidiary().toIdNamePair());
			document.setTruckLoadType(shipment.getOrder().getTruckLoadType().name());
			document.setServiceType(shipment.getOrder().getServiceType().name());
			document.setStatus(shipment.getOrder().getStatus().name());

			document.setReadyDate(DateTimeDocument.withFixedZoneDateTime(shipment.getReadyAtDate()));
			document.setDeliveryDate(DateTimeDocument.withFixedZoneDateTime(shipment.getDeliveryDate()));

			document.setLoadingAppointment(convert(shipment.getLoadingAppointment()));
			document.setUnloadingAppointment(convert(shipment.getUnloadingAppointment()));

			document.setReadyDateOrLoadingAppointment(pick(shipment.getReadyAtDate(), shipment.getLoadingAppointment()));
			document.setDeliveryDateOrUnloadingAppointment(pick(shipment.getDeliveryDate(), shipment.getUnloadingAppointment()));

			document.setSender(convert(shipment.getSender()));
			document.setConsignee(convert(shipment.getConsignee()));

			document.setPackageCount(shipment.getTotalQuantity());
			document.setPackageType(Optional.ofNullable(shipment.getPackageTypes()).orElseGet(HashSet::new).size() > 1 ? "Package" : shipment.getPackageTypes().parallelStream().map(PackageType::getName).findFirst().orElse(null));
			document.setGrossWeight(Objects.isNull(shipment.getGrossWeight())?null: shipment.getGrossWeight().doubleValue());
			document.setVolume(Objects.isNull(shipment.getTotalVolume())?null: shipment.getTotalVolume().doubleValue());
			document.setLdm(Objects.isNull(shipment.getTotalLdm())?null: shipment.getTotalLdm().doubleValue());
			document.setPayWeight(Objects.isNull(shipment.getPayWeight())?null: shipment.getPayWeight().doubleValue());
			if(!CollectionUtils.isEmpty(shipment.getMappedIds())) {
				document.setApplicationIds(shipment.getMappedIds().stream().map(t->CodeNamePair.with(t.getApplicationOrderShipmentId(), t.getApplication())).collect(Collectors.toList()));
			}

			if(!CollectionUtils.isEmpty(shipment.getAdrDetails())) {
				document.getSpecial().add("adr");
			}
			if(!CollectionUtils.isEmpty(shipment.getHealthCertificateTypes())) {
				document.getSpecial().add("certificated");
			}
			if(!(Objects.isNull(shipment.getTemperatureMinValue()) && Objects.isNull(shipment.getTemperatureMaxValue()))) {
				document.getSpecial().add("temperature");
			}
			if(shipment.isHangingLoad()) {
				document.getSpecial().add("hangingLoad");
			}
			if(shipment.isLongLoad()) {
				document.getSpecial().add("longLoad");
			}
			if(shipment.isOversizeLoad()) {
				document.getSpecial().add("oversizeLoad");
			}
			if(shipment.isHeavyLoad()) {
				document.getSpecial().add("heavyLoad");
			}
			if(shipment.isValuableLoad()) {
				document.getSpecial().add("valuableLoad");
			}
			return document;
		} catch(Exception e) {
			LOG.error(e.getMessage());
		}
		return null;
	}
	
	private DateTimeDocument pick(FixedZoneDateTime datetime, Appointment appointment) {
		if(Objects.isNull(appointment)) {
			return DateTimeDocument.withFixedZoneDateTime(datetime);
		}
		return DateTimeDocument.withFixedZoneDateTime(appointment.getStartDateTime());
	}

	private DateTimeWindowDocument convert(Appointment appointment) {
		if(Objects.isNull(appointment)){
			return null;
		}
		return DateTimeWindowDocument.fromFixedZoneDateTimeWindow(new FixedZoneDateTimeWindow(appointment.getStartDateTime(), appointment.getEndDateTime()));
	}
	
	private String nameOrShortName(IdNameEmbeddable company, CompanyType companyType) {
		if(CompanyType.COMPANY == companyType) {
			return Optional.ofNullable(company).map(IdNameEmbeddable::getId).filter(kartoteksServiceClient::isCompanyExists).map(kartoteksServiceClient::getCompany).map(CompanyResponse::getShortName).orElse(company.getName());
		} else if(CompanyType.CUSTOMS == companyType) {
			return Optional.ofNullable(company).map(IdNameEmbeddable::getId).filter(locationService::isCustomsOfficeExists).map(locationService::getCustomsOffice).map(CustomsOfficeResponse::getShortName).orElse(company.getName());
		}
		return company.getName();
		
	}
	
	private HandlingPartySearchDocument convert(ShipmentHandlingParty handlingParty){
		HandlingPartySearchDocument document = new HandlingPartySearchDocument();
		if(!Objects.isNull(handlingParty.getCompany())) {
			document.setCompanyId(handlingParty.getCompany().getId());
			document.setCompanyName(nameOrShortName(handlingParty.getCompany(), CompanyType.COMPANY));
		}
		if(!Objects.isNull(handlingParty.getHandlingCompany())) {
			document.setHandlingCompanyId(handlingParty.getHandlingCompany().getId());
			document.setHandlingCompanyName(nameOrShortName(handlingParty.getHandlingCompany(), handlingParty.getHandlingCompanyType()));
		}
		if(!Objects.isNull(handlingParty.getHandlingOperationRegion())) {
			document.setHandlingOperationRegion(handlingParty.getHandlingOperationRegion().toIdNamePair());
		}
		if(!Objects.isNull(handlingParty.getHandlingRegion())) {
			document.setHandlingRegion(handlingParty.getHandlingRegion().toIdNamePair());
			document.setHandlingRegionCategory(handlingParty.getHandlingRegionCategory().toCodeNamePair());
		}

		if(!Objects.isNull(handlingParty.getCompanyLocation())) {
			document.setCompanyLocationId(handlingParty.getCompanyLocation().getId());
			document.setCompanyLocationName(handlingParty.getCompanyLocation().getName());
		}
		
		if(!Objects.isNull(handlingParty.getHandlingLocation())) {
			document.setHandlingLocationId(handlingParty.getHandlingLocation().getId());
			document.setHandlingLocationName(handlingParty.getHandlingLocation().getName());
			
			if(CompanyType.COMPANY == handlingParty.getHandlingCompanyType()) {
				Optional<LocationResponse> locationResponse = Optional.of(handlingParty.getHandlingLocation()).map(IdNameEmbeddable::getId).filter(kartoteksServiceClient::isLocationExists).map(kartoteksServiceClient::getLocation);
				document.setHandlingLocationCountryCode(locationResponse.map(LocationResponse::getPostaladdress).map(PostalAddress::getCountry).map(Country::getIso).orElse(null));
				document.setHandlingLocationPostalCode(locationResponse.map(LocationResponse::getPostaladdress).map(PostalAddress::getPostalCode).orElse(null));
				document.setHandlingLocationTimezone(locationResponse.map(LocationResponse::getTimezone).orElse(null));
				document.setHandlingLocationPointOnMap(locationResponse	.map(LocationResponse::getPostaladdress)
																		.map(PostalAddress::getPointOnMap)
																		.filter(Objects::nonNull)
																		.map(point-> new GeoPoint(	Optional.ofNullable(point.getLat())
																											.orElse(BigDecimal.ZERO)
																											.doubleValue(),
																									Optional.ofNullable(point.getLng())
																											.orElse(BigDecimal.ZERO)
																											.doubleValue())).orElse(null));
			}else if(CompanyType.CUSTOMS == handlingParty.getHandlingCompanyType()) {
				Optional<CustomsOfficeLocationResponse> locationResponse = Optional.of(handlingParty.getHandlingLocation()).map(IdNameEmbeddable::getId).filter(locationService::isCustomsOfficeLocationExists).map(locationService::getCustomsOfficeLocation);
				document.setHandlingLocationCountryCode(locationResponse.map(CustomsOfficeLocationResponse::getCountry).map(Country::getIso).orElse(null));
				document.setHandlingLocationPostalCode(locationResponse.map(CustomsOfficeLocationResponse::getPostalCode).orElse(null));
				document.setHandlingLocationTimezone(locationResponse.map(CustomsOfficeLocationResponse::getTimezone).orElse(null));
				document.setHandlingLocationPointOnMap(locationResponse.map(CustomsOfficeLocationResponse::getPointOnMap)
																		.filter(Objects::nonNull)
																		.map(point-> new GeoPoint(	Optional.ofNullable(point.getLat())
																											.orElse(BigDecimal.ZERO)
																											.doubleValue(),
																									Optional.ofNullable(point.getLng())
																											.orElse(BigDecimal.ZERO)
																											.doubleValue())).orElse(null));
			}
		}
		return document;
	}
}
