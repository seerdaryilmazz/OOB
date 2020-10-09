package ekol.crm.quote.domain.enumaration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

import java.util.Arrays;
import java.util.List;

@EnumSerializableToJsonAsLookup
public enum RiskFactor {

    FACTORS(Arrays.asList("2", "3", "4", "4.1", "4.2", "4.3","5", "5.1", "5.2", "6", "6.1", "6.2", "7", "8", "9"));

    private List<String> options;

    RiskFactor(List<String> options){
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
