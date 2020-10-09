package ekol.notification.domain;

import ekol.json.serializers.common.*;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup(ConverterType.HAS_DESCRIPTION)
public enum Channel implements EnumWithDescription {
	WEB_PUSH("Web Notification"),
	EMAIL("E-Mail"),
	;

	private String description;
}
