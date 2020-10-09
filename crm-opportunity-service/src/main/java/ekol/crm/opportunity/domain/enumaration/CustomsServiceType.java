package ekol.crm.opportunity.domain.enumaration;

import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@EnumSerializableToJsonAsLookup
public enum CustomsServiceType {

    CUSTOMS_SERVICE_TYPE(Arrays.asList("IHR-1",
            "IHR-2",
            "IHR-3",
            "IHR-4",
            "IHR-5",
            "IHR-6",
            "IHR-7",
            "IHR-8",
            "ITH-1",
            "ITH-2",
            "ITH-3",
            "ITH-4",
            "ITH-5",
            "ITH-6",
            "ITH-7",
            "ITH-8",
            "ITH-11",
            "ITH-12",
            "ITH-13-a",
            "ITH-13-b",
            "ITH-13-c",
            "ITH-13-d",
            "ITH-13-e",
            "ITH-14",
            "TR-1",
            "TR-2",
            "TR-3",
            "ANT-1",
            "ANT-2",
            "ANT-3",
            "ANT-4",
            "OZ-1",
            "OZ-2",
            "OZ-3",
            "OZ-4"));

    private List<String> options;

    CustomsServiceType(List<String> options){
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
