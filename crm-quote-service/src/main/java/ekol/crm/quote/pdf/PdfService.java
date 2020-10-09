package ekol.crm.quote.pdf;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.itextpdf.text.PageSize;

import ekol.crm.quote.client.*;
import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.Package;
import ekol.crm.quote.domain.model.product.SpotProduct;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.repository.SpotQuotePdfSettingRepository;
import ekol.crm.quote.service.*;
import ekol.crm.quote.util.*;
import ekol.exceptions.*;
import ekol.json.serializers.common.ConverterType;
import ekol.model.*;
import ekol.notification.api.NotificationApi;
import freemarker.template.*;

@Service
public class PdfService {

	private static final String SPOT_QUOTE_TEMPLATE_NAME = "spotQuotePdfTemplate.ftl";
	private static final String SPOT_QUOTE_TEMPLATE_NAME_MAIL = "spotQuoteMailTemplate.ftl";

	private static final String TENDER_QUOTE_TEMPLATE_NAME_MAIL = "tenderQuoteMailTemplate.ftl";
	private static final String FONT_DIRECTORY = "spotQuotePdfResources/fonts"; // resources dizininde ...
	private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private Path fileRepoPath;
	
	private Configuration freeMarkerConfiguration;
	private SpotQuotePdfSettingRepository spotQuotePdfSettingRepository;
	private TranslatorService translatorService;
	private KartoteksService kartoteksService;
	private UserService userService;
	private ExchangeRateService exchangeRateService;
	private AuthorizationService authorizationService;
	private NoteService noteService;
	private NoteCrudService noteCrudService;
	private BillingItemService billingItemService;
	private Environment environment;
	
	@Autowired
	public PdfService(
			@Value("${oneorder.fileRepoPath}") String fileRepoPathString,
			Configuration freeMarkerConfiguration, 
			SpotQuotePdfSettingRepository spotQuotePdfSettingRepository,
			TranslatorService translatorService, 
			KartoteksService kartoteksService, 
			UserService userService,
			ExchangeRateService exchangeRateService, 
			AuthorizationService authorizationService, 
			NoteService noteService,
			NoteCrudService noteCrudService, 
			BillingItemService billingItemService,
			Environment environment) {

		
		if (StringUtils.isEmpty(fileRepoPathString)) {
			throw new ApplicationException("Property is not found: oneorder.fileRepoPath");
		}

		fileRepoPath = Paths.get(fileRepoPathString);
		if (!fileRepoPath.toFile().exists() || !fileRepoPath.toFile().isDirectory()) {
			throw new ApplicationException("Directory does not exist: " + fileRepoPathString);
		}

		this.freeMarkerConfiguration = freeMarkerConfiguration;
		this.spotQuotePdfSettingRepository = spotQuotePdfSettingRepository;
		this.translatorService = translatorService;
		this.kartoteksService = kartoteksService;
		this.userService = userService;
		this.exchangeRateService = exchangeRateService;
		this.authorizationService = authorizationService;
		this.noteService = noteService;
		this.noteCrudService = noteCrudService;
		this.billingItemService = billingItemService;
		this.environment = environment;
	}

	private SpotQuotePdfSetting getPdfSetting(Long subsidiaryId, String serviceArea, Long languageId) {
		Optional<SpotQuotePdfSetting> optionalPdfSetting = spotQuotePdfSettingRepository.findBySubsidiaryIdAndServiceAreaAndLanguageId(subsidiaryId, serviceArea, languageId);
		if (!optionalPdfSetting.isPresent()) {
			throw new BadRequestException("There is no pdf setting for specified subsidiary, service area and language.");
		}
		return optionalPdfSetting.get();
	}

	private IsoNamePair findSubsidiaryCountry(SpotQuote quote) {
		Country country = authorizationService.findCountryOfSubsidiary(quote.getSubsidiary().getId());
		return new IsoNamePair(country.getIso(), country.getCountryName());
	}

	private boolean isImport(IsoNamePair subsidiaryCountry, IsoNamePair fromCountry, IsoNamePair toCountry) {

		if (fromCountry.getIso().equals(toCountry.getIso())) {
			return false;
		} else {
			return !subsidiaryCountry.getIso().equals(fromCountry.getIso());
		}
	}

	private String translate(String languageCode, String str) {
		if ("en".equalsIgnoreCase(languageCode)) {
			return str;
		}
		if(StringUtils.isBlank(str)) {
			return StringUtils.EMPTY;
		}
		return Optional.ofNullable(translatorService.translate("crm", str, languageCode)).filter(StringUtils::isNotBlank).orElse(StringUtils.EMPTY);
	}

	private String getCountryDisplayString(IsoNamePair country) {
		return country.getName() + " - " + country.getIso();
	}

	private String getLoadingAddressLabel(String languageCode, SpotQuote quote) {
		if (quote.getServiceArea().equals("ROAD") || quote.getServiceArea().equals("DTR")) {
			return translate(languageCode, "Loading Address");
		} else if (quote.getServiceArea().equals("SEA")) {
			return translate(languageCode, "Departure Port");
		} else if (quote.getServiceArea().equals("AIR")) {
			return translate(languageCode, "Departure Airport");
		} else {
			throw new ApplicationException("No implementation for {0}", quote.getServiceArea());
		}
	}

