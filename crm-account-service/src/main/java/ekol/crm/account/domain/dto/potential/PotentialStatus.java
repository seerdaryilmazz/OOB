package ekol.crm.account.domain.dto.potential;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum PotentialStatus {
	ACTIVE,
	INACTIVE,
	;
}
