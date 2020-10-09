package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.CountryStatus;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Entity
@Table(name = "CrmCountry")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Country extends AuditedBaseEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String iso;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private CountryStatus status;

    private Integer rank;

}
