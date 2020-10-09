package ekol.note.domain;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum NoteType {
    SPOT_PDF_NOTE,
    PREPARATION_NOTES,
    ACTIVITY_REPORT,
    MANAGER_NOTE
}
