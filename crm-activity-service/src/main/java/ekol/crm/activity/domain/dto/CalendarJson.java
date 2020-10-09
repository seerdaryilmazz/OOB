package ekol.crm.activity.domain.dto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.activity.common.FixedZoneDateTime;
import ekol.crm.activity.domain.Calendar;
import ekol.crm.activity.domain.CalendarStatus;
import ekol.model.CodeNamePair;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CalendarJson {
    private Long calendarId;
    private IndividualJson organizer;
    private List<IndividualJson> internalParticipants;
    private List<IndividualJson> externalParticipants;
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
    private CalendarStatus status;

    public Calendar toEntity(){
        return Calendar.builder()
                .calendarId(getCalendarId())
                .organizer(getOrganizer().toEntity())
                .internalParticipants(getInternalParticipants() != null ? getInternalParticipants().stream().map(IndividualJson::toEntity).collect(Collectors.toList()) : null)
                .externalParticipants(getExternalParticipants() != null ? getExternalParticipants().stream().map(IndividualJson::toEntity).collect(Collectors.toList()) : null)
                .location(getLocation())
                .meetingRoom(getMeetingRoom())
                .startDate(getStartDate())
                .endDate(Optional.ofNullable(getEndDate()).orElse(null))
                .allDayEvent(isAllDayEvent())
                .onlyWithOrganizer(isOnlyWithOrganizer())
                .shareWithAll(isShareWithAll())
                .showAs(getShowAs())
                .subject(getSubject())
                .content(getContent()).build();

    }

    public static CalendarJson fromEntity(Calendar calendar){
        return new CalendarJsonBuilder()
                .calendarId(calendar.getCalendarId())
                .organizer(IndividualJson.fromEntity(calendar.getOrganizer()))
                .internalParticipants(calendar.getInternalParticipants() != null ? calendar.getInternalParticipants().stream().map(IndividualJson::fromEntity).collect(Collectors.toList()) : null)
                .externalParticipants(calendar.getExternalParticipants() != null ? calendar.getExternalParticipants().stream().map(IndividualJson::fromEntity).collect(Collectors.toList()) : null)
                .location(calendar.getLocation())
                .meetingRoom(calendar.getMeetingRoom())
                .startDate(calendar.getStartDate())
                .endDate(Optional.ofNullable(calendar.getEndDate()).orElse(null))
                .allDayEvent(calendar.isAllDayEvent())
                .onlyWithOrganizer(calendar.isOnlyWithOrganizer())
                .shareWithAll(calendar.isShareWithAll())
                .showAs(calendar.getShowAs())
                .subject(calendar.getSubject())
                .content(calendar.getContent())
                .status(calendar.getStatus()).build();
    }

}
