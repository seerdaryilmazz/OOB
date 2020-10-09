package ekol.crm.activity.domain;

import java.time.ZonedDateTime;
import java.util.*;

import ekol.crm.activity.common.FixedZoneDateTime;
import ekol.model.CodeNamePair;
import lombok.*;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {
    private Long calendarId;
    private Individual organizer;
    private List<Individual> internalParticipants;
    private List<Individual> externalParticipants;
    private String location;
    private String meetingRoom;
    private FixedZoneDateTime startDate;
    private FixedZoneDateTime endDate;
    private boolean allDayEvent;
    private boolean onlyWithOrganizer;
    private boolean shareWithAll;
    private CodeNamePair showAs;
    private String subject;
    private String content;
    
    public CalendarStatus getStatus() {
    	ZonedDateTime current = ZonedDateTime.now();
    	ZonedDateTime start = Optional.ofNullable(startDate).map(FixedZoneDateTime::toZonedDateTime).orElse(null);
    	ZonedDateTime end = Optional.ofNullable(endDate).map(FixedZoneDateTime::toZonedDateTime).orElse(null);
    	if(Objects.isNull(start) && Objects.isNull(end)) {
    		return null;
    	} else if(end !=null && current.isAfter(end)) {
    		return CalendarStatus.EXPIRED;
    	} else if(current.isAfter(start)) {
    		return CalendarStatus.ONGOING;
    	} else {
    		return CalendarStatus.UPCOMING;
    	}
    }
}
