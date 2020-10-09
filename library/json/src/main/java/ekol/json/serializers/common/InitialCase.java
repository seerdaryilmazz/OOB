package ekol.json.serializers.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by ozer on 03/11/16.
 */
public class InitialCase implements EnumConverter {

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
        return WordUtils.capitalizeFully(name.replace('_', ' '));
    }
}
