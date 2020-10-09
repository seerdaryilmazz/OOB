package ekol.crm.history.event.dto.potential;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.model.IsoNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class PotentialJson {

    private Long id;
    private IsoNamePair fromCountry;
    private Set<CodeNamePair> fromPoint;
    private IsoNamePair toCountry;
    private Set<CodeNamePair> toPoint;
    private UtcDateTime createdAt;
    private String createdBy;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;

}

