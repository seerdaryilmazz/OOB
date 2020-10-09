package ekol.crm.opportunity.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by Dogukan Sahinturk on 7.01.2020
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CloseType {
    CLOSE, REJECT, WITHDRAWN;
}
