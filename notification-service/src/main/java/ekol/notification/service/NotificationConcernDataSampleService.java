package ekol.notification.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.notification.domain.*;
import ekol.notification.repository.NotificationConcernDataSampleRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationConcernDataSampleService {
	private NotificationConcernDataSampleRepository notificationConcernDataSampleRepository;
	
	public NotificationConcernDataSample save(Concern concern, Object data) {
		NotificationConcernDataSample entity = Optional.ofNullable(notificationConcernDataSampleRepository.findByConcern(concern)).orElse(NotificationConcernDataSample.with(concern));
		entity.setData(data);
		return notificationConcernDataSampleRepository.save(entity);
	}
	
	public NotificationConcernDataSample get(Concern concern) {
		return Optional.ofNullable(notificationConcernDataSampleRepository.findByConcern(concern)).orElseThrow(ResourceNotFoundException::new);
	}
}