	private String getLoadingAddressDisplayString(SpotQuote quote, SpotProduct product) {

		if (quote.getServiceArea().equals("ROAD") || quote.getServiceArea().equals("DTR")) {

			LoadingType loadingType = product.getLoadingType();
			if (loadingType.equals(LoadingType.CUSTOMER_ADDRESS)) {
				IsoNamePair fromCountry = product.getFromCountry();
				return getCountryDisplayString(fromCountry) + " - " + product.getFromPoint().getName();
			} else if (loadingType.equals(LoadingType.EKOL_CROSS_DOCK) || loadingType.equals(LoadingType.PARTNER_CROSS_DOCK)) {
				if (product.getLoadingLocation() != null) {
					CompanyLocation companyLocation = kartoteksService.findLocationById(product.getLoadingLocation().getId(), true);
					if (companyLocation == null || !companyLocation.getActive()) {
						throw new BadRequestException("Loading location is deleted or not active.");
					}
					return companyLocation.getName() + " (" + companyLocation.getPostaladdress().getFormattedAddress() + ")";
				} else {
					return "";
				}
			} else {
				throw new ApplicationException("No implementation for {0}", loadingType.name());
			}

		} else if (quote.getServiceArea().equals("SEA") || quote.getServiceArea().equals("AIR")) {

			return product.getFromPoint().getName();

		} else {
			throw new ApplicationException("No implementation for {0}", quote.getServiceArea());
		}
	}

	private String getDeliveryAddressLabel(String languageCode, SpotQuote quote) {
		if (quote.getServiceArea().equals("ROAD") || quote.getServiceArea().equals("DTR")) {
			return translate(languageCode, "Delivery Address");
		} else if (quote.getServiceArea().equals("SEA")) {
			return translate(languageCode, "Arrival Port");
		} else if (quote.getServiceArea().equals("AIR")) {
			return translate(languageCode, "Arrival Airport");
		} else {
			throw new ApplicationException("No implementation for {0}", quote.getServiceArea());
		}
	}

	private String getDeliveryAddressDisplayString(SpotQuote quote, SpotProduct product) {

		if (quote.getServiceArea().equals("ROAD") || quote.getServiceArea().equals("DTR")) {

			DeliveryType deliveryType = product.getDeliveryType();
			if (deliveryType.equals(DeliveryType.CUSTOMER_ADDRESS)) {
				IsoNamePair toCountry = product.getToCountry();
				return getCountryDisplayString(toCountry) + " - " + product.getToPoint().getName();
			} else if (deliveryType.equals(DeliveryType.EKOL_CROSS_DOCK) || deliveryType.equals(DeliveryType.PARTNER_CROSS_DOCK)) {
				if (product.getDeliveryLocation() != null) {
					CompanyLocation companyLocation = kartoteksService.findLocationById(product.getDeliveryLocation().getId(), true);
					if (companyLocation == null || !companyLocation.getActive()) {
						throw new BadRequestException("Delivery location is deleted or not active.");
					}
					return companyLocation.getName() + " (" + companyLocation.getPostaladdress().getFormattedAddress() + ")";
				} else {
					return "";
				}
			} else if (deliveryType.equals(DeliveryType.CUSTOMS_ADDRESS)) {
				if (quote.getCustoms().getArrival().getLocation() != null) {
					return quote.getCustoms().getArrival().getLocation().getName();
				} else {
					return "";
				}
			} else {
				throw new ApplicationException("No implementation for {0}", deliveryType.name());
			}

		} else if (quote.getServiceArea().equals("SEA") || quote.getServiceArea().equals("AIR")) {

			return product.getToPoint().getName();

		} else {
			throw new ApplicationException("No implementation for {0}", quote.getServiceArea());
		}
	}

	private String getIncotermDisplayString(String languageCode, SpotProduct product) {
		if (StringUtils.isNotBlank(product.getIncotermExplanation())) {
			return product.getIncoterm() + " - " + translate(languageCode, product.getIncotermExplanation());
		} else {
			return product.getIncoterm();
		}
	}

	private String getDimensionDisplayString(Dimension d) {
		String width = (d != null && d.getWidth() != null) ? d.getWidth().toString() : "-";
		String length = (d != null && d.getLength() != null) ? d.getLength().toString() : "-";
		String height = (d != null && d.getHeight() != null) ? d.getHeight().toString() : "-";
		return width + "x" + length + "x" + height;
	}

	private String getWeightDisplayString(Measurement m) {
		if (m != null && m.getWeight() != null) {
			return m.getWeight().toString() + " kg";
		} else {
			return "";
		}
	}

	private String getLoadingMeterDisplayString(Measurement m) {
		if (m != null && m.getLdm() != null) {
			return m.getLdm().toString();
		} else {
			return "";
		}
	}

	private String getVolumeDisplayString(Measurement m) {
		if (m != null && m.getVolume() != null) {
			return m.getVolume().toString() + " m3";
		} else {
			return "";
		}
	}

