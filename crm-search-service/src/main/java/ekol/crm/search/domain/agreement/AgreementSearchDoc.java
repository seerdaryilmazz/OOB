package ekol.crm.search.domain.agreement;

import java.time.ZoneOffset;
import java.util.*;

import org.springframework.data.elasticsearch.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.*;

import ekol.crm.search.domain.SearchDoc;
import ekol.crm.search.event.dto.agreement.AgreementJson;
import ekol.crm.search.serializer.*;
import ekol.crm.search.utils.LanguageStringUtils;
import ekol.model.*;
import lombok.*;

@Document(indexName = "crm", type = "agreement")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementSearchDoc extends SearchDoc {

    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.Long)
    private Long number;
    
    @Field(type = FieldType.Nested)
    private IdNamePair account;

    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonDatePattern("dd/MM/yyyy")
    private Date startDate;

    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonDatePattern("dd/MM/yyyy")
    private Date endDate;

    @Field(type = FieldType.Nested)
    private List<CodeNamePair> serviceAreas;
    
    @Field(type = FieldType.Nested)
    private CodeNamePair status;
    
    @Field(type = FieldType.Nested)
    private CodeNamePair type;

    public static AgreementSearchDoc fromAgreement(AgreementJson agreement){
        AgreementSearchDoc agreementSearchDoc = new AgreementSearchDocBuilder()
                .id(agreement.getId())
                .name(agreement.getName())
                .number(agreement.getNumber())
                .account(agreement.getAccount())
                .status(agreement.getStatus())
                .type(agreement.getType())
                .startDate(Date.from(agreement.getStartDate().atStartOfDay().toInstant(ZoneOffset.UTC)))
                .endDate(Date.from(agreement.getEndDate().atStartOfDay().toInstant(ZoneOffset.UTC)))
                .serviceAreas(agreement.getServiceAreas()).build();

        agreementSearchDoc.setAccountName(LanguageStringUtils.setTextForSearch(agreement.getAccount().getName()));
        agreementSearchDoc.setTextToSearch(LanguageStringUtils.setTextForSearch(agreement.getName()));
        agreementSearchDoc.setDocumentType("agreement");
        return agreementSearchDoc;
    }
}
