package ekol.crm.history.domain.dto.type;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.history.event.dto.potential.PotentialJson;
import ekol.model.CodeNamePair;
import ekol.model.IsoNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Potential {
    private Long id;
    private String fromCountry;
    private Set<String> fromPoint;
    private String toCountry;
    private Set<String> toPoint;

    public static Potential fromJson(PotentialJson json){
        return new PotentialBuilder()
                .id(json.getId())
                .fromCountry(Optional.ofNullable(json.getFromCountry()).map(IsoNamePair::getName).orElse(null))
                .fromPoint(Optional.ofNullable(json.getFromPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CodeNamePair::getName).collect(Collectors.toSet()))
                .toCountry(Optional.ofNullable(json.getToCountry()).map(IsoNamePair::getName).orElse(null))
                .toPoint(Optional.ofNullable(json.getToPoint()).map(Collection::stream).orElseGet(Stream::empty).map(CodeNamePair::getName).collect(Collectors.toSet()))
                .build();
    }
}
