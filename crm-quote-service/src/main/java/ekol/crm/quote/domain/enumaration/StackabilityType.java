package ekol.crm.quote.domain.enumaration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

import java.util.Arrays;
import java.util.List;

@EnumSerializableToJsonAsLookup
public enum StackabilityType {

    STACKABILITY(Arrays.asList("NOT_STACKABLE",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "MAX_AMOUNT"));

    private List<String> options;

    StackabilityType(List<String> options){
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
