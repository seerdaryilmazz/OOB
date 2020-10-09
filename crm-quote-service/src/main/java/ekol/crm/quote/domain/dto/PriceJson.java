package ekol.crm.quote.domain.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.dto.sales.SalesDemandJson;
import ekol.crm.quote.domain.enumaration.PriceType;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.service.BillingItemService;
import ekol.crm.quote.util.BeanUtils;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceJson {

    private Long id;
    private PriceType type;
    private BillingItem billingItem;
    private boolean addToFreight;
    private MonetaryAmountJson charge;
    private MonetaryAmountJson priceExchanged;
    private PriceCalculationJson priceCalculation;
    private PriceAuthorizationJson authorization;
    /**
     * Bu alan sadece fiyat hesaplama yapıldığında, sales-price-service'ten dönen kampanya bilgisini arayüze aktarmak için kullanılıyor.
     */
    private SalesDemandJson campaign;
    private String campaignId;

    public Price toEntity(){
        return Price.builder()
                .id(getId())
                .type(getType())
                .billingItem(getBillingItem().getName())
                .addToFreight(isAddToFreight())
                .charge(getCharge() != null ? getCharge().toEntity() : null)
                .priceExchanged(getPriceExchanged() != null ? getPriceExchanged().toEntity() : null)
                .priceCalculation(getPriceCalculation() != null ? getPriceCalculation().toEntity() : null)
                .campaignId(getCampaignId())
                .authorization(Optional.ofNullable(getAuthorization()).map(PriceAuthorizationJson::toEntity).orElse(null))
                .build();
    }

    public static PriceJson fromEntity(Price price){
        return new PriceJsonBuilder()
                .id(price.getId())
                .type(price.getType())
                .billingItem(BeanUtils.getBean(BillingItemService.class).getBillingItemByName(price.getBillingItem()))
                .addToFreight(price.isAddToFreight())
                .charge(Optional.ofNullable(price.getCharge()).map(MonetaryAmountJson::fromEntity).orElse(null))
                .priceExchanged(Optional.ofNullable(price.getPriceExchanged()).map(MonetaryAmountJson::fromEntity).orElse(null))
                .priceCalculation(Optional.ofNullable(price.getPriceCalculation()).filter(t->Objects.nonNull(t.getCalculatedAmount())).map(PriceCalculationJson::fromEntity).orElse(null))
                .campaignId(price.getCampaignId())
                .authorization(Optional.ofNullable(price.getAuthorization()).map(PriceAuthorizationJson::fromEntity).orElse(null))
                .build();
    }

    public void clear(){
    	this.charge = new MonetaryAmountJson();
        this.priceExchanged = new MonetaryAmountJson();
        this.priceCalculation = null;
        this.campaign = null;
        this.campaignId = null;
    }
}
