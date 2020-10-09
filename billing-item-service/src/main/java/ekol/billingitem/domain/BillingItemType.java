package ekol.billingitem.domain;

import ekol.billingitem.util.Util;
import ekol.json.serializers.common.ConverterType;
import ekol.json.serializers.common.EnumConverter;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

import java.util.Set;

@EnumSerializableToJsonAsLookup(ConverterType.SELF)
public enum BillingItemType implements EnumConverter {

    // TODO: İsimlerde COST, SURCHARGE gibi ifadeler kullanmasak daha doğru olur sanki.
    FREIGHT_COST,
    ADR_COST,
    EXPRESS_COST,
    SUPER_EXPRESS_COST,
    SPEEDY_COST, // Speedy ve Speedy XL servis tipi tamamen aynı ücretlendirildiği için sadece bir tip oluşturduk.
    COLLECTION_COST,
    DELIVERY_COST,
    COT_COST, // COT: Collection On Truck
    DOT_COST, // DOT: Delivery On Truck
    MAUT_COST, // TODO: "MAUT" kelimesi Almanca, bunun yerine "TOLL" kelimesini kullanmamız gerekmez mi?
    FUEL_COST,
    COD_CAD_COST, // COD: Cash On Delivery, CAD: Cash Against Documents
    THERMO_GOODS_TRAILER,
    GROSS_WEIGHT_SURCHARGE,
    HANGING_GARMENT_TRAILER,
    TAIL_LIFTED_TRAILER;

    /**
     * Bu listede yeralan kayıtların provizyon hesabı, bu kayıtların kullanıldığı provizyon hesabından önce yapılıyor olmalı.
     */
    public static final Set<BillingItemType> ALL_THAT_CAN_BE_INCLUDED_IN_PERCENTAGE_OF_OTHER_PRICES_CALCULATION =
            Util.hashSet(FREIGHT_COST, COLLECTION_COST, DELIVERY_COST);

    public String convert(Enum input) {
        if (input.equals(COD_CAD_COST)) {
            return "COD-CAD COST";
        } else {
            return ConverterType.UPPER_CASE.convert(input);
        }
    }

    public boolean in(BillingItemType... types) {
        return Util.hashSet(types).contains(this);
    }
}
