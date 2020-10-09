package ekol.orders.search.domain;

import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import ekol.orders.lookup.domain.HSCodeExtended;
import lombok.Data;

@Data
@Document(indexName = "hscodeextended")
@Setting(settingPath = "/elastic/HSCodeSettings.json")
public class HSCodeExtendedSearchDocument {

	private Long id;

	@CompletionField(analyzer="tr_analyzer")
	private String code;

	@CompletionField(analyzer="tr_analyzer")
	private String name;

	public static HSCodeExtendedSearchDocument fromHSCode(HSCodeExtended hsCodeExtended){
		HSCodeExtendedSearchDocument doc = new HSCodeExtendedSearchDocument();
		doc.setId(hsCodeExtended.getId());
		doc.setCode(hsCodeExtended.getCode());
		doc.setName(hsCodeExtended.getName());
		return doc;
	}

}
