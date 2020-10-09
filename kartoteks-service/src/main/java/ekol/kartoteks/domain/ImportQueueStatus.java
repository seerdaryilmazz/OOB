package ekol.kartoteks.domain;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

/**
 * Created by fatmaozyildirim on 4/7/16.
 */
@EnumSerializableToJsonAsLookup
public enum ImportQueueStatus {
    PENDING,
    SUCCESS,
    OUT_OF_DATE
}
