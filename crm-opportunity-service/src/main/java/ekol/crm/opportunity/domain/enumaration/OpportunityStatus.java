package ekol.crm.opportunity.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by Dogukan Sahinturk on 14.11.2019
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum OpportunityStatus {
    PROSPECTING, QUOTED, CLOSED, CANCELED, VALUE_PROPOSITION, REJECTED, WITHDRAWN;
}
