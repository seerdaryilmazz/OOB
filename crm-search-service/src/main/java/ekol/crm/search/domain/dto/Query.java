package ekol.crm.search.domain.dto;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.NotBlank;

import ekol.crm.search.domain.DocumentType;
import lombok.Data;

@Data
public class Query {
	
	@NotBlank
	private String q;
	
	private DocumentType documentType;
	
	@Max(100)
	private Integer size;
	
	private Integer page;
	
	private Boolean withoutGlobal;
	
	private Long withoutId;
}
