package ekol.hibernate5.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ekol.exceptions.ValidationException;
import ekol.hibernate5.domain.serializer.DurationDeserializer;
import ekol.hibernate5.domain.serializer.DurationSerializer;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

/**
 * Created by burak on 07/02/17.
 */
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = DurationSerializer.class)
@JsonDeserialize(using = DurationDeserializer.class)
public class Duration {

    private static String DEFAULT_DELIMITER = ":";

    private static int HOUR_HAS_SECONDS = 3600;
    private static int MINUTE_HAS_SECONDS = 60;

    private int totalSeconds = 0;

    public Duration() {
    }

    public Duration(int seconds) {
        this.totalSeconds = seconds;
    }

    public Duration(int minutes, int seconds) {
        this.totalSeconds = minutes * MINUTE_HAS_SECONDS + seconds;
    }

    public Duration(int hours, int minutes, int seconds) {
        this.totalSeconds = hours * HOUR_HAS_SECONDS +  minutes * MINUTE_HAS_SECONDS + seconds;

    }


    public Duration(String duration) {
        this(duration, DEFAULT_DELIMITER);
    }

    public Duration(String duration, String delimiter) {

        String[] arr = duration.split(delimiter);

        if (arr.length != 3) {
            throw new ValidationException("Duration: Invalid Format, Expected format is 'HH:mm:ss'.");
        }

        int hour;
        int minute;
        int second;
        try {
            hour = Integer.parseInt(arr[0]);
            minute = Integer.parseInt(arr[1]);
            second = Integer.parseInt(arr[2]);
        } catch (Exception e) {
            throw new ValidationException("Duration: Invalid Format, Expected format is 'HH:mm:ss'.");
        }

        this.addHours(hour);

        if (minute > 59) {
            throw new ValidationException("Duration: Invalid Value, Minute can not have value greater than 59");
        }
        this.addMinutes(minute);

        if (second > 59) {
            throw new ValidationException("Duration: Invalid Value, Second can not have value greater than 59");
        }
        this.addSeconds(second);


    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public void addHours(int amount) {
       this.totalSeconds += amount * HOUR_HAS_SECONDS;
    }

    public void addMinutes(int amount) {
        this.totalSeconds += amount * MINUTE_HAS_SECONDS;
    }

    public void addSeconds(int amount) {
        this.totalSeconds += amount;
    }

    public int getHours(){
        return  this.getTotalSeconds() /  HOUR_HAS_SECONDS;
    }

    public int getMinutes(){
        return  (this.getTotalSeconds() % HOUR_HAS_SECONDS) / MINUTE_HAS_SECONDS;
    }

    public int getSeconds(){
        return (this.getTotalSeconds() % HOUR_HAS_SECONDS) % MINUTE_HAS_SECONDS;
    }


    public int getTotalHours(){
        return  this.getTotalSeconds() /  HOUR_HAS_SECONDS;
    }

    public int getTotalMinutes(){
        return  this.getTotalSeconds() / MINUTE_HAS_SECONDS;
    }

    public int getTotalSeconds(){
        return this.totalSeconds;
    }

    public void addDuration(Duration duration){
        this.setTotalSeconds(this.getTotalSeconds() + duration.getTotalSeconds());
    }

    public String getDurationAsString() {
        return this.getDurationAsString(DEFAULT_DELIMITER);
    }

    public String getDurationAsString(String delimiter) {
        return this.handleValueDisplay(this.getHours())
                + delimiter + this.handleValueDisplay(this.getMinutes())
                + delimiter + handleValueDisplay(this.getSeconds());
    }

    private String handleValueDisplay(int val) {
        if(val < 10) {
            return "0" + val;
        } else {
            return "" + val;
        }
    }

    /**
     *
     * @param duration
     * @return return the time difference between this object and given parameter in seconds
     *         longer duration this object has, positive the result
     *         longer duration the given object has, negative the result
     */
    public long compareTo(Duration duration) {
        return this.totalSeconds - duration.totalSeconds;
    }
}
