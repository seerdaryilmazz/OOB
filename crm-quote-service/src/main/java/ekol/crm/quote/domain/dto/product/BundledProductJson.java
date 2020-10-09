package ekol.crm.quote.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.quote.domain.dto.MonetaryAmountJson;
import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.enumaration.UnitOfMeasure;
import ekol.crm.quote.domain.enumaration.VehicleType;
import ekol.crm.quote.domain.model.businessVolume.BusinessVolume;
import ekol.crm.quote.domain.model.product.BundledProduct;
import ekol.crm.quote.domain.model.product.Product;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class BundledProductJson extends ProductJson {

    private UnitOfMeasure unitOfMeasure;
    private Integer quantity;
    private MonetaryAmountJson priceOriginal;
    private MonetaryAmountJson priceExchanged;
    private MonetaryAmountJson expectedTurnoverOriginal;
    private MonetaryAmountJson expectedTurnoverExchanged;
    private String customsServiceType;
    private IdNamePair customsOffice;
    private Set<VehicleType> vehicleType = new HashSet<>();
    private IsoNamePair country;
    private IdNamePair point;
    private BusinessVolume businessVolume;
    private IdNamePair crossDock;

    @Override
    public Product toEntity(){
        BundledProduct entity = new BundledProduct();
        entity.setId(getId());
        entity.setFromCountry(getFromCountry());
        entity.setFromPoint(getFromPoint());
        entity.setToCountry(getToCountry());
        entity.setToPoint(getToPoint());
        entity.setShipmentLoadingType(getShipmentLoadingType());
        entity.setIncoterm(getIncoterm());
        entity.setIncotermExplanation(getIncotermExplanation());
        entity.setCalculationType(getCalculationType());
        entity.setStatus(getStatus());
        entity.setUnitOfMeasure(getUnitOfMeasure());
        entity.setQuantity(getQuantity());
        entity.setPriceOriginal(getPriceOriginal() != null ? getPriceOriginal().toEntity() : null);
        entity.setPriceExchanged(getPriceExchanged() != null ? getPriceExchanged().toEntity() : null);
        entity.setExpectedTurnoverOriginal(getExpectedTurnoverOriginal() != null ? getExpectedTurnoverOriginal().toEntity() : null);
        entity.setExpectedTurnoverExchanged(getExpectedTurnoverExchanged() != null ? getExpectedTurnoverExchanged().toEntity() : null);
        entity.setExistence(getExistence());
        entity.setCustomsServiceType(getCustomsServiceType());
        entity.setCustomsOffice(getCustomsOffice());
        entity.setLostReason(getLostReason() != null ? getLostReason().toEntity() : null);
        entity.setServiceArea(getServiceArea().getCode());
        entity.setVehicleType(getVehicleType());
        entity.setCountry(getCountry());
        entity.setPoint(getPoint());
        entity.setCrossDock(getCrossDock());
        return entity;
    }

    @Override
    public void validate(QuoteType type) {

        if(type!=QuoteType.TENDER){
            if(getQuantity() == null && !getServiceArea().getCode().equals("WHM")){
                throw new ValidationException("Quantity should not be empty for Product");
            }
            if(getExpectedTurnoverOriginal() == null){
                throw new ValidationException("Expected turnover original value should not be empty for Product");
            }
            if(getExpectedTurnoverExchanged() == null){
                throw new ValidationException("Expected turnover exchanged value should not be empty for Product");
            }

        }

        if(getUnitOfMeasure() == null && !getServiceArea().getCode().equals("WHM")){
            throw new ValidationException("Product should have a unit of measure");
        }
        if(getExistence() == null){
            throw new ValidationException("Existence type should not be empty for Product");
        }


        if(getServiceArea().getCode().equals("ROAD") || getServiceArea().getCode().equals("SEA") || getServiceArea().getCode().equals("DTR")){
            if(StringUtils.isEmpty(getShipmentLoadingType())){
                throw new ValidationException("Shipment loading type should not be empty");
            }
        }
    }

}
