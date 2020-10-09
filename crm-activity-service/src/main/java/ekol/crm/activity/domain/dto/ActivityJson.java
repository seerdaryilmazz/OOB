package ekol.crm.activity.domain.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.activity.domain.Activity;
import ekol.crm.activity.domain.ActivityStatus;
import ekol.exceptions.ValidationException;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class ActivityJson{
    private String id;
    private String createdBy;
    private String lastUpdatedBy;
    private IdNamePair account;
    private List<CodeNamePair> serviceAreas;
    private CodeNamePair scope;
    private CodeNamePair tool;
    private CodeNamePair type;
    private CalendarJson calendar;
    private Map<String, String> activityAttributes = new HashMap<>();
    private ActivityStatus status;
    private List<DocumentJson> documents;
    private List<NoteJson> notes;
    private UtcDateTime createdAt;
    private UtcDateTime lastUpdated;
    private boolean deleted;

    public Activity toEntity(){
    	Activity entity = new Activity();
    	entity.setId(getId());
    	entity.setAccount(getAccount());
    	entity.setServiceAreas(getServiceAreas());
    	entity.setScope(getScope());
    	entity.setTool(getTool());
    	entity.setType(getType());
    	entity.setDeleted(isDeleted());
    	entity.setCalendar(Optional.ofNullable(getCalendar()).map(CalendarJson::toEntity).orElse(null));
    	Optional.ofNullable(getActivityAttributes()).ifPresent(entity.getActivityAttributes()::putAll);
    	entity.setStatus(getStatus());
    	entity.setDocuments(!CollectionUtils.isEmpty(getDocuments()) ? getDocuments().stream().map(DocumentJson::toEntity).collect(Collectors.toList()) : null);
    	entity.setNotes(!CollectionUtils.isEmpty(getNotes()) ? getNotes().stream().map(NoteJson::toEntity).collect(Collectors.toList()) : null);
    	return entity;
    }

    public static ActivityJson fromEntity(Activity activity){
        return new ActivityJsonBuilder()
                .id(activity.getId())
                .createdBy(activity.getCreatedBy())
                .createdAt(Optional.of(activity).map(BaseEntity::getCreatedAt).map(UtcDateTime::withLocalTime).orElse(null))
                .lastUpdatedBy(activity.getLastUpdatedBy())
                .lastUpdated(Optional.of(activity).map(BaseEntity::getLastUpdated).map(UtcDateTime::withLocalTime).orElse(null))
                .account(activity.getAccount())
                .serviceAreas(activity.getServiceAreas())
                .scope(activity.getScope())
                .tool(activity.getTool())
                .type(activity.getType())
                .deleted(activity.isDeleted())
                .calendar(activity.getCalendar() != null ? CalendarJson.fromEntity(activity.getCalendar()) : null)
                .activityAttributes(activity.getActivityAttributes())
                .status(activity.getStatus())
                .documents(!CollectionUtils.isEmpty(activity.getDocuments()) ?
                        activity.getDocuments().stream()
                                .filter(document -> !document.isDeleted())
                                .map(DocumentJson::fromEntity)
                                .collect(Collectors.toList()) : null)
                .notes(!CollectionUtils.isEmpty(activity.getNotes()) ?
                        activity.getNotes().stream()
                                .filter(note -> !note.isDeleted())
                                .map(NoteJson::fromEntity)
                                .collect(Collectors.toList()) : null).build();
                
    }

    public void validate() {
        if (getAccount() == null || getAccount().getId() == null) {
            throw new ValidationException("Activity should have an account");
        }
        if (CollectionUtils.isEmpty(getServiceAreas())) {
            throw new ValidationException("Activity should have at least one service area");
        }
        if (getScope() == null || StringUtils.isBlank(getScope().getCode())) {
            throw new ValidationException("Activity should have a scope");
        }
        if (getTool() == null || StringUtils.isBlank(getTool().getCode())) {
            throw new ValidationException("Activity should have a tool");
        }
        if (getType() == null || StringUtils.isBlank(getType().getCode())) {
            throw new ValidationException("Activity should have a type");
        }
        if(getCalendar() != null){
            if(getCalendar().getOrganizer() == null){
                throw new ValidationException("Calendar should have a organizer");
            }
            if(StringUtils.isBlank(getCalendar().getSubject())){
                throw new ValidationException("Calendar should have a subject");
            }
            if(getCalendar().getStartDate() == null || getCalendar().getEndDate() == null && !(tool.getName().equals("Call"))){
            		 throw new ValidationException("Calendar should have a date interval");
            	
               
            }
            if(getCalendar().getEndDate()!=null && !getCalendar().getStartDate().getDateTimeUtc().isBefore(getCalendar().getEndDate().getDateTimeUtc())){
                throw new ValidationException("Start date should be before than end date");
            }
        }
        if(!CollectionUtils.isEmpty(getDocuments())){
            getDocuments().stream().forEach(DocumentJson::validate);
        }
        if(!CollectionUtils.isEmpty(getNotes())){
            getNotes().stream().forEach(NoteJson::validate);
        }
    }

}
