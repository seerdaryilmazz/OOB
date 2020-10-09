package ekol.agreement.domain.model;

import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.util.AuditListener;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "AgreementPriceAdpModel")
@SequenceGenerator(name = "SEQ_PRICEADAPTATIONMODEL", sequenceName = "SEQ_PRICEADAPTATIONMODEL")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@AuditTable("AUD_AGRMNT_PRICE_MODEL")
@Audited
@Where(clause = "deleted = 0")
//@RevisionEntity(AuditListener.class)
public class PriceAdaptationModel extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRICEADAPTATIONMODEL")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 3)
    private Integer eur;

    @Column(precision = 3)
    private Integer usd;

    @Column(precision = 3)
    private Integer inflation;

    @Column(precision = 3)
    private Integer minimumWage;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column
    private LocalDate validityEndDate;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getName())
                .append(getEur())
                .append(getUsd())
                .append(getInflation())
                .append(getMinimumWage())
                .append(getValidityStartDate())
                .toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PriceAdaptationModel))
            return false;
        if (object == this)
            return true;

        PriceAdaptationModel entity = PriceAdaptationModel.class.cast(object);
        if(Objects.nonNull(getId()) && Objects.nonNull(entity.getId())) {
            return new EqualsBuilder().append(getId(), entity.getId()).isEquals();
        }
        return new EqualsBuilder().
                append(getId(), entity.getId()).
                append(getName(), entity.getName()).
                append(getEur(), entity.getEur()).
                append(getUsd(), entity.getUsd()).
                append(getInflation(), entity.getInflation()).
                append(getMinimumWage(), entity.getMinimumWage()).
                append(getValidityStartDate(), entity.getValidityStartDate()).
                isEquals();
    }
}
