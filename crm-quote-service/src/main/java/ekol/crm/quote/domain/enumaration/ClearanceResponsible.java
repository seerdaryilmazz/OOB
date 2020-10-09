package ekol.crm.quote.domain.enumaration;

import ekol.exceptions.ValidationException;
import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
@Getter
public enum ClearanceResponsible {
    EKOL, CUSTOMER, ALL;

    private static Map<String, Map<String, Map<ClearanceResponsible, Set<String>>>> data;

    static{

        HashMap<ClearanceResponsible, Set<String>> importIncotermsMapForDeparture = new HashMap<>();
        importIncotermsMapForDeparture.put(ClearanceResponsible.EKOL, new HashSet<>(Arrays.asList("EXW")));
        importIncotermsMapForDeparture.put(ClearanceResponsible.CUSTOMER, new HashSet<>(Arrays.asList("FCA", "FOB")));
        importIncotermsMapForDeparture.put(ClearanceResponsible.ALL, new HashSet<>(Arrays.asList("EXW", "FCA", "FOB")));

        HashMap<ClearanceResponsible, Set<String>> exportIncotermsMapForDeparture = new HashMap<>();
        exportIncotermsMapForDeparture.put(ClearanceResponsible.EKOL, null);
        exportIncotermsMapForDeparture.put(ClearanceResponsible.CUSTOMER, null);
        exportIncotermsMapForDeparture.put(ClearanceResponsible.ALL, null);

        HashMap<ClearanceResponsible, Set<String>> nonTurkeyIncotermsMapForDeparture = new HashMap<>();
        nonTurkeyIncotermsMapForDeparture.put(ClearanceResponsible.EKOL, new HashSet<>(Arrays.asList("EXW")));
        nonTurkeyIncotermsMapForDeparture.put(ClearanceResponsible.CUSTOMER, new HashSet<>(Arrays.asList("FCA", "FOB")));
        nonTurkeyIncotermsMapForDeparture.put(ClearanceResponsible.ALL, new HashSet<>(Arrays.asList("EXW", "FCA", "FOB")));

        Map<String, Map<ClearanceResponsible, Set<String>>> mapForDeparture = new LinkedHashMap<>();
        mapForDeparture.put("IMPORT", importIncotermsMapForDeparture);
        mapForDeparture.put("EXPORT", exportIncotermsMapForDeparture);
        mapForDeparture.put("NON_TURKEY", nonTurkeyIncotermsMapForDeparture);


        HashMap<ClearanceResponsible, Set<String>> importIncotermsMapForArrival = new HashMap<>();
        importIncotermsMapForArrival.put(ClearanceResponsible.EKOL, null);
        importIncotermsMapForArrival.put(ClearanceResponsible.CUSTOMER, null);
        importIncotermsMapForArrival.put(ClearanceResponsible.ALL, null);

        HashMap<ClearanceResponsible, Set<String>> nonTurkeyIncotermsMapForArrival = new HashMap<>();
        nonTurkeyIncotermsMapForArrival.put(ClearanceResponsible.EKOL, null);
        nonTurkeyIncotermsMapForArrival.put(ClearanceResponsible.CUSTOMER, null);
        nonTurkeyIncotermsMapForArrival.put(ClearanceResponsible.ALL, null);

        HashMap<ClearanceResponsible, Set<String>> exportIncotermsMapForArrival = new HashMap<>();
        exportIncotermsMapForArrival.put(ClearanceResponsible.EKOL, new HashSet<>(Arrays.asList("DDP", "DDU")));
        exportIncotermsMapForArrival.put(ClearanceResponsible.CUSTOMER, new HashSet<>(Arrays.asList("CIF", "CPT", "CIP", "DAT", "DAP")));
        exportIncotermsMapForArrival.put(ClearanceResponsible.ALL, new HashSet<>(Arrays.asList("CIF", "CPT", "CIP", "DAT", "DAP", "DDP", "DDU")));

        Map<String, Map<ClearanceResponsible, Set<String>>> mapForArrival = new LinkedHashMap<>();
        mapForArrival.put("IMPORT", importIncotermsMapForArrival);
        mapForArrival.put("EXPORT", exportIncotermsMapForArrival);
        mapForArrival.put("NON_TURKEY", nonTurkeyIncotermsMapForArrival);

        data = new HashMap<>();
        data.put("DEPARTURE", mapForDeparture);
        data.put("ARRIVAL", mapForArrival);

    }

    public boolean isSuitable(String activity, String operation, String incoterm){


        if(StringUtils.isBlank(operation)){
            throw new ValidationException("Operation type should not be empty");
        }
        if(StringUtils.isBlank(activity)){
            throw new ValidationException("Activity should not be empty");
        }
        if(StringUtils.isBlank(incoterm)){
            incoterm = "";
        }

        if(this == ClearanceResponsible.ALL){
            return false;
        }

        Map<ClearanceResponsible, Set<String>> incotermsMap = data.get(activity).get(operation);
        if(CollectionUtils.isEmpty(incotermsMap)){
            return false;
        }
        Set<String> allIncoterms = incotermsMap.get(ClearanceResponsible.ALL);
        if(!CollectionUtils.isEmpty(allIncoterms) && allIncoterms.contains(incoterm)){
            Set<String> incoterms = incotermsMap.get(this);
            if(CollectionUtils.isEmpty(incoterms) || !incoterms.contains(incoterm)){
                return false;
            }
        }
        return true;
    }
}
