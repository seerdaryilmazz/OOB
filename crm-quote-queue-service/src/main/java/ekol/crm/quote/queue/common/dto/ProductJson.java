package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProductJson {

    private Long id;
    private IsoNamePair fromCountry;
    private IdNamePair fromPoint;
    private IsoNamePair toCountry;
    private IdNamePair toPoint;
    private String shipmentLoadingType;
    private String incoterm;
    private String incotermExplanation;
    private CodeNamePair status;
    private CodeNamePair existence;
    private LostReasonJson lostReason;
    private CodeNamePair serviceArea;

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
