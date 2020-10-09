package ekol.json.serializers.common;

import org.apache.commons.lang3.StringUtils;

public class EnumWithDescriptionConverter implements EnumConverter {

    @Override
    public String convert(Enum input) {
    	String description = getDescription(input);
    	if(StringUtils.isNotBlank(description)) {
    		return description;
    	}
        return input.name();
    }
}
