package ekol.crm.activity.validator;


import ekol.crm.activity.domain.Activity;
import ekol.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ActivityValidator {

    public void validate(Activity activity){
        if(activity.getAccount() == null || activity.getAccount().getId() == null){
            throw new ValidationException("Activity should have an account");
        }
        if(CollectionUtils.isEmpty(activity.getServiceAreas())){
            throw new ValidationException("Activity should have at least one service area");
        }
        if(activity.getScope() == null || StringUtils.isBlank(activity.getScope().getCode())){
            throw new ValidationException("Activity should have a scope");
        }
        if(activity.getType() == null || StringUtils.isBlank(activity.getType().getCode())){
            throw new ValidationException("Activity should have a type");
        }
        if(activity.getTool() == null || StringUtils.isBlank(activity.getTool().getCode())){
            throw new ValidationException("Activity should have a tool");
        }
        if(activity.getCalendar() != null){
            if(activity.getCalendar().getOrganizer() == null){
                throw new ValidationException("Calendar should have a organizer");
            }
            if(StringUtils.isBlank(activity.getCalendar().getSubject())){
                throw new ValidationException("Calendar should have a subject");
            }
            if(StringUtils.isBlank(activity.getCalendar().getLocation()) && !(activity.getTool().getName().equals("Call"))){
                throw new ValidationException("Calendar should have a location");
            }
            if(activity.getCalendar().getStartDate() == null || activity.getCalendar().getEndDate() == null && !(activity.getTool().getName().equals("Call"))){
                throw new ValidationException("Calendar should have a date interval");
            }
            if(activity.getCalendar().getEndDate()!=null && !activity.getCalendar().getStartDate().getDateTimeUtc().isBefore(activity.getCalendar().getEndDate().getDateTimeUtc())){
                throw new ValidationException("Start date should be before than end date");
            }
        }
    }

}
