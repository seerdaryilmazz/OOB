package ekol.location.domain.location.comnon.workinghour;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by burak on 10/04/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Time {

    private int hour;
    private int minute;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


}