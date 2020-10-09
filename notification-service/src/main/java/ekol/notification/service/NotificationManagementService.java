package ekol.notification.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.notification.domain.*;
import ekol.notification.domain.dto.NotificationConcernDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationManagementService {
	
	private NotificationTemplateService notificationTemplateService;
	
	public List<NotificationConcernDto> listConcerns(){
		Map<Concern, Status> concerns = notificationTemplateService.findAll()
				.parallelStream()
				.collect(Collectors.toMap(NotificationTemplate::getConcern, NotificationTemplate::getStatus, (x, y)->x != y ? Status.ACTIVE : x));

		return Arrays.stream(Concern.values())
				.parallel()
				.map(concern->NotificationConcernDto.with(concern, Optional.ofNullable(concerns.get(concern)).orElse(Status.INACTIVE), notificationTemplateService.findTemplate(concern, null)))
				.sorted((x,y)->x.getConcern().name().compareTo(y.getConcern().name()))
				.collect(Collectors.toList());
	}

	public NotificationConcernDto updateStatus(Concern concern, Status status) {
		return notificationTemplateService.updateStatus(concern, status)
			.parallelStream()
			.collect(Collectors.toMap(NotificationTemplate::getConcern, NotificationTemplate::getStatus, (x, y)->x != y ? Status.ACTIVE : x))
			.entrySet()
			.parallelStream()
			.map(t->NotificationConcernDto.with(t.getKey(), t.getValue(), notificationTemplateService.findTemplate(t.getKey(), null)))
			.findFirst()
			.orElse(null);
	}	
}
