package ekol.location.domain.location.enumeration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.json.serializers.common.ConverterType;

/**
 * Created by burak on 02/05/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CWBookingType {
    ORDER_REQUEST, BEFORE_READY_DATE, BEFORE_DELIVERY_DATE
}
