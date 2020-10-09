package ekol.crm.activity.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ActivityTool {
    CALL,
    E_MAIL,
    MEETING,
    VIDEO_CONFERENCE
}
