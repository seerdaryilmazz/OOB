package ekol.location.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by burak on 13/12/17.
 */
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum RouteLegExpeditionStatus {
    PLANNING, ON_THE_WAY, COMPLETED
}
