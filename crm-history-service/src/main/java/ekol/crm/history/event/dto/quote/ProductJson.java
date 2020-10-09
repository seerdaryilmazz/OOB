package ekol.crm.history.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class ProductJson{

    private Long id;
    private IsoNamePair fromCountry;
    private IdNamePair fromPoint;
    private IsoNamePair toCountry;
    private IdNamePair toPoint;
    private String shipmentLoadingType;
    private String incoterm;
    private String incotermExplanation;
    private CodeNamePair calculationType;
    private CodeNamePair status;
    private CodeNamePair existence;
    private CodeNamePair serviceArea;

    // SPOT PRODUCT SPECIFIC FIELDS
    private LocalDate earliestReadyDate;
    private LocalDate latestReadyDate;
    private CodeNamePair loadingType;
    private IdNamePair loadingCompany;
    private IdNamePair loadingLocation;
    private CodeNamePair deliveryType;
    private IdNamePair deliveryCompany;
    private IdNamePair deliveryLocation;
    private Integer vehicleCount;

}
