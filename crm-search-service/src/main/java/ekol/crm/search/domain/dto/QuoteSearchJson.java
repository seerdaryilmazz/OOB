package ekol.crm.search.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteSearchJson {

	private Integer page = 0;
	
	private List<String> teammates;
	
	private Long accountId;
	private String createdBy;
	private Long number;
	private String typeCode;
	private String statusCode;
	private String minUpdateDate;
	private String maxUpdateDate;
	private String serviceAreaCode;
	private String shipmentLoadingType;
	private String minCreatedAt;
	private String maxCreatedAt;
	private String fromCountry;
	private String toCountry;
	private Long fromPoint;
	private Long toPoint;
	private String quoteAttributeKey;
	private String quoteAttributeValue;
}