	private String getPackageDetailsDisplayString(String languageCode, SpotQuote quote) {
		List<String> lines = new ArrayList<>();
		for (Package p : quote.getPackages()) {
			String line = p.getQuantity() + " " + p.getType() + ", " + getDimensionDisplayString(p.getDimension());
			if (StringUtils.isNotBlank(p.getStackabilityType())) {
				boolean isContentNumber = (NumberUtils.toInt(p.getStackabilityType(), -1) != -1);
				if (isContentNumber) {
					line += ", " + p.getStackabilityType();
				} else {
					line += ", " + translate(languageCode, p.getStackabilityType());
				}
			} else {
				line += ", -";
			}
			lines.add(line);
		}
		return StringUtils.join(lines, "<br/>");
	}

	private String getVehicleRequirementsDisplayString(String languageCode, SpotQuote quote) {

		List<String> requiredForLoading = new ArrayList<>();
		List<String> requiredForUnloading = new ArrayList<>();

		for (VehicleRequirement vr : quote.getVehicleRequirements()) {
			if (vr.getOperationType().getCode().equals("COLLECTION")) {
				requiredForLoading.add(translate(languageCode, vr.getRequirement().getName()));
			} else if (vr.getOperationType().getCode().equals("DISTRIBUTION")) {
				requiredForUnloading.add(translate(languageCode, vr.getRequirement().getName()));
			} else {
				throw new ApplicationException("No implementation for {0}", vr.getOperationType().getCode());
			}
		}

		List<String> lines = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(requiredForLoading)) {
			lines.add(translate(languageCode, "Required For Loading") + ": " + StringUtils.join(requiredForLoading, ", "));
		}

		if (CollectionUtils.isNotEmpty(requiredForUnloading)) {
			lines.add(translate(languageCode, "Required For Unloading") + ": " + StringUtils.join(requiredForUnloading, ", "));
		}

