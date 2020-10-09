package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IsoNamePair {

    @Column
    private String iso;

    @Column
    private String name;
}
