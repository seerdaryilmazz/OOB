package ekol.crm.search.utils;


import lombok.NoArgsConstructor;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
public class LanguageStringUtils {

    
    public static String stripAccents(String s) {
        /**
         * Türkçe 'ı' karakterini biz ele alıyoruz, gerisini StringUtils.stripAccents hallediyor.
         */
        return StringUtils.stripAccents(s).replaceAll("\\u0131", "i");
    }

    public static String setTextForSearch(String s){
    	return Optional.ofNullable(s)
    		.map(LanguageStringUtils::stripAccents)
    		.map(t->t.replaceAll("\\.", ""))
    		.map(t->t.replaceAll("[^A-Za-z0-9 ]", " "))
    		.orElse(null);
    }
}
