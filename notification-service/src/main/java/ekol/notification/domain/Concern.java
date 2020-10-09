package ekol.notification.domain;

import ekol.json.serializers.common.*;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EnumSerializableToJsonAsLookup(ConverterType.HAS_DESCRIPTION)
public enum Concern implements EnumWithDescription {
	QUOTE_QUADRO_NO_RECEIVED("Spot Quote Quadro Number is Received"),
//	ORDER_QUADRO_NO_RECEIVED("Notify me when Order No is received from QUADRO"),
	EXPIRING_CRM_QUOTES("Spot Quote will be Expired"),
	EXPIRED_CRM_QUOTES("Spot Quote had been Expired"),
	AGREEMENT_END_DATE("Agreement will be Expired"),
	AGREEMENT_INSURANCE_END_DATE("Agreement Insurance will be Expired"),
	AGREEMENT_STAMP_TAX_DUE_DATE("Agreement Stamp Tax Payment Day"),
	AGREEMENT_AUTO_RENEWAL_DATE("Agreement Auto Renewal Date Reminder"),
	AGREEMENT_LAG_END_DATE("Agreement Letter of Guarantee will be Expired"),
	AGREEMENT_UNIT_PRICE_END_DATE("Agreement Unit Price Definitions will be Expired"),
	AGREEMENT_KPI_NEXT_UPDATE_DATE("Agreement KPI Update Date Reminder"),
	ACTIVITY_END_DATE("Activity should be Completed"),
	QUOTE_PRICE_REQUEST("Approved price has been requested"),
	QUOTE_PRICE_APPROVE("Approved price has been given"),
	WON_QUOTES_NO_RELATED_ORDER("WON quotes no relation with any order "),
	ACCOUNT_OWNER_CHANGES("account was assigned from"),
	OPPORTUNITY_EXPECTED_QUOTE_DATE("Opportunity expected quote date will be expired")
	;
	
	private String description;
}
