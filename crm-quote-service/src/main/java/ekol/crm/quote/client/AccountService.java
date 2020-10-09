package ekol.crm.quote.client;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.accountservice.*;
import ekol.crm.quote.domain.dto.validation.*;
import ekol.crm.quote.domain.enumaration.CountryPointType;
import ekol.crm.quote.domain.model.quote.SpotQuote;
import ekol.crm.quote.util.Utils;
import ekol.exceptions.ValidationException;

@Service
public class AccountService {

	@Value("${oneorder.crm-account-service}")
    private String accountServiceName;

	@Autowired
    private RestTemplate restTemplate;

    public Account findAccountById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, accountServiceName + "/account/{id}", Account.class, id);
    }

    @Cacheable("one-day-cache")
    public Country findCountry(String name, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, accountServiceName + "/lookup/country/byName?name={name}", Country.class, name);
    }

    @Cacheable("one-hour-cache")
    public CountryPoint findCountryPoint(String countryIso, CountryPointType type, String name, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, accountServiceName + "/lookup/country-point/byCountryAndTypeAndName?iso={countryIso}&type={type}&name={name}", CountryPoint.class, countryIso, type.name(), name);
    }
    @Cacheable("one-hour-cache")
    public CountryPoint findCountryPointByCountryName(String countryName, CountryPointType type, String name, boolean ignoreNotFoundException) {
    	return Utils.getForObject(ignoreNotFoundException, restTemplate, accountServiceName + "/lookup/country-point/byCountryNameAndTypeAndName?countryName={countryName}&type={type}&name={name}", CountryPoint.class, countryName, type.name(), name);
    }

    @Cacheable("one-day-cache")
    public List<ShipmentLoadingType> findShipmentLoadingTypes(String serviceAreaCode) {
    	return Optional.ofNullable(Utils.getForObject(true, restTemplate, accountServiceName + "/lookup/shipment-loading-type/{serviceArea}", ShipmentLoadingType[].class, serviceAreaCode)).map(Arrays::asList).orElseGet(ArrayList::new);
    }

    public ShipmentLoadingType findShipmentLoadingType(String code, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, accountServiceName + "/lookup/shipment-loading-type/byCode?code={code}", ShipmentLoadingType.class, code);
    }

    public List<Account> findAccountsByAccountOwner(String accountOwner) {
    	return Optional.ofNullable(restTemplate.getForObject(accountServiceName + "/account/byAccountOwner?accountOwner={accountOwner}", Account[].class, accountOwner)).map(Arrays::asList).orElseGet(ArrayList::new);
    }

    public boolean isCompanyContactLinkedToAccount(Long companyContactId, Long accountId) {
        return restTemplate.getForObject(accountServiceName + "/contact/is-company-contact-linked-to-account?companyContactId={companyContactId}&accountId={accountId}", Boolean.class, companyContactId, accountId).booleanValue();
    }
}
