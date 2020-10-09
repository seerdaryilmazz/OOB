package ekol.json.serializers.common;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ozer on 03/11/16.
 */
public interface EnumConverter {

    String convert(Enum input);
    
    default String getDescription(Enum input) {
    	if(input instanceof EnumWithDescription){
    		String description = ((EnumWithDescription)input).getDescription();
    		if(StringUtils.isNotBlank(description)) {
    			return description;
    		}
    	}
    	return null;
    }
}
