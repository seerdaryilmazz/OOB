package ekol.crm.quote.domain.enumaration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

import java.util.Arrays;
import java.util.List;

@EnumSerializableToJsonAsLookup
public enum PaymentDueDays {

    DUE_DAYS(Arrays.asList("0",
            "7",
            "14",
            "21",
            "30",
            "45",
            "60",
            "90"));

    private List<String> options;

    PaymentDueDays(List<String> options){
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
