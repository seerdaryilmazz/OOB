package ekol.crm.opportunity.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by Dogukan Sahinturk on 9.12.2019
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum QuotePriceType {
    INCOME, EXPENSE
}
