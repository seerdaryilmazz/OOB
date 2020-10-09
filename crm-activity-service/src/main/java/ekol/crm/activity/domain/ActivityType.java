package ekol.crm.activity.domain;

import ekol.json.serializers.common.*;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ActivityType implements EnumWithDescription {
    ACTIVATION_MEETING,
    OPERATIONAL_PROBLEMS,
    GENERAL_ASSESSMENT,
    PRODUCT_INTRODUCTION,
    SALES_QUOTE_OR_CONTRACT_NEGOTIATION,
    SATISFACTION_AND_COMPLAINT_MANAGEMENT,
    COLLECTION_MEETING,
    FAIR,
    LTL_CUSTOMER_ACQUISITION("LTL Customer Acquisition");

	private String description;
}
