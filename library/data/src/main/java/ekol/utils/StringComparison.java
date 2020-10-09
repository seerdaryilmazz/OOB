package ekol.utils;


import java.text.Collator;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by kilimci on 13/07/16.
 */
public class StringComparison {

    public static boolean equalsIgnoreCase(String s1, String s2){
        if(s1 == null) s1 = "";
        if(s2 == null) s2 = "";
        return 0 == Collator.getInstance().compare(StringUtils.upperCase(s1), StringUtils.upperCase(s2))
        	&& 0 == Collator.getInstance().compare(StringUtils.lowerCase(s1), StringUtils.lowerCase(s2));
    }
}
