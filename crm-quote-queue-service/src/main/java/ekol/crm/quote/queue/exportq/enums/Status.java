package ekol.crm.quote.queue.exportq.enums;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;


@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum Status {
    PENDING, 
    SUCCESSFUL, 
    FAILED, 
    IGNORED, 
    SKIPPED, 
    CONSECUTIVE_FAILURE, 
    REQUEUED, 
    OUT_OF_DATE,
    ;
}
