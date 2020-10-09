package ekol.crm.account.domain.enumaration;

import java.util.HashMap;
import java.util.Map;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum BlockageType {
	NO_BLOCKAGE(0),
	DUPLICATE_COMPANY(1),
	LATE_PAYMENT(2),
	LEGAL_ISSUES(3),
	COMPANY_MOVED(4),
	COMPANY_CLOSED(5)
	;
	
	private int code;
	
	private static Map<Integer, BlockageType> lookup = new HashMap<>();
	static {
        for (BlockageType d : BlockageType.values()) {
            lookup.put(d.getCode(), d);
        }
    }
	
	public static BlockageType get(Integer code) {
        return lookup.get(code);
    }
}
