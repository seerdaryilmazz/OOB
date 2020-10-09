package ekol.kartoteks.utils;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {

    public static List<Long> commaSeparatedNumbersToLongList(String commaSeparatedNumbers) {
        List<Long> result = new ArrayList<>();
        if (commaSeparatedNumbers != null && commaSeparatedNumbers.length() > 0) {
            for (String value : commaSeparatedNumbers.split(",")) {
                result.add(Long.valueOf(value));
            }
        }
        return result;
    }
}
