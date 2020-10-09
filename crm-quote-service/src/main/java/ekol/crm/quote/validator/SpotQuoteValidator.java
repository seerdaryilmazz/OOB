package ekol.crm.quote.validator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import ekol.crm.quote.common.Constants;
import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.domain.enumaration.ServiceTypeCategory;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.product.SpotProduct;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;

@Component
public class SpotQuoteValidator extends AbstractQuoteValidator {

	@Override
	protected void validateQuote(Quote quote) {
		SpotQuote spotQuote = SpotQuote.class.cast(quote);

		if (Objects.equals(quote.getServiceArea(), "ROAD")) {
			Optional<Measurement> measurement = Optional.ofNullable(spotQuote.getMeasurement());
			BigDecimal ldm = measurement.map(Measurement::getLdm).orElse(null);
			BigDecimal weight = measurement.map(Measurement::getWeight).orElse(null);
			if (Objects.equals("FTL", quote.getProducts().iterator().next().getShipmentLoadingType())) {
				BigDecimal expectedLdm = getExpectedLdm(spotQuote);
				if (ldm == null || ldm.compareTo(expectedLdm) != 0) {
					throw new ValidationException("Total LDM value must be equal to {0}", expectedLdm);
				}
			}
			if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
				throw new ValidationException("Total weight value must be entered");
			}
		}

		if (spotQuote.getContact() == null || spotQuote.getContact().getId() == null) {
			throw new ValidationException("Quote should have a contact");
		}

		if (!getAccountService().isCompanyContactLinkedToAccount(spotQuote.getContact().getId(), spotQuote.getAccount().getId())) {
			throw new ValidationException("The contact is not active for account company. Please select another contact for the quote!");
		}
		if (!getKartoteksService().isContactLinkedToLocation(spotQuote.getContact().getId(), spotQuote.getAccountLocation().getId())) {
			throw new ValidationException("The contact is not active for account location. Please select another contact for the quote!");
		}
		if (!getKartoteksService().isContactActive(spotQuote.getContact().getId())) {
			throw new ValidationException("The contact is not active for account company. Please select another contact for the quote!");
		}
	}
	
	@Override
	protected void ensureLocationBelongToCompany(Quote quote) {
		super.ensureLocationBelongToCompany(quote);
		PaymentRule paymentRule = SpotQuote.class.cast(quote).getPaymentRule();
		
		Company company = Optional.ofNullable(getKartoteksService().findCompanyById(paymentRule.getInvoiceCompany().getId(), true))
				.orElseThrow(()->new ValidationException("{0} named invoice company is not exists", paymentRule.getInvoiceCompany().getName()));
		if(company.getCompanyLocations().parallelStream().map(CompanyLocation::getId).noneMatch(paymentRule.getInvoiceLocation().getId()::equals)) {
			throw new ValidationException("{0} named invoice location does not belong to invoice company", paymentRule.getInvoiceLocation().getName());
		}
	}
	
	@Override
	protected Map<Long, String> extractCompaniesId(Quote quote) {
		Map<Long, String> ids = new HashMap<>();
		
		Optional<SpotQuote> spotQuote = Optional.of(quote)
				.map(SpotQuote.class::cast);
		
		Optional<SpotProduct> spotProduct = spotQuote
				.map(SpotQuote::getProducts)
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.findFirst()
				.map(SpotProduct.class::cast);

		spotQuote.map(Quote::getAccountLocation).map(IdNamePair::getId).ifPresent(id->ids.put(id, "account"));
		spotQuote.map(SpotQuote::getPaymentRule).map(PaymentRule::getInvoiceLocation).map(IdNamePair::getId).ifPresent(id->ids.put(id, "invoice"));
		spotProduct.map(SpotProduct::getLoadingLocation).map(IdNamePair::getId).ifPresent(id->ids.put(id, "loading"));
		spotProduct.map(SpotProduct::getDeliveryLocation).map(IdNamePair::getId).ifPresent(id->ids.put(id, "delivery"));
		return ids;
	}

	private BigDecimal getExpectedLdm(SpotQuote spotQuote) {
		String serviceType = Optional.ofNullable(getMainService(spotQuote)).map(Service::getType).map(ServiceType::getCode).orElse(null);
		if(Objects.equals("SPEEDY", serviceType)) {
			return Constants.FTL_TOTAL_LDM_SPEEDY;
		} else if(Objects.equals("SPEEDY_VAN", serviceType)){
			return Constants.FTL_TOTAL_LDM_SPEEDY_VAN;
		}
		return Constants.FTL_TOTAL_LDM;
	}

	private Service getMainService(SpotQuote spotQuote) {
		return Optional.ofNullable(spotQuote.getServices())
				.map(Collection::stream)
				.orElseGet(Stream::empty)
				.parallel()
				.filter(service->Objects.equals(service.getType().getCategory(), ServiceTypeCategory.MAIN))
				.findFirst()
				.orElse(null);
	}

}