		return StringUtils.join(lines, "<br/>");
	}

	private String getContainerRequirementsDisplayString(String languageCode, SpotQuote quote) {
		List<String> lines = new ArrayList<>();
		for (ContainerRequirement cr : quote.getContainerRequirements()) {
			lines.add(cr.getQuantity() + " x " + cr.getVolume().getName() + " " + translate(languageCode, cr.getType().getName()));
		}
		return StringUtils.join(lines, "<br/>");
	}

	private String getServiceTypesDisplayString(SpotQuote quote, String languageCode) {
		List<String> lines = new ArrayList<>();
		for (ekol.crm.quote.domain.model.Service s : quote.getServices()) {
			if (s.getType().getCategory().equals("MAIN")) {
				lines.add(translate(languageCode, s.getType().getName()));
			}
		}
		return StringUtils.join(lines, "<br/>");
	}

	private String getDepartureCustomsOfficeDisplayString(SpotQuote quote) {
		if (quote.getCustoms() != null && quote.getCustoms().getDeparture() != null && quote.getCustoms().getDeparture().getOffice() != null) {
			return quote.getCustoms().getDeparture().getOffice().getName();
		} else {
			return "";
		}
	}

	private String getArrivalCustomsOfficeDisplayString(SpotQuote quote) {
		if (quote.getCustoms() != null && quote.getCustoms().getArrival() != null && quote.getCustoms().getArrival().getOffice() != null) {
			return quote.getCustoms().getArrival().getOffice().getName();
		} else {
			return "";
		}
	}

	private String getArrivalCustomsClearanceTypeDisplayString(String languageCode, SpotQuote quote) {
		if (quote.getCustoms() != null && quote.getCustoms().getArrival() != null && quote.getCustoms().getArrival().getClearanceType() != null) {
			return translate(languageCode, quote.getCustoms().getArrival().getClearanceType().getName());
		} else {
			return "";
		}
	}

	private String getArrivalCustomsLocationDisplayString(SpotQuote quote) {
		if (quote.getCustoms() != null && quote.getCustoms().getArrival() != null && quote.getCustoms().getArrival().getLocation() != null) {
			return quote.getCustoms().getArrival().getLocation().getName();
		} else {
			return "";
		}
	}

    private String setGreeting(SpotQuote quote, String languageCode) {
        String greeting = "Dear";
        String lang = StringUtils.split(languageCode, "_")[0];
        if ("es".equalsIgnoreCase(lang)) {
            CompanyContact contact = kartoteksService.findContactById(quote.getContact().getId(), false);
            if ("MALE".equals(contact.getGender().getCode())) {
                greeting = "Estimado";
            } else if ("FEMALE".equals(contact.getGender().getCode())) {
                greeting = "Estimada";
            }
            return greeting + " " + quote.getContact().getName() + ",";
        }
        return translate(languageCode, greeting) + " " + quote.getContact().getName() + ",";
    }


	public File createPdfForSpotQuote(SpotQuote quote, Long languageId) {

		LocalDate currentDate = LocalDate.now();
		SpotQuotePdfSetting pdfSetting = getPdfSetting(quote.getSubsidiary().getId(), quote.getServiceArea(), languageId);
		SpotProduct product = (SpotProduct) quote.getProducts().iterator().next(); // Her zaman 1 adet kayıt olduğundan ilkini alıyoruz.
		IsoNamePair subsidiaryCountry = findSubsidiaryCountry(quote);
		IsoNamePair fromCountry = product.getFromCountry();
		IsoNamePair toCountry = product.getToCountry();
		
		boolean isImport = isImport(subsidiaryCountry, fromCountry, toCountry);
		String languageCode = pdfSetting.getLanguage().getIsoCode();
		String lang = StringUtils.split(languageCode, "_")[0];
		boolean isRoad = quote.getServiceArea().equals("ROAD");
		boolean isSea = quote.getServiceArea().equals("SEA");
		boolean isDomestic = quote.getServiceArea().equals("DTR");
		boolean isAir = quote.getServiceArea().equals("AIR");

		CompanyLocation accountLocation = kartoteksService.findLocationById(quote.getAccountLocation().getId(), true);
		if (accountLocation == null || !accountLocation.getActive()) {
			throw new BadRequestException("Account location is deleted or not active.");
		}

		ModelForSpotQuotePdfTemplate model = new ModelForSpotQuotePdfTemplate();
		model.setLanguage(lang);
		model.setSubsidiaryName(quote.getSubsidiary().getName());

		model.setServiceArea(quote.getServiceArea());

		model.setAccount(quote.getAccount().getName());
		model.setAccountLocation(accountLocation.getPostaladdress().getFormattedAddress());

		model.setQuoteNumberLabel(translate(languageCode, "Our Reference"));
		model.setQuoteNumber(quote.getNumber().toString());

		model.setCreatedByLabel(translate(languageCode, "Your Contact"));
		model.setCreatedBy(userService.findUser(quote.getCreatedBy()).getDisplayName());
		
		model.setCreationDateLabel(translate(languageCode, "Date"));
		model.setCreationDate(currentDate.format(DEFAULT_DATE_FORMATTER));

		model.setValidityStartDateLabel(translate(languageCode, "Validity Start Date"));
		model.setValidityStartDate(quote.getValidityStartDate().format(DEFAULT_DATE_FORMATTER));

		model.setDetailsTableLabel(translate(languageCode, "Quote Details"));

		model.setDetailsTableRows(new ArrayList<>());

		Boolean hasDifferentBackgroundColor = Boolean.TRUE;
		String label = translate(languageCode, "Properties");
		String value = "";
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		if (!isDomestic) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Departure");
			value = getCountryDisplayString(fromCountry);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Arrival");
			value = getCountryDisplayString(toCountry);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}
		
		hasDifferentBackgroundColor = Boolean.FALSE;
		label = getLoadingAddressLabel(languageCode, quote);
		value = getLoadingAddressDisplayString(quote, product);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		
		hasDifferentBackgroundColor = Boolean.FALSE;
		label = getDeliveryAddressLabel(languageCode, quote);
		value = getDeliveryAddressDisplayString(quote, product);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		if (isRoad || isDomestic || isSea || isAir) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Loading Type");
			String loadingType = Optional.ofNullable(product.getLoadingType()).map(LoadingType::getName).orElse(StringUtils.EMPTY);
			value = translate(languageCode, loadingType);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}
		
		if (isSea || isAir) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Loading Address");
			String countryPoint = Optional.ofNullable(product.getLoadingCountryPoint()).map(IdNamePair::getName).map(" - "::concat).orElse("");
			value = Optional.ofNullable(product.getLoadingCountry()).map(IsoNamePair::getName).map(t->t.concat(countryPoint)).orElse("");
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}
		
		if (isSea) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Transshipment Port");
			value = Optional.ofNullable(product.getTransshipmentPort()).map(IdNamePair::getName).orElse("");
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}
		
		if (isAir) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Transshipment Airport");
			value = Optional.ofNullable(product.getTransshipmentPort()).map(IdNamePair::getName).orElse("");
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}
		
		if (isSea || isAir) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Transit Time(Days)");
			value = Optional.ofNullable(product.getTransitTime()).map(String::valueOf).orElse("");
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}


		if (isRoad || isDomestic || isSea || isAir) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Delivery Type");
			String deliveryType = Optional.ofNullable(product.getDeliveryType()).map(DeliveryType::getName).orElse(StringUtils.EMPTY);
			value = translate(languageCode,deliveryType);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}
		
		if (isSea || isAir) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Delivery Address");
			
			String countryPoint = Optional.ofNullable(product.getDeliveryCountryPoint()).map(IdNamePair::getName).map(" - "::concat).orElse("");
			value = Optional.ofNullable(product.getDeliveryCountry()).map(IsoNamePair::getName).map(t->t.concat(countryPoint)).orElse("");
			
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		if (!isDomestic) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Incoterm");
			value = getIncotermDisplayString(languageCode, product);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		if (isRoad && (fromCountry.getIso().equals("TR") || toCountry.getIso().equals("TR"))) {

			if (isImport) {
				hasDifferentBackgroundColor = Boolean.FALSE;
				label = translate(languageCode, "Arrival Customs Office");
				value = getArrivalCustomsOfficeDisplayString(quote);
				model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
			} else {
				hasDifferentBackgroundColor = Boolean.FALSE;
				label = translate(languageCode, "Departure Customs Office");
				value = getDepartureCustomsOfficeDisplayString(quote);
				model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
			}

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Arrival Customs Clearance Type");
			value = getArrivalCustomsClearanceTypeDisplayString(languageCode, quote);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Arrival Customs Location");
			value = getArrivalCustomsLocationDisplayString(quote);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		if (!isDomestic) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Quantity, Unit, Size, Stacking");
			value = getPackageDetailsDisplayString(languageCode, quote);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Weight");
			value = getWeightDisplayString(quote.getMeasurement());
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Loading Meter (LDM)");
			value = getLoadingMeterDisplayString(quote.getMeasurement());
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Volume");
			value = getVolumeDisplayString(quote.getMeasurement());
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			if (isRoad) {
				hasDifferentBackgroundColor = Boolean.FALSE;
				label = translate(languageCode, "Vehicle Type");
				value = getVehicleRequirementsDisplayString(languageCode, quote);
				model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
			} else if (isSea) {
				hasDifferentBackgroundColor = Boolean.FALSE;
				label = translate(languageCode, "Container Requirements");
				value = getContainerRequirementsDisplayString(languageCode, quote);
				model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
			}
		}

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Service Type");
		value = getServiceTypesDisplayString(quote, languageCode);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "Prices");
		value = "";
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		Price freightPrice = null;
		List<Price> pricesAddedToFreightPrice = new ArrayList<>();
		List<Price> pricesNotAddedToFreightPrice = new ArrayList<>();

		for (Price price : quote.getPrices()) {
			if (price.getType().equals(PriceType.INCOME)) {
				if (billingItemService.getBillingItemByName(price.getBillingItem()).getDescription().equals("Freight")) {
					freightPrice = price;
				} else {
					// Sadece 0'dan büyük olanları dikkate al.
					if (price.getCharge().getAmount().compareTo(BigDecimal.ZERO) > 0) {
						if (price.isAddToFreight()) {
							pricesAddedToFreightPrice.add(price);
						} else {
							pricesNotAddedToFreightPrice.add(price);
						}
					}
				}
			}
		}

		if (freightPrice == null) {
			throw new ApplicationException("Freight price cannot be null.");
		}

		BigDecimal displayedFreightPriceAmount = freightPrice.getCharge().getAmount();

		for (Price price : pricesAddedToFreightPrice) {
			BigDecimal exchangeRate = exchangeRateService.getExchangeRate(price.getCharge().getCurrency(), freightPrice.getCharge().getCurrency(), quote.getSubsidiary().getId());
			BigDecimal convertedPrice = price.getCharge().getAmount().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
			displayedFreightPriceAmount = displayedFreightPriceAmount.add(convertedPrice);
		}

		List<ModelForSpotQuotePdfTemplate.Price> displayedPrices = new ArrayList<>();

		String priceDescription = "";
		if(isRoad){
			if("FTL".equals(product.getShipmentLoadingType())){
				priceDescription = "("+ translate(languageCode, "per truck") + ")";
			}else if("LTL".equals(product.getShipmentLoadingType())) {
				priceDescription = "("+ translate(languageCode, "per shipment") + ")";
			}
		}

		for (Price price : pricesNotAddedToFreightPrice) {
			ModelForSpotQuotePdfTemplate.Price p = new ModelForSpotQuotePdfTemplate.Price();
			p.setBillingItemDescription(translate(languageCode,billingItemService.getBillingItemByName(price.getBillingItem()).getDescription()));
			p.setAmountWithCurrency(price.getCharge().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + " " + price.getCharge().getCurrency() + "   " + priceDescription);
			displayedPrices.add(p);
		}

		Collections.sort(displayedPrices);

		ModelForSpotQuotePdfTemplate.Price displayedFreightPrice = new ModelForSpotQuotePdfTemplate.Price();
		displayedFreightPrice.setBillingItemDescription(translate(languageCode,billingItemService.getBillingItemByName(freightPrice.getBillingItem()).getDescription()));
		displayedFreightPrice.setAmountWithCurrency(displayedFreightPriceAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + " " + freightPrice.getCharge().getCurrency() + "   " + priceDescription);

		displayedPrices.add(0, displayedFreightPrice);

		for (ModelForSpotQuotePdfTemplate.Price price : displayedPrices) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = price.getBillingItemDescription();
			value = price.getAmountWithCurrency();
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "Payment Terms");
		value = "";
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Payment Type");
		value = translate(languageCode, ConverterType.INITIAL_CASE.convert(quote.getPaymentRule().getType()));
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Invoice Term");
		value = quote.getPaymentRule().getPaymentDueDays().toString();
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		model.setContactGreeting(setGreeting(quote, languageCode));
		model.setAboutCompanyHtml(StringUtils.isNotBlank(pdfSetting.getAboutCompany()) ? pdfSetting.getAboutCompany().replaceAll("\\n", "<br />") : "");
		model.setGeneralConditionsHtml(isImport ? pdfSetting.getImportGeneralConditions() : pdfSetting.getExportGeneralConditions());

		model.setRoadLabel(translate(languageCode, "Road"));
		model.setSeaLabel(translate(languageCode, "Sea"));

		List<Note> notes = noteCrudService.getByQuote(quote);
		model.setSpecialNotes("");
		if ( !notes.isEmpty()){
			String specialNotes = "SPECIAL NOTES";
			if (!languageCode.equalsIgnoreCase("en")) {
				specialNotes = translatorService.translate("crm", "SPECIAL NOTES", languageCode);
			}

			hasDifferentBackgroundColor = Boolean.TRUE;
			label = specialNotes;
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, " "));
			List<String> noteList = notes.stream().filter(note -> "SPOT_PDF_NOTE".equalsIgnoreCase(note.getType().getCode())).map(Note::getNoteId).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(noteList)) {
//			StringBuilder notesToBeAddedBuilder = new StringBuilder(
//                    "<div class=\"\" data-block=\"true\" data-editor=\"37oaf\" data-offset-key=\"1sfes-0-0\">\n" +
//                            "    <div data-offset-key=\"1sfes-0-0\" class=\"public-DraftStyleDefault-block public-DraftStyleDefault-ltr\"><span data-offset-key=\"1sfes-0-0\" style=\"font-weight: bold;\"><span data-text=\"true\">"+specialNotes+"</span></span>\n" +
//                            "    </div>\n" +
//                            "</div>");
				noteService.findNotesByIds(noteList)
						.stream()
						.map(ekol.crm.quote.domain.dto.noteservice.Note::getContent).forEach(note -> model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(false, "", note)));
			}
		}

		return createPdfForSpotQuote(model, Utils.generatePdfFileName(quote), languageCode);

	}

	public String createMailForSpotQuote(SpotQuote quote, Long languageId) {

		SpotProduct product = (SpotProduct) quote.getProducts().iterator().next(); // Her zaman 1 adet kayıt olduğundan ilkini alıyoruz.
		IsoNamePair subsidiaryCountry = findSubsidiaryCountry(quote);
		IsoNamePair fromCountry = product.getFromCountry();
		IsoNamePair toCountry = product.getToCountry();
		boolean isImport = isImport(subsidiaryCountry, fromCountry, toCountry);
		String languageCode = "en";
		String htmlString = null;

		
		boolean isRoad = quote.getServiceArea().equals("ROAD");
		boolean isSea = quote.getServiceArea().equals("SEA");

		CompanyLocation accountLocation = kartoteksService.findLocationById(quote.getAccountLocation().getId(), true);
		if (accountLocation == null || !accountLocation.getActive()) {
			throw new BadRequestException("Account location is deleted or not active.");
		}

		
		String accountName = quote.getAccount().getName();
		String accountLoc = accountLocation.getPostaladdress().getFormattedAddress();

		accountName = Normalizer.normalize(accountName, Normalizer.Form.NFKD);
		accountName = accountName.replaceAll("[^\\p{ASCII}]", "");
		accountLoc = Normalizer.normalize(accountLoc, Normalizer.Form.NFKD);
		accountLoc = accountLoc.replaceAll("[^\\p{ASCII}]", "");

		
		ModelForSpotQuotePdfTemplate model = new ModelForSpotQuotePdfTemplate();

		model.setId(quote.getId());
		model.setNumber(String.valueOf(quote.getNumber()));
		model.setServiceArea(quote.getServiceArea());

		model.setAccountLabel(translate(languageCode, "Account"));
		model.setAccount(accountName);

		model.setAccountLocationLabel(translate(languageCode, "Account Location"));
		model.setAccountLocation(accountLoc);

		model.setCreatedByLabel(translate(languageCode, "Created By"));
		model.setCreatedBy(userService.findUser(quote.getCreatedBy()).getDisplayName());

		model.setQuoteNumberLabel(translate(languageCode, "Quote"));
		model.setQuoteNumber(quote.getName());

		model.setQuadroLabel(translate(languageCode, "Quadro Number"));
		model.setQuadro(quote.getMappedIds().stream().filter(t -> Objects.equals("QUADRO", t.getApplication())).findFirst().map(QuoteIdMapping::getApplicationQuoteId).orElse(StringUtils.EMPTY));

		model.setDetailsTableLabel(translate(languageCode, "Quote Details"));

		model.setDetailsTableRows(new ArrayList<>());

		Boolean hasDifferentBackgroundColor = Boolean.TRUE;
		String label = translate(languageCode, "Properties");
		String value = "";
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Departure");
		value = getCountryDisplayString(fromCountry);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Arrival");
		value = getCountryDisplayString(toCountry);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		if (isRoad) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Loading Type");
			value = translate(languageCode, product.getLoadingType().getName());
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = getLoadingAddressLabel(languageCode, quote);
		value = getLoadingAddressDisplayString(quote, product);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		if (isRoad) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Delivery Type");
			value = translate(languageCode, product.getDeliveryType().getName());
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = getDeliveryAddressLabel(languageCode, quote);
		value = getDeliveryAddressDisplayString(quote, product);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Incoterm");
		value = getIncotermDisplayString(languageCode, product);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		if (isRoad && Stream.of(fromCountry.getIso(), toCountry.getIso()).anyMatch("TR"::equals)) {
			if (isImport) {
				hasDifferentBackgroundColor = Boolean.FALSE;
				label = translate(languageCode, "Arrival Customs Office");
				value = getArrivalCustomsOfficeDisplayString(quote);
				value = Normalizer.normalize(value, Normalizer.Form.NFKD);
				value = value.replaceAll("[^\\p{ASCII}]", "");
				model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
			} else {
				hasDifferentBackgroundColor = Boolean.FALSE;
				label = translate(languageCode, "Departure Customs Office");
				value = getDepartureCustomsOfficeDisplayString(quote);
				value = Normalizer.normalize(value, Normalizer.Form.NFKD);
				value = value.replaceAll("[^\\p{ASCII}]", "");
				model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
			}

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Arrival Customs Clearance Type");
			value = getArrivalCustomsClearanceTypeDisplayString(languageCode, quote);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Arrival Customs Location");
			value = getArrivalCustomsLocationDisplayString(quote);
			value = Normalizer.normalize(value, Normalizer.Form.NFKD);
			value = value.replaceAll("[^\\p{ASCII}]", "");
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Quantity, Unit, Size, Stacking");
		value = getPackageDetailsDisplayString(languageCode, quote);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Weight");
		value = getWeightDisplayString(quote.getMeasurement());
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Loading Meter (LDM)");
		value = getLoadingMeterDisplayString(quote.getMeasurement());
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Volume");
		value = getVolumeDisplayString(quote.getMeasurement());
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		if (isRoad) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Vehicle Type");
			value = getVehicleRequirementsDisplayString(languageCode, quote);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		} else if (isSea) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = translate(languageCode, "Container Requirements");
			value = getContainerRequirementsDisplayString(languageCode, quote);
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Service Type");
		value = getServiceTypesDisplayString(quote, languageCode);
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "Prices");
		value = "";
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		Price freightPrice = null;
		List<Price> pricesAddedToFreightPrice = new ArrayList<>();
		List<Price> pricesNotAddedToFreightPrice = new ArrayList<>();

		for (Price price : quote.getPrices()) {
			if (price.getType().equals(PriceType.INCOME)) {
				if (billingItemService.getBillingItemByName(price.getBillingItem()).getDescription().equals("Freight")) {
					freightPrice = price;
				} else {
					// Sadece 0'dan büyük olanları dikkate al.
					if (price.getCharge().getAmount().compareTo(BigDecimal.ZERO) > 0) {
						if (price.isAddToFreight()) {
							pricesAddedToFreightPrice.add(price);
						} else {
							pricesNotAddedToFreightPrice.add(price);
						}
					}
				}
			}
		}

		if (freightPrice == null) {
			throw new ApplicationException("Freight price cannot be null.");
		}

		BigDecimal displayedFreightPriceAmount = freightPrice.getCharge().getAmount();

		for (Price price : pricesAddedToFreightPrice) {
			BigDecimal exchangeRate = exchangeRateService.getExchangeRate(price.getCharge().getCurrency(), freightPrice.getCharge().getCurrency(), quote.getSubsidiary().getId());
			BigDecimal convertedPrice = price.getCharge().getAmount().multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
			displayedFreightPriceAmount = displayedFreightPriceAmount.add(convertedPrice);
		}

		List<ModelForSpotQuotePdfTemplate.Price> displayedPrices = new ArrayList<>();

		String priceDescription = "";
		if(isRoad){
			if("FTL".equals(product.getShipmentLoadingType())){
				priceDescription = "(" + translate(languageCode, "per truck") + ")";
			}else if("LTL".equals(product.getShipmentLoadingType())) {
				priceDescription = "("+ translate(languageCode, "per shipment") + ")";
			}
		}

		for (Price price : pricesNotAddedToFreightPrice) {
			ModelForSpotQuotePdfTemplate.Price p = new ModelForSpotQuotePdfTemplate.Price();
			p.setBillingItemDescription(translate(languageCode, billingItemService.getBillingItemByName(price.getBillingItem()).getDescription()));
			p.setAmountWithCurrency(price.getCharge().getAmount().setScale(2, BigDecimal.ROUND_HALF_UP) + " " + price.getCharge().getCurrency() + "   " + priceDescription);
			displayedPrices.add(p);
		}

		Collections.sort(displayedPrices);

		ModelForSpotQuotePdfTemplate.Price displayedFreightPrice = new ModelForSpotQuotePdfTemplate.Price();
		displayedFreightPrice.setBillingItemDescription(translate(languageCode, billingItemService.getBillingItemByName(freightPrice.getBillingItem()).getDescription()));
		displayedFreightPrice.setAmountWithCurrency(displayedFreightPriceAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + " " + freightPrice.getCharge().getCurrency() + "   " + priceDescription);

		displayedPrices.add(0, displayedFreightPrice);

		for (ModelForSpotQuotePdfTemplate.Price price : displayedPrices) {
			hasDifferentBackgroundColor = Boolean.FALSE;
			label = price.getBillingItemDescription();
			value = price.getAmountWithCurrency();
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));
		}

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "Payment Terms");
		value = "";
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Payment Type");
		value = translate(languageCode, ConverterType.INITIAL_CASE.convert(quote.getPaymentRule().getType()));
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Invoice Term");
		value = quote.getPaymentRule().getPaymentDueDays().toString();
		model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		model.setContactGreeting(setGreeting(quote, languageCode));
		//model.setAboutCompanyHtml(pdfSetting.getAboutCompany()); 
		//model.setGeneralConditionsHtml(isImport ? pdfSetting.getImportGeneralConditions() : pdfSetting.getExportGeneralConditions());

		model.setRoadLabel(translate(languageCode, "Road"));
		model.setSeaLabel(translate(languageCode, "Sea"));
		
		List<Note> notes = noteCrudService.getByQuote(quote);
		model.setSpecialNotes("");
		if ( !notes.isEmpty()){
			String specialNotes = "SPECIAL NOTES";
			if (!languageCode.equalsIgnoreCase("en")) {
				specialNotes = translatorService.translate("crm", "SPECIAL NOTES", languageCode);
			}

			hasDifferentBackgroundColor = Boolean.TRUE;
			label = specialNotes;
			model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, " "));
			List<String> noteList = notes.stream().filter(note -> "SPOT_PDF_NOTE".equalsIgnoreCase(note.getType().getCode())).map(Note::getNoteId).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(noteList)) {
				
				noteService.findNotesByIds(noteList)
						.stream()
						.map(ekol.crm.quote.domain.dto.noteservice.Note::getContent).forEach(note -> model.getDetailsTableRows().add(new ModelForSpotQuotePdfTemplate.DetailsTableRow(false, "", note)));
			}
		}

		if("NOTIFICATION".equalsIgnoreCase(environment.getProperty("oneorder.quote.notification.quadro-no", String.class, "EMAIL"))) {
			BeanUtils.getBean(NotificationApi.class).sendNotification("QUOTE_QUADRO_NO_RECEIVED", model, quote.getCreatedBy());
		} else {
			try {
				htmlString = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfiguration.getTemplate(SPOT_QUOTE_TEMPLATE_NAME_MAIL), model);
			} catch (Exception e) {
				throw new ApplicationException("Exception while creating mail", e);
			}
		}
		return htmlString;
	}

	public File createPdfForSpotQuote(ModelForSpotQuotePdfTemplate model, String pdfFileName, String languageCode) {

		File result = null;

		try {

			Template template = freeMarkerConfiguration.getTemplate(SPOT_QUOTE_TEMPLATE_NAME);
			String htmlString = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			String pdfFilePath = fileRepoPath.resolve(pdfFileName).toString();

			float marginLeft = Utils.convertMillimeterToPdfUnit(10);
			float marginRight = marginLeft;
			float marginTop = Utils.convertMillimeterToPdfUnit(40);
			float marginBottom = marginTop;

			Utils.convertHtmlToPdf(
					htmlString, pdfFilePath, new OurImageProvider(), new OurPdfPageEventHelper(FONT_DIRECTORY, languageCode),
					PageSize.A4, marginLeft, marginRight, marginTop, marginBottom, 
					FONT_DIRECTORY);

			result = new File(pdfFilePath);

		} catch (Exception e) {
			throw new ApplicationException("Exception while creating pdf file", e);
		}

		return result;
	}

	///////////// tender////////////

	public String createMailForTenderQuote(TenderQuote quote, Long languageId, String greeting) {

		String languageCode = "en";
		String htmlString;

		ModelForTenderQuotePdfTemplate model = new ModelForTenderQuotePdfTemplate();
		model.setGreetingMessage(greeting);
		model.setDetailsTableRows(new ArrayList<>());

		
		Boolean hasDifferentBackgroundColor = Boolean.TRUE;
		String label = translate(languageCode, "COMMON INFO");
		String value = "";
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Contract Start Date");
		value = Optional.of(quote).map(TenderQuote::getContractStartDate).map(LocalDate::toString).orElse(StringUtils.EMPTY);
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Payment Term");
		value = (quote.getPaymentDueDays()).toString();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		
		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "TRANSPORTATION & EQUIPMENT INFO");
		value = "";
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Product Type");
		value = quote.getProductType();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Equipment Type");
		value = quote.getEquipmentType();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Transportation Type");
		value = quote.getTransportationType();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "PRICING INFO");
		value = "";
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Important Price Issues");
		value = quote.getImportantPriceIssues();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Conversion Factors For LTL");
		value = quote.getConversionFactorsLtl();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "SURCHARGES INFO");
		value = "";
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Diesel Mechanism");
		value = quote.getDieselMechanism();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "ADR/Frigo/Express");
		value = quote.getAdrFrigoExpress();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Demurrage");
		value = quote.getDemurrage();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Cancellation");
		value = quote.getCancellation();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "KPI & PENALTY INFO");
		value = "";
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "KPI");
		value = quote.getKpi();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Penalty");
		value = quote.getPenaltyDetail();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.TRUE;
		label = translate(languageCode, "LOADING & UNLOADING INFO");
		value = "";
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Loading & Unloading Free Times");
		value = quote.getLoadUnloadFreeTimes();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		hasDifferentBackgroundColor = Boolean.FALSE;
		label = translate(languageCode, "Stackability");
		value = quote.getStackability();
		model.getDetailsTableRows().add(new ModelForTenderQuotePdfTemplate.DetailsTableRow(hasDifferentBackgroundColor, label, value));

		
		try {

			Template template = freeMarkerConfiguration.getTemplate(TENDER_QUOTE_TEMPLATE_NAME_MAIL);
			htmlString = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		} catch (Exception e) {
			throw new ApplicationException("Exception while creating mail", e);
		}

		return htmlString;
	}

	
	
	public byte[] readPdfForSpotQuote(SpotQuote quote) throws IOException {
		return FileUtils.readFileToByteArray(fileRepoPath.resolve(Utils.generatePdfFileName(quote)).toFile());
	}
}
