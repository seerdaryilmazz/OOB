package ekol.crm.quote.pdf;

import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelForSpotQuotePdfTemplate {

	private String language = "en";
	
	private Long id;
	private String number;
	
    private String serviceArea;

    private String accountLabel;
    private String account;

    private String accountLocationLabel;
    private String accountLocation;

    private String quoteNumberLabel;
    private String quoteNumber;

    private String quadroLabel;
    private String quadro;

    private String createdByLabel;
    private String createdBy;

    private String creationDateLabel;
    private String creationDate;

    private String validityStartDateLabel;
    private String validityStartDate;

    private String detailsTableLabel;

    private List<DetailsTableRow> detailsTableRows;

    private String specialNotes;


    private String contactGreeting;
    private String aboutCompanyHtml;
    private String generalConditionsHtml;

    private String roadLabel;
    private String seaLabel;

    private String subsidiaryName;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailsTableRow {

        private Boolean hasDifferentBackgroundColor;
        private String label;
        private String value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Price implements Comparable<Price> {

        private String billingItemDescription;
        private String amountWithCurrency;

		@Override
		public int compareTo(Price o) {
			return Comparator.comparing(Price::getBillingItemDescription).compare(this, o);
		}
    }
}
