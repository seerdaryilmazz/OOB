package ekol.outlook.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "event")
public class OutlookEvent {
    private String id;
    private Long sourceId;
    private String location;
    private String summary;
    private String description;
    private List<Attendee> attendees;
    private CalendarEventTime start;
    private CalendarEventTime end;
    private String visibility;
    private Boolean cancel;
    
    @Getter
    @Setter
    public static class Attendee{
        String email;
        String displayName;
        boolean organizer;
        boolean optional;

        @Override
        public boolean equals(Object o) {

            if (o == this) return true;
            if (!(o instanceof Attendee)) {
                return false;
            }

            Attendee user = (Attendee) o;

            return new EqualsBuilder()
            		.append(getEmail(), user.getEmail())
            		.append(getDisplayName(), user.getDisplayName())
            		.append(isOrganizer(), user.isOrganizer())
                    .append(isOptional(), user.isOptional())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(13, 33)
                    .append(email)
                    .append(displayName)
                    .append(organizer)
                    .append(optional)
                    .toHashCode();
        }
    }

    @Getter
    @Setter
    public static class CalendarEventTime{
    	private String date;
        private String dateTime;
        private String timeZone;

        @Override
        public boolean equals(Object o) {

            if (o == this) return true;
            if (!(o instanceof CalendarEventTime)) {
                return false;
            }

            CalendarEventTime calendarEventTime = (CalendarEventTime) o;

            return new EqualsBuilder()
            		.append(date, calendarEventTime.getDate())
                    .append(dateTime, calendarEventTime.getDateTime())
                    .append(timeZone, calendarEventTime.getTimeZone())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(19, 39)
            		.append(date)
                    .append(dateTime)
                    .append(timeZone)
                    .toHashCode();
        }
    }
}
