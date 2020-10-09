package ekol.crm.quote.domain.dto.quote;

import java.util.*;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "discriminator",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpotQuoteJson.class, name = "SPOT"),
        @JsonSubTypes.Type(value = LongTermQuoteJson.class, name = "LONG_TERM"),
        @JsonSubTypes.Type(value = TenderQuoteJson.class, name = "TENDER")
})
@Data
@NoArgsConstructor
public abstract class QuoteJson {

    private Long id;
    private Long number;
    private String name;
    private IdNamePair account;
    private IdNamePair accountLocation;
    @NotNull(message = "Subsidiary should not be empty")
    private IdNamePair subsidiary;
    private String defaultInvoiceCompanyCountry;
    private CodeNamePair serviceArea;
    private QuoteType type;
    private List<ProductJson> products;
    private QuoteStatus status;
    private String createdBy;
    private String quoteOwner;
    private UtcDateTime createdAt;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
    private String discriminator;
    private boolean initial; // Bu alanın entity'de yeri yok, bazı yerlerde yeni kayıt mı yaratıyor, güncelleme mi yapıyor olduğumuzu belirtmek için kullanıyoruz.
    private SupportedLocale pdfLanguage; // Bu alanın entity'de yeri yok, sadece update aşamasında gönderilen bir parametre.
    private List<NoteJson> notes = new ArrayList<>();
    private List<DocumentJson> documents = new ArrayList<>();
    private Map<String,String> mappedIds = new HashMap<>();
    private Boolean holdingForCompanyTransfer;
    private UtcDateTime requestedDate;
    private Map<String, String> quoteAttribute = new HashMap<>();
    private Set<QuoteOrderMappingJson> orders = new HashSet<>();
    
    @JsonInclude(Include.NON_NULL)
    private QuoteJson previousData = null;

    protected abstract void validate(QuoteType type);

    public abstract Quote toEntity();

    public void validateQuote(){

        if(getId() != null && getNumber() == null){
            throw new ValidationException("Quote number should not be empty");
        }
        if(getAccount() == null || getAccount().getId() == null){
            throw new ValidationException("Quote should have a account");
        }
        if(getAccountLocation() == null || getAccountLocation().getId() == null){
            throw new ValidationException("Quote should have a account location");
        }
        if(getServiceArea() == null){
            throw new ValidationException("Service area should not be empty");
        }
        if(getStatus() == null){
            throw new ValidationException("Status should not be empty");
        }
        if(getType() == null){
            throw new ValidationException("Type should not be empty");
        }
        validate(getType());
        if(getQuoteOwner() == null){
            throw new ValidationException("Quote Owner should not be empty");
        }
    }
}

