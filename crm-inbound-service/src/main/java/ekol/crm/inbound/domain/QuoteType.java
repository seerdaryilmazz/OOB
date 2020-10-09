package ekol.crm.inbound.domain;

import java.util.Set;
import java.util.stream.*;

import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum QuoteType {
    SPOT(Stream.of("ROAD", "SEA", "AIR", "DTR").collect(Collectors.toSet())), 
    LONG_TERM(Stream.of("ROAD", "SEA", "AIR", "DTR","CCL").collect(Collectors.toSet())), 
    TENDER(Stream.of("ROAD").collect(Collectors.toSet())), 
    ;
	private Set<String> servicaArea;
}
