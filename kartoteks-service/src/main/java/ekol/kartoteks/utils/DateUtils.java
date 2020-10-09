package ekol.kartoteks.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by kilimci on 17/04/16.
 */
public class DateUtils {

    private  DateUtils() {
        //Default constructor.
    }

    public static LocalDate parse(String date) {
        if(StringUtils.isNotBlank(date)){
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return null;
    }
}
