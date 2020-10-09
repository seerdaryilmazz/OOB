package ekol.email.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum EmailSendStatus {
	NOT_SENT,
	SUCCESSFUL,
	FAILED,
	;
}
