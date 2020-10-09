package ekol.crm.configuration.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.configuration.component.*;
import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ValueType {
	TEXT(new EmptyObjectVerification()),
	NUMBER(new EmptyObjectVerification()),
	BOOLEAN(new EmptyObjectVerification()),
	LIST(new EmptyObjectVerification()),
	LOOKUP(new EmptyObjectVerification()),
	RULE(new EmptyObjectVerification()),
	;
	
	private EmptyVerification emptyVerification;
}
