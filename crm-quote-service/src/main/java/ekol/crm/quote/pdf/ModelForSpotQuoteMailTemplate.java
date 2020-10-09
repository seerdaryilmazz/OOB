package ekol.crm.quote.pdf;

import java.util.*;

import lombok.*;

@Getter
@Setter
public class ModelForSpotQuoteMailTemplate {

	private Long id;
	private String number;
	
    private String serviceArea;

    private String accountLabel;
    private String account;

    private String accountLocationLabel;
    private String accountLocation;

    private String quadroLabel;
    private String quadro;

    private String quoteNumberLabel;
    private String quoteNumber;

    private String createdByLabel;
    private String createdBy;

    private String creationDateLabel;
    private String creationDate;

    private String validityStartDateLabel;
    private String validityStartDate;

    private String detailsTableLabel;

    private List<DetailsTableRow> detailsTableRows;

    private String contactGreeting;
    private String aboutCompanyHtml;
    private String generalConditionsHtml;

    private String roadLabel;
    private String seaLabel;

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
            this.value = value;
        }
    }

    @Getter
    @Setter
    public static class Price {

        public static final Comparator<Price> SORTER = new Comparator<Price>() {
            @Override
            public int compare(Price o1, Price o2) {
                return o1.billingItemDescription.compareTo(o2.billingItemDescription);
            }
        };

        private String billingItemDescription;

        private String amountWithCurrency;
    }
}
