package ekol.location.util;

import ekol.exceptions.BadRequestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {

    public static final BigDecimal MIN_LAT = new BigDecimal("-90");
    public static final BigDecimal MAX_LAT = new BigDecimal("90");

    public static final BigDecimal MIN_LNG = new BigDecimal("-180");
    public static final BigDecimal MAX_LNG = new BigDecimal("180");

    public static void ensureValueIsGreaterThanZero(Integer value, String descriptor) {
        if (value <= 0) {
            throw new BadRequestException(descriptor + " must be greater than 0.");
        }
    }

    public static void ensureLatitudeIsValid(BigDecimal lat, String descriptor) {
        if (lat.compareTo(MIN_LAT) < 0 || lat.compareTo(MAX_LAT) > 0) {
            throw new BadRequestException(descriptor + " must be between -90 and 90.");
        }
    }

    public static void ensureLongitudeIsValid(BigDecimal lng, String descriptor) {
        if (lng.compareTo(MIN_LNG) < 0 || lng.compareTo(MAX_LNG) > 0) {
            throw new BadRequestException(descriptor + " must be between -180 and 180.");
        }
    }

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
