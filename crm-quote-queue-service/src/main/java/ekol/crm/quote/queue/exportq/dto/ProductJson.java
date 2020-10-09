package ekol.crm.quote.queue.exportq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.queue.common.dto.IsoNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ProductJson {

    private IsoNamePair fromCountry;
    private List<IdNamePair> fromPoints;
    private IsoNamePair toCountry;
    private List<IdNamePair> toPoints;
    private String shipmentLoadingType;
    private String incoterm;
    private String incotermExplanation;
    private String status;

    private String earliestReadyDate;
    private String latestReadyDate;
    private String loadingType;
    private IdNamePair loadingCompany;
    private IdNamePair loadingLocation;
    private String deliveryType;
    private IdNamePair deliveryCompany;
    private IdNamePair deliveryLocation;
    private Integer vehicleCount;


}
