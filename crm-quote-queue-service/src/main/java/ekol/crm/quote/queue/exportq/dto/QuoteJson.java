package ekol.crm.quote.queue.exportq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.queue.common.dto.ServiceTypeJson;
import ekol.crm.quote.queue.common.dto.UserJson;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class QuoteJson{

    private String dmlType;
    private Long number;
    private String name;
    private AccountJson account;
    private IdNamePair subsidiary;
    private String serviceArea;
    private String type;
    private List<ProductJson> products;
    private String status;

    private String validityStartDate;
    private String validityEndDate;
    private String contact;
    private MeasurementJson totalMeasurement;
    private BigDecimal totalPayWeight;
    private Integer totalQuantity;
    private CustomsJson customs;
    private List<PackageJson> packages;
    private List<LoadJson> loads;
    private List<VehicleRequirementJson> vehicleRequirements;
    private List<ContainerRequirementJson> containerRequirements;
    private List<ServiceTypeJson> services;
    private List<PriceJson> prices;
    private PaymentRuleJson paymentRule;
    private BigDecimal chargeableWeight;

    private UserJson createdBy;
    private String createdAt;
    private String resultDate;
    
    private int revisionNumber;

    /**
     * Aşağıdaki alanlar normalde bizim yapımızda yok, Boomi tarafındaki eşleştirmeyi kısa yoldan halledebilmek için yapıldı.
     */
    private String hasDangerousGoods;
    private String adrType;
    private String hasFrigoGoods;
    private Integer frigoMinTemperature;
    private Integer frigoMaxTemperature;
    private String hasLongGoods;
    private String hasOversizeGoods;
    private String hasFragileGoods;
    private String hasFoodProduct;
    private String hasPalletization;
    private String hasHealthCertification;
    private String hasAtaCarnet;
    private String hasTransitTrade;
    private String hasCAD;
    private String serviceType;
    private Long loadingCompanyId;
    private String loadingCompanyName;
    private Long loadingLocationId;
    private String loadingLocationName;
    private Long deliveryCompanyId;
    private String deliveryCompanyName;
    private Long deliveryLocationId;
    private String deliveryLocationName;

}

