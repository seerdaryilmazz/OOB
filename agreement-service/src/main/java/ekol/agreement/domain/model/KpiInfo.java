package ekol.agreement.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ekol.agreement.domain.enumaration.RenewalDateType;
import ekol.agreement.domain.model.agreement.Agreement;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "AgreementKpiInfo")
@SequenceGenerator(name = "SEQ_KPIINFO", sequenceName = "SEQ_KPIINFO")
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Where(clause = "deleted = 0")
@SQLDelete(sql = "UPDATE AGREEMENT_KPI_INFO SET DELETED=1 WHERE ID=?")
public class KpiInfo extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KPIINFO")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable=false)
    private Agreement agreement;

    private String name;

    private String target;

    private String actual;

    private LocalDate lastUpdateDate;

    private Integer updatePeriod;

    @Enumerated(EnumType.STRING)
    private RenewalDateType renewalDateType;

    private LocalDate nextUpdateDate;
}

