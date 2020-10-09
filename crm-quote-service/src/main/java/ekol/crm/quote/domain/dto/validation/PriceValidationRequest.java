package ekol.crm.quote.domain.dto.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.dto.PriceJson;
import ekol.exceptions.ValidationException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PriceValidationRequest {

    private String fromCountryCode;
    private String fromPostalCode;
    private String toCountryCode;
    private String toPostalCode;
    private String truckLoadType;
    private LocalDate loadingDate;
    private LocalDate deliveryDate;
    private String loadingType;
    private String deliveryType;
    private String vehicleType;
    private BigDecimal weight;
    private BigDecimal ldm;
    private BigDecimal volume;
    private BigDecimal payWeight;
    private List<PriceJson> prices;

    public void validate(){
        if(StringUtils.isBlank(getFromCountryCode())){
            throw new ValidationException("From country code should not be empty");
        }
        if(StringUtils.isBlank(getFromPostalCode())){
            throw new ValidationException("From postal code should not be empty");
        }
        if(StringUtils.isBlank(getToCountryCode())){
            throw new ValidationException("To country code should not be empty");
        }
        if(StringUtils.isBlank(getToPostalCode())){
            throw new ValidationException("To postal code should not be empty");
        }
        if(CollectionUtils.isEmpty(prices)){
            throw new ValidationException("Freight Price should not be empty");
        }
        Optional.ofNullable(prices).orElseGet(Collections::emptyList).forEach(quotePriceJson -> {
            if(quotePriceJson == null || quotePriceJson.getCharge() == null){
                throw new ValidationException("{0} price should not be empty", quotePriceJson.getBillingItem().getDescription());
            }
        });
    }
}
