package ekol.crm.opportunity.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by Dogukan Sahinturk on 18.11.2019
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum FrequencyType {
    ANNUAL, MONTHLY, WEEKLY, DAILY;
}
