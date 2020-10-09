package ekol.crm.account.domain.enumaration;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum ContainerType {
    DRY_VAN, OPEN_TOP, FLEXITANK, ISOTANK, HIGH_CUBE, FLAT_RACK, REEFER, PALLET_WIDE;
}
