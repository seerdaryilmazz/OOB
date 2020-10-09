package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.enumaration.CountryPointType;
import lombok.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Entity
@Table(name = "CrmCountryPoint")
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class CountryPoint extends AuditedBaseEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @Enumerated(EnumType.STRING)
    private CountryPointType type;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

}
