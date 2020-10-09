package ekol.crm.quote.domain.enumaration;

import java.util.*;

import ekol.exceptions.ApplicationException;
import ekol.json.serializers.common.ConverterType;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;

@EnumSerializableToJsonAsLookup(ConverterType.INITIAL_CASE)
public enum CountryPointType {

    POSTAL("ROAD"), PORT("SEA"), AIRPORT("AIR");

    CountryPointType(String serviceAreaCode) {
        this.serviceAreaCode = serviceAreaCode;
    }

    private String serviceAreaCode;

    public String getServiceAreaCode() {
        return serviceAreaCode;
    }

    public static CountryPointType findByServiceArea(String serviceAreaCode) {
    	return Arrays.stream(CountryPointType.values())
    			.filter(t->Objects.equals(t.getServiceAreaCode(), serviceAreaCode))
    			.findFirst()
    			.orElseThrow(()->new ApplicationException("No matching element"));
    }
}
