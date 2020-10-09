package ekol.crm.quote.domain.dto.locationservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WarehouseJson {

    private IdNamePair companyLocation;
    private IdNamePair company;
    private CodeNamePair warehouseOwnerType;
    private CustomsDetailsJson customsDetails;
}
