package ekol.crm.search.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductJson {
	private String shipmentLoadingType;
	private IsoNamePair fromCountry;
	private IsoNamePair toCountry;
	private IdNamePair fromPoint;
	private IdNamePair toPoint;
	
}
