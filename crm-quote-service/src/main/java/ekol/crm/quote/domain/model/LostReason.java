package ekol.crm.quote.domain.model;

import java.math.BigDecimal;

import javax.persistence.*;

import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import ekol.crm.quote.domain.enumaration.LostReasonType;
import ekol.model.IdNamePair;
import lombok.*;
import lombok.Builder;

@Entity
@Table(name = "CrmLostReason")
@SequenceGenerator(name = "SEQ_CRMLOSTREASON", sequenceName = "SEQ_CRMLOSTREASON")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class LostReason extends AuditedBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMLOSTREASON")
    private Long id;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="COMPETITOR_ID")),
            @AttributeOverride(name = "name", column=@Column(name="COMPETITOR_NAME"))})
    private IdNamePair competitor;

    @Column
    private BigDecimal competitorPrice;

    @Column
    private String competitorPriceCurrency;

    @Enumerated(EnumType.STRING)
    private LostReasonType reason;

    @Column
    private String reasonDetail;
    
    @Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getCompetitor())
				.append(getCompetitorPrice())
				.append(getCompetitorPriceCurrency())
				.append(getReason())
				.append(getReasonDetail())
				.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LostReason))
			return false;
		if (object == this)
			return true;

		LostReason entity = LostReason.class.cast(object);
		return new EqualsBuilder()
				.append(entity.getId(), getId())
				.append(entity.getCompetitor(), getCompetitor())
				.append(entity.getCompetitorPrice(), getCompetitorPrice())
				.append(entity.getCompetitorPriceCurrency(), getCompetitorPriceCurrency())
				.append(entity.getReason(), getReason())
				.append(entity.getReasonDetail(), getReasonDetail())
				.isEquals();
	}

}
