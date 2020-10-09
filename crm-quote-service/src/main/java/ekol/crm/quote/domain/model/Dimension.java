package ekol.crm.quote.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {

    @Column
    private Integer width;

    @Column
    private Integer length;

    @Column
    private Integer height;
}
