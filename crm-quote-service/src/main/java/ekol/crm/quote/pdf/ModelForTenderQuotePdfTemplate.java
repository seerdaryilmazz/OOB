package ekol.crm.quote.pdf;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import lombok.*;

@Getter
@Setter

public class ModelForTenderQuotePdfTemplate {
	
	private Long id;
	private String number;
	
	private String contractStartDate;
	
	private String productType;
	private String equipmentType;
	
	private String transportationType;
	private String importantPriceIssues;
	
	private String conversionFactorsLtl;
	private String dieselMechanism;
	
	private String adrFrigoExpress;
	private String demurrage;
	
	private String cancellation;
	private String kpi;
	
	private String penaltyDetail;
	private String loadUnloadFreeTimes;
	
	private String stackability;
	
	private String greetingMessage;
	
    private List<DetailsTableRow> detailsTableRows;

    
    @Getter
    @Setter
    public static class DetailsTableRow {

        private Boolean hasDifferentBackgroundColor;

        private String label;

        private String value;

        public DetailsTableRow() {
        }

        public DetailsTableRow(Boolean hasDifferentBackgroundColor, String label, String value) {
            this.hasDifferentBackgroundColor = hasDifferentBackgroundColor;
            this.label = label;
            this.value = Optional.ofNullable(value).orElse(StringUtils.EMPTY);
        }
    }

	

}
