package ekol.crm.quote.domain.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.enumaration.PriceAuthorizationStatus;
import ekol.crm.quote.domain.model.PriceAuthorization;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceAuthorizationJson {
	private Long id;
	private MonetaryAmountJson minimumAmount;
	private MonetaryAmountJson calculatedAmount;
	private MonetaryAmountJson requestedAmount;
	private MonetaryAmountJson approvedAmount;
	private String requestBy;
	private UtcDateTime requestedAt;
	private String closedBy;
	private UtcDateTime closedAt;
	private PriceAuthorizationStatus closeStatus;

	public PriceAuthorization toEntity(){
		return PriceAuthorization.builder()
				.id(getId())
				.minimumAmount(Optional.ofNullable(getMinimumAmount()).map(MonetaryAmountJson::toEntity).orElse(null))
				.calculatedAmount(Optional.ofNullable(getCalculatedAmount()).map(MonetaryAmountJson::toEntity).orElse(null))
				.requestedAmount(Optional.ofNullable(getRequestedAmount()).map(MonetaryAmountJson::toEntity).orElse(null))
				.approvedAmount(Optional.ofNullable(getApprovedAmount()).map(MonetaryAmountJson::toEntity).orElse(null))
				.requestBy(getRequestBy())
				.requestedAt(getRequestedAt())
				.closedBy(getClosedBy())
				.closedAt(getClosedAt())
				.closeStatus(getCloseStatus())
				.build();
	}

	public static PriceAuthorizationJson fromEntity(PriceAuthorization priceAuthorization){
		if(Objects.isNull(priceAuthorization)) {
			return null;
		}
		
		return PriceAuthorizationJson.builder()
				.id(priceAuthorization.getId())
				.minimumAmount(MonetaryAmountJson.fromEntity(priceAuthorization.getMinimumAmount()))
				.approvedAmount(MonetaryAmountJson.fromEntity(priceAuthorization.getApprovedAmount()))
				.calculatedAmount(MonetaryAmountJson.fromEntity(priceAuthorization.getCalculatedAmount()))
				.requestedAmount(MonetaryAmountJson.fromEntity(priceAuthorization.getRequestedAmount()))
				.requestBy(priceAuthorization.getRequestBy())
				.requestedAt(priceAuthorization.getRequestedAt())
				.closedBy(priceAuthorization.getClosedBy())
				.closedAt(priceAuthorization.getClosedAt())
				.closeStatus(priceAuthorization.getCloseStatus())
				.build();
	}

}
