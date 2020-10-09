package ekol.crm.quote.queue.importq.enums;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum Status {
	PENDING, 
	SUCCESSFUL, 
	FAILED, 
	REQUEUED,
	;
}
