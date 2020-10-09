package ekol.crm.account.domain.enumaration;

import java.util.*;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup
public enum TotalLogisticsPotential {

    POTENTIALS(Arrays.asList("0K€-50K€", "50K€-100K€", "100K€-300K€", "300K€-1M€", "1M€-5M€", "5M€-10M€", "10M€-15M€", "15M€-20M€", "20M€+"));

    private List<String> options;
}
