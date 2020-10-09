package ekol.authorization.dto;

import java.util.Map;
import java.util.stream.*;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum Membership {
	MANAGER(300),
	SUPERVISOR(200),
	MEMBER(100),
	;
	
	private Integer level;
	
	private static Map<Integer, Membership> levels = Stream.of(Membership.values()).collect(Collectors.toMap(Membership::getLevel, m->m));
	public static Membership fromLevel(Integer level) {
		return levels.get(level);
	}
	
}
