package ekol.orders.transportOrder.domain;


import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by burak on 18/10/16.
 */

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum OrderQuotaTime {

    DAY, WEEK, MONTH, YEAR;

}
