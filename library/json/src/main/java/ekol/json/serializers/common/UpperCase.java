package ekol.json.serializers.common;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by burak on 29/05/16.
 */
public class UpperCase implements EnumConverter {

    @Override
    public String convert(Enum input) {
    	String description = getDescription(input);
    	if(StringUtils.isNotBlank(description)) {
    		return description;
    	}
        String name = input.name();
        if (name.startsWith("_")) {
            name = name.replaceFirst("_", "");
        }
        return StringUtils.upperCase(name.replace('_', ' '));
    }
}
