package ekol.crm.search.domain;

import ekol.model.IdNamePair;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchDoc {

    @Field(type = FieldType.Long)
    private Long id;
    @Field(type = FieldType.String)
    private String name;

    public static CompanySearchDoc fromCompany(IdNamePair company){
        return new CompanySearchDocBuilder()
                .id(company.getId())
                .name(company.getName()).build();
    }
}
