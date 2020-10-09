package ekol.crm.search.domain.quote;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.*;

import ekol.crm.search.domain.SearchDoc;
import ekol.crm.search.event.dto.quote.QuoteJson;
import ekol.crm.search.serializer.*;
import ekol.crm.search.utils.LanguageStringUtils;
import ekol.model.*;
import lombok.*;


@Document(indexName = "crm", type = "quote")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteSearchDoc extends SearchDoc {

    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Long)
    private Long number;

    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.Nested)
    private IdNamePair account;

    @Field(type = FieldType.Nested)
    private IdNamePair accountLocation;

    @Field(type = FieldType.Nested)
    private CodeNamePair serviceArea;

    @Field(type = FieldType.Nested)
    private CodeNamePair type;

    @Field(type = FieldType.Nested)
    private CodeNamePair status;

    @Field(type = FieldType.Long)
    private Long potentialId;

    @Field(type = FieldType.String)
    private String createdBy;

    @Field(type = FieldType.String)
    private String lastUpdatedBy;

    @Field(type = FieldType.String)
    private String accountOwner;
    
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date lastUpdated;

    @Field(type = FieldType.Object)
    private Map<String, String> mappedIds;

    @Field(type = FieldType.Nested)
    private List<ProductSearchDoc> products;
    
    @Field(type = FieldType.Double)
    private BigDecimal payWeight;

    @Field(type = FieldType.Object)
    private Map<String, String> quoteAttribute;

    public static QuoteSearchDoc fromQuote(QuoteJson quote){
        QuoteSearchDoc quoteSearchDoc = new QuoteSearchDocBuilder()
                .id(quote.getId())
                .number(quote.getNumber())
                .name(quote.getName())
                .account(quote.getAccount())
                .accountLocation(quote.getAccountLocation())
                .serviceArea(quote.getServiceArea())
                .type(quote.getType())
                .status(quote.getStatus())
                .potentialId(quote.getPotentialId())
                .createdBy(quote.getCreatedBy())
                .lastUpdatedBy(quote.getLastUpdatedBy())
                .accountOwner(quote.getAccountOwner())
                .lastUpdated(Date.from(quote.getLastUpdated().getDateTime().toInstant(ZoneOffset.UTC)))
                .createdAt(Date.from(quote.getCreatedAt().getDateTime().toInstant(ZoneOffset.UTC)))
                .mappedIds(quote.getMappedIds())
                .products(Optional.ofNullable(quote.getProducts()).orElseGet(ArrayList::new).stream().map(ProductSearchDoc::fromProduct).collect(Collectors.toList()))
                .payWeight(quote.getPayWeight())
                .quoteAttribute(quote.getQuoteAttribute())
                .build();
        quoteSearchDoc.setAccountName(LanguageStringUtils.setTextForSearch(quote.getAccount().getName()));
        quoteSearchDoc.setTextToSearch(LanguageStringUtils.setTextForSearch(quote.getName()));
        quoteSearchDoc.setDocumentType("quote");
        return quoteSearchDoc;
    }
}
