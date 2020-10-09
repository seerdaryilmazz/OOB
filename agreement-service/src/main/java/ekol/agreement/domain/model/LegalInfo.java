package ekol.agreement.domain.model;

import ekol.agreement.domain.enumaration.ApographType;
import ekol.model.IsoNamePair;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LegalInfo {

    private String court;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "iso", column = @Column(name = "LAW_COUNTRY_ISO")),
            @AttributeOverride(name = "name", column = @Column(name = "LAW_COUNTRY_NAME"))})
    private IsoNamePair law;

    @Column(precision = 2)
    private Integer numberOfApographs;

    @Enumerated(EnumType.STRING)
    private ApographType apographType;

    @Column(precision = 2)
    private Integer numberOfPapers;

    private LocalDate terminationDate;

    @Column(length = 500)
    private String terminationReason;
}
