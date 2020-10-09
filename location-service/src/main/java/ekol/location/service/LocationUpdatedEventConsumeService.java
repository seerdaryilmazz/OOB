package ekol.location.service;

import java.util.*;
import java.util.stream.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.location.client.KartoteksServiceClient;
import ekol.location.client.dto.*;
import ekol.location.domain.Country;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.location.comnon.*;
import ekol.location.domain.location.comnon.PhoneNumber;
import ekol.location.domain.location.comnon.PhoneNumberWithType;
import ekol.location.domain.location.comnon.Point;
import ekol.location.domain.location.customs.*;
import ekol.location.util.WarehouseRampManager;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LocationUpdatedEventConsumeService {
	
	private WarehouseService warehouseService;
	private CustomerWarehouseService customerWarehouseService;
	private KartoteksServiceClient kartoteksServiceClient;
	private CountryService countryService;
	private WarehouseRampManager warehouseRampManager;
	
	@Transactional
	public void consumeCustomsOffice(CustomsOffice customsOffice) {
		Optional.of(customsOffice).map(CustomsOffice::getLocations).map(Collection::stream).orElseGet(Stream::empty).forEach(location->
			Optional.ofNullable(customerWarehouseService.retrieveCustomerWarehouseByCompanyAndLocation(location.getId(), WarehouseCompanyType.CUSTOMS)).ifPresent(customerWarehouse->{
				customerWarehouse.setActive(location.isActive());
				customerWarehouse.setName(location.getName());
				customerWarehouse.getCompany().setName(customsOffice.getName());
				customerWarehouse.getCompanyLocation().setName(location.getName());
				customerWarehouse.setLocalName(location.getName());
				customerWarehouse.getEstablishment().setAddress(mapAddress(customerWarehouse.getEstablishment().getAddress(), location));
				customerWarehouse.getEstablishment().setPhoneNumbers(location.getPhoneNumbers());
				customerWarehouse.setLocation(this.mapLocation(customerWarehouse.getLocation(), location));
				customerWarehouseService.editCustomerWarehouse(customerWarehouse.getId(), customerWarehouse);
			})
		);
	}
	
	@Transactional
	public void consumeCompany(Long companyId) {
		Company company = kartoteksServiceClient.getCompany(companyId);
		Optional.ofNullable(company).map(Company::getCompanyLocations).map(Collection::stream).orElseGet(Stream::empty).forEach(location->{
			Optional.ofNullable(warehouseService.getWarehouseByCompanyLocation(location.getId())).ifPresent(warehouse->{
				warehouse.setActive(location.isActive());
				warehouse.setName(location.getName());
				warehouse.getCompany().setName(company.getName());
				warehouse.getCompanyLocation().setName(location.getName());
				warehouse.setLocalName(location.getName());
				warehouse.getEstablishment().setAddress(mapAddress(warehouse.getEstablishment().getAddress(), location.getPostaladdress()));
				warehouse.getEstablishment().setPhoneNumbers(Optional.of(location).map(CompanyLocation::getPhoneNumbers).map(Collection::stream).orElseGet(Stream::empty).map(this::mapPhone).collect(Collectors.toSet()));
				warehouse.setLocation(this.mapLocation(warehouse.getLocation(), location.getPostaladdress()));
				warehouseService.editWarehouse(warehouse.getId(), warehouseRampManager.groupRamps(warehouse));
			});
			Optional.ofNullable(customerWarehouseService.retrieveCustomerWarehouseByCompanyAndLocation(location.getId())).ifPresent(customerWarehouse->{
				customerWarehouse.setActive(location.isActive());
				customerWarehouse.setName(location.getName());
				customerWarehouse.getCompany().setName(company.getName());
				customerWarehouse.getCompanyLocation().setName(location.getName());
				customerWarehouse.setLocalName(location.getName());
				customerWarehouse.getEstablishment().setAddress(mapAddress(customerWarehouse.getEstablishment().getAddress(), location.getPostaladdress()));
				customerWarehouse.getEstablishment().setPhoneNumbers(Optional.of(location).map(CompanyLocation::getPhoneNumbers).map(Collection::stream).orElseGet(Stream::empty).map(this::mapPhone).collect(Collectors.toSet()));
				customerWarehouse.setLocation(this.mapLocation(customerWarehouse.getLocation(), location.getPostaladdress()));
				customerWarehouseService.editCustomerWarehouse(customerWarehouse.getId(), customerWarehouse);
			});
		});
	}
	
	private Location mapLocation(Location location, CustomsOfficeLocation customsOfficeLocation) {
		if(Objects.nonNull(location)) {
			location.setPointOnMap(customsOfficeLocation.getPointOnMap());
			location.setGooglePlaceId(customsOfficeLocation.getGooglePlaceId());
			location.setGooglePlaceUrl(customsOfficeLocation.getGooglePlaceUrl());
			location.setTimezone(customsOfficeLocation.getTimezone());
			location.setPolygonPath(Collections.emptySet());
		}
		return location;
	}
	
	private Location mapLocation(Location location, ekol.location.client.dto.PostalAddress postaladdress) {
		if(Objects.nonNull(location)) {
			Optional.ofNullable(postaladdress.getPointOnMap()).map(p->new Point(p.getLat(), p.getLng())).ifPresent(location::setPointOnMap);
			location.setGooglePlaceId(postaladdress.getGooglePlaceId());
			location.setGooglePlaceUrl(postaladdress.getGooglePlaceUrl());
			location.setTimezone(postaladdress.getTimezone());
			location.setPolygonPath(Collections.emptySet());
		}
		return location;
	}
	
	private Address mapAddress(Address address, CustomsOfficeLocation customsOfficeLocation) {
		if(Objects.nonNull(address)) {
			address.setCountry(customsOfficeLocation.getCountry());
			address.setPostalCode(customsOfficeLocation.getPostalCode());
			address.setStreetAddress(customsOfficeLocation.getAddress());
		}
		return address;
	}
	
	private Address mapAddress(Address address, ekol.location.client.dto.PostalAddress postaladdress) {
		if(Objects.nonNull(address)) {
			Country country = countryService.findByIsoOrThrowResourceNotFoundException(postaladdress.getCountry().getIso());
			address.setCity(postaladdress.getCity());
			address.setCountry(country);
			address.setDistrict(postaladdress.getDistrict());
			address.setFormattedAddress(postaladdress.getFormattedAddress());
			address.setPostalCode(postaladdress.getPostalCode());
			address.setRegion(postaladdress.getRegion());
			address.setStreetAddress(postaladdress.getFormattedAddress());
		}
		return address;
	}
	
	private PhoneNumberWithType mapPhone(ekol.location.client.dto.PhoneNumberWithType locationPhone) {
		PhoneNumberWithType phone = new PhoneNumberWithType();
		phone.setDefault(locationPhone.isDefault());
		phone.setPhoneNumber(new PhoneNumber(locationPhone.getPhoneNumber().getCountryCode(), locationPhone.getPhoneNumber().getRegionCode(), locationPhone.getPhoneNumber().getPhone(), locationPhone.getPhoneNumber().getExtension()));
		phone.setPhoneType(locationPhone.getNumberType().getCode());
		return phone;
	}
}
