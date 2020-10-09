package ekol.crm.activity.event.dto;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.activity.common.FixedZoneDateTime;
import ekol.crm.activity.domain.*;
import ekol.crm.activity.domain.Calendar;
import lombok.*;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutlookEvent {
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
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
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "with")
    public static class CalendarEventTime{
    	private String date;
        private String dateTime;
        private String timeZone;
        
        public static CalendarEventTime with(String timeZone) {
        	return with(null, null, timeZone);
        }

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
    
    public static OutlookEvent fromActivity(Activity activity){
    	Calendar calendar = activity.getCalendar();
    	
        OutlookEvent event = new OutlookEvent();
        event.setSourceId(calendar.getCalendarId());
        event.setDescription(calendar.getContent());
        event.setSummary(calendar.getSubject());
        event.setCancel(ActivityStatus.CANCELED == activity.getStatus());
        
        List<String> locationList = new LinkedList<>();
    	locationList.add(calendar.getLocation());
    	locationList.add(calendar.getMeetingRoom());
        event.setLocation(locationList.stream().filter(StringUtils::isNotBlank).collect(Collectors.joining(" - ")));

        List<Attendee> attendees = new ArrayList<>();
        if(calendar.isShareWithAll()){
            if(!CollectionUtils.isEmpty(calendar.getInternalParticipants())){
                calendar.getInternalParticipants().stream().filter(t->StringUtils.isNotBlank(t.getEmailAddress())).forEach(participant -> attendees.add(convertToAttendee(participant, false)));
            }
            if(!CollectionUtils.isEmpty(calendar.getExternalParticipants())){
            	calendar.getExternalParticipants().stream().filter(t->StringUtils.isNotBlank(t.getEmailAddress())).forEach(participant -> attendees.add(convertToAttendee(participant, false)));
            }
        }
        attendees.add(convertToAttendee(calendar.getOrganizer(), true));
        event.setAttendees(attendees);
        Optional.ofNullable(calendar.getStartDate()).filter(Objects::nonNull).map(t->OutlookEvent.convertToCalendarEventTime(t, calendar.isAllDayEvent())).ifPresent(event::setStart);
        Optional.ofNullable(calendar.getEndDate()).filter(Objects::nonNull).map(t->OutlookEvent.convertToCalendarEventTime(t, calendar.isAllDayEvent())).ifPresent(event::setEnd);
        return event;
    }
    
    private static CalendarEventTime convertToCalendarEventTime(FixedZoneDateTime datetime, boolean allDayEvent) {
    	if(Objects.isNull(datetime)) {
    		return null;
    	}
    	CalendarEventTime eventTime = CalendarEventTime.with(datetime.getTimeZone());
    	if(allDayEvent) {
    		eventTime.setDate(ZonedDateTime.ofInstant(datetime.getDateTimeUtc(), ZoneId.of(datetime.getTimeZone())).format(DATE_FORMATTER));
    	} else {
    		eventTime.setDateTime(ZonedDateTime.ofInstant(datetime.getDateTimeUtc(), ZoneId.of("UTC")).format(DATE_TIME_FORMATTER));
    	}
    	
    	return eventTime;
    }
    
    private static Attendee convertToAttendee(Individual individual, boolean organizer) {
    	Attendee attendee = new Attendee();
    	attendee.setDisplayName(individual.getName());
    	attendee.setEmail(individual.getEmailAddress());
    	attendee.setOrganizer(organizer);
        return attendee;
    }
}
