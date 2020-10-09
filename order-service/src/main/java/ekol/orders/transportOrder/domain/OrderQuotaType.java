package ekol.orders.transportOrder.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by burak on 18/10/16.
 */

@EnumSerializableToJsonAsLookup
public enum OrderQuotaType {

    GROSS_WEIGHT, VOLUME, LDM;

}
