package ekol.crm.opportunity.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by Dogukan Sahinturk on 14.11.2019
 */
@EnumSerializableToJsonAsLookup(ConverterType.UPPER_CASE)
public enum Currency {
    EUR, USD, TRY, GBP, RON;
}
