package ekol.crm.quote.domain.model;

import java.time.*;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.domain.Persistable;

import ekol.crm.quote.domain.enumaration.PriceAuthorizationStatus;
import ekol.crm.quote.util.BeanUtils;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.resource.oauth2.SessionOwner;
import lombok.*;

@Entity
@Table(name = "CrmPriceAuthorization")
@SequenceGenerator(name = "SEQ_CRMPRICEAUTHORIZATION", sequenceName = "SEQ_CRMPRICEAUTHORIZATION")
@Where(clause = "deleted = 0")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PriceAuthorization extends BaseEntity implements Persistable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CRMPRICEAUTHORIZATION")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "price_id")
	private Price price;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "minimumAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "minimumCurrency"))
	})
	private MonetaryAmount minimumAmount;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "calculatedAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "calculatedCurrency"))
	})
	private MonetaryAmount calculatedAmount;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "requestedAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "requestedCurrency"))
	})
	private MonetaryAmount requestedAmount;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "amount", column = @Column(name = "approvedAmount")),
		@AttributeOverride(name = "currency", column = @Column(name = "approvedCurrency"))
	})
	private MonetaryAmount approvedAmount;

	@CreatedBy
	private String requestBy;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "dateTime", column = @Column(name = "requestedAt"))
	})
	private UtcDateTime requestedAt;

	private String closedBy;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "dateTime", column = @Column(name = "closedAt"))
	})
	private UtcDateTime closedAt;

	@Enumerated(EnumType.STRING)
	private PriceAuthorizationStatus closeStatus;
	
	@Override
	@PrePersist
	public void prePersist() {
		super.prePersist();
		request();
		approve();
	}
	
	@Override
	@PreUpdate
	public void preUpdate() {
		super.preUpdate();
		approve();
	}
	private void request() {
		if(isNew()) {
			if(Objects.isNull(requestedAt)) {
				requestedAt = new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
			}
			if(Objects.isNull(closeStatus)) {
				closeStatus = PriceAuthorizationStatus.REQUESTED;
			}
		} 
	}
	private void approve() {
		if(Objects.nonNull(approvedAmount) && PriceAuthorizationStatus.REQUESTED == closeStatus) {
			closeStatus = PriceAuthorizationStatus.APPROVED;
			closedAt = new UtcDateTime(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
			closedBy = BeanUtils.getBean(SessionOwner.class).getCurrentUser().getUsername();
		}
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(getId());
	}
}
