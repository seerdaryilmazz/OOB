package ekol.crm.account.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.UPPER_CASE)
public enum SegmentType {
    KAM, CRM, SALES, LSP, PARTNER;
}
