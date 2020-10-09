package ekol.crm.quote.domain.enumaration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum LostReasonType {
    PRICE("Price",
            null),
    CANCELED_BY_CUSTOMER("Canceled by Customer",
            Arrays.asList("Late Answer")),
    FAILURE_TO_OBSERVE_DEADLINE_DATE("Failure to Observe the Deadline Date",
            null),
    FAILURE_TO_VEHICLE_ALLOCATION("Failure to Vehicle Allocation",
            Arrays.asList("No Standard Vehicle",
                    "No Special Vehicle and Equipment (Speedy)",
                    "No Special Vehicle and Equipment (Double Decker)",
                    "No Special Vehicle and Equipment (Siobas)",
                    "No Special Vehicle and Equipment (Heat Control)")),
    CONFLICT_ON_TERMS_OF_PAYMENT("Conflict on Terms of Payment",
            null),
    UNWANTED_CUSTOMER("Unwanted Customer",
            null),
    WAREHOUSE_PRICE("Warehouse Price",
            null),
    EXTERNAL_UNLOADING_COST("External Unloading Cost",
            null),
    FISCAL_CUSTOMS("Fiscal Customs",
            null),
    ADDITIONAL_CUSTOMS_CLEARENCE_COST("Additional Customs Clearance Cost",
            Arrays.asList("Domestic", "Abroad")),
    COST_STUDY_OF_COMPANY("Cost Study of Company",
            null),
    NO_ANSWER_FROM_CUSTOMER("No Answer from Customer",
            null),
    INCONVENIENT_CONTRACT_TERMS_FOR_EKOL("Inconvenient Contract Terms for Ekol",
            null),
    DEMAND_CEASED("Demand Ceased",
            null),
    TRANSIT_TIME_MISMATCHED("Transit Time Mismatched",
            null),
    EKOL_COULD_NOT_MEET_CONTRACT_TERMS("Ekol Could Not Meet Contract Terms", 
            null);

    private String id;
    private String code;
    private String name;
    private List<String> options;


    LostReasonType(String desc, List<String> options){
        this.id = name();
        this.code = name();
        this.name = desc;
        this.options = options;
    }

    @JsonCreator
    public static LostReasonType fromNode(JsonNode node) {
        if (!node.has("id"))
            return null;

        String name = node.get("id").asText();

        return LostReasonType.valueOf(name);
    }
}
