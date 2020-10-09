package ekol.crm.quote.domain.dto.product;

import java.util.*;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.*;

import ekol.crm.quote.domain.dto.LostReasonJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.product.Product;
import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "discriminator",
        defaultImpl = ProductJson.class,
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpotProductJson.class, name ="SPOT"),
        @JsonSubTypes.Type(value = BundledProductJson.class, name = "BUNDLED")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class ProductJson{

    private Long id;
    private IsoNamePair fromCountry;
    private IdNamePair fromPoint;
    private IsoNamePair toCountry;
    private IdNamePair toPoint;
    private String shipmentLoadingType;
    private String incoterm;
    private String incotermExplanation;
    private CalculationType calculationType;
    private ProductStatus status;
    private ExistenceType existence;
    private LostReasonJson lostReason;
    private CodeNamePair serviceArea;
    private String discriminator;
    private Set<VehicleType> vehicleType;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;
    private UtcDateTime deletedAt;

    protected abstract void validate(QuoteType type);

    public abstract Product toEntity();


    public void validateProduct(QuoteType type, String countryCode){
        if(getServiceArea() == null){
            throw new ValidationException("Product should have service area info");
        }
        if(getStatus() == null){
            throw new ValidationException("Product should have a status");
        }

        if(!(getServiceArea().getCode().equals("CCL") || getServiceArea().getCode().equals("WHM")) ){
            if(getFromCountry() == null || StringUtils.isEmpty(getFromCountry().getIso())){
                throw new ValidationException("From country should not be empty");
            }
            if(type!= QuoteType.TENDER){
                if(StringUtils.isEmpty(getFromPoint())){
                    throw new ValidationException("From country point should not be empty");
                }
            }

            if(getToCountry() == null || StringUtils.isEmpty(getToCountry().getIso())){
                throw new ValidationException("To country should not be empty");
            }
        }
        if(getServiceArea().getCode().equals("DTR")){
            if(!Objects.equals(getFromCountry().getIso(), countryCode) ||
            		!Objects.equals(getToCountry().getIso(), countryCode)){
                throw new ValidationException("From/To country should be {0}", countryCode) ;
            }
            if(getCalculationType() == null){
                throw new ValidationException("Calculation type should not be empty");
            }
        }
        validate(type);
    }
}
