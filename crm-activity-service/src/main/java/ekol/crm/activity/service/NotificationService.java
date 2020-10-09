package ekol.crm.activity.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.activity.domain.*;
import ekol.crm.activity.domain.dto.ActivityJson;
import ekol.crm.activity.repository.ActivityRepository;
import ekol.model.CodeNamePair;
import ekol.notification.api.NotificationApi;
import ekol.notification.dto.NotificationBuild;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {
	private ActivityRepository activityRepository;
	private NotificationApi notificationApi;
	
	public Set<CodeNamePair> notifyForExpiredActivities(String concern){
		Collection<Activity> activities = activityRepository.findByDeletedFalseAndStatus(ActivityStatus.OPEN)
				.parallelStream()
				.filter(t->Objects.isNull(t.getCalendar()) || CalendarStatus.EXPIRED == t.getCalendar().getStatus())
				.collect(Collectors.toMap(Activity::getId, t->t, (x,y)->x))
				.values();

		activities.forEach(activity->notificationApi.sendNotification(concern, ()->NotificationBuild.with(ActivityJson.fromEntity(activity), activity.getCreatedBy())));
		return activities.parallelStream().map(activity->CodeNamePair.with(activity.getId(), activity.getTool().getName())).collect(Collectors.toSet());
	}
}
