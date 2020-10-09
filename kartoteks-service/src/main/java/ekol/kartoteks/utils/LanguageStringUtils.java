package ekol.kartoteks.utils;


import java.util.*;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import ekol.kartoteks.domain.common.Country;

/**
 * Created by kilimci on 18/04/16.
 */
public class LanguageStringUtils {

    private static final String GLOBAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-0123456789.&/+()";
    private static final String TURKISH_CHARS = "ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ-0123456789.&/+()";

    private LanguageStringUtils() {

        //Default Constructor
    }

    public static boolean checkStringForCountryAllowedChars(@NotNull String textToCheck, @NotNull Country country){
        final Locale locale = country.getLocale();
        return isStringMatchesAllCharacters(textToCheck, GLOBAL_CHARS.concat(country.getAllowedChars() != null ? country.getAllowedChars() : ""), locale);
    }
    public static boolean checkStringForStandardChars(@NotNull String textToCheck, @NotNull Country country){
        final Locale locale = country.getLocale();
        
        return isStringMatchesAllCharacters(textToCheck, TURKISH_CHARS, locale);
    }

    private static boolean isStringMatchesAllCharacters(@NotNull String textToCheck, @NotNull String allowedCharacters, @NotNull Locale locale){
        Stream<Character> chars = textToCheck.chars().mapToObj(i -> (char)i);
        return chars.allMatch(c -> {
                    if(Character.isWhitespace(c)){
                        return true;
                    }
                   
                    String upper = String.valueOf(c).toUpperCase(locale);
                    return allowedCharacters.contains(upper);
                }
        );
    }

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
                .map(StringUtils::lowerCase)
                .orElse(null);
    }

}
