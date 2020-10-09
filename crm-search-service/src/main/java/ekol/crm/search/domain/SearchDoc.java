package ekol.crm.search.domain;

import javax.persistence.Transient;

import org.springframework.data.elasticsearch.annotations.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "crm")
@Setting(settingPath = "elastic/settings.json")
public class SearchDoc {

	@MultiField(
			mainField = @Field(includeInParent = true, type = FieldType.String, analyzer="search_analyzer", searchAnalyzer = "search_analyzer"),
            otherFields = {
            		@InnerField(suffix = "raw", type = FieldType.String, index = FieldIndex.analyzed, indexAnalyzer ="autocomplete_analyzer", searchAnalyzer = "autocomplete_analyzer" )})
    private String textToSearch;

	@Field(type = FieldType.String, analyzer="search_analyzer", searchAnalyzer = "search_analyzer")
	private String accountName;

    @Field(type = FieldType.String)
    private String documentType;
    
    @Transient
    private Float score;
    
}
