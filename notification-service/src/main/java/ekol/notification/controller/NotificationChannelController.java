package ekol.notification.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.mongodb.domain.controller.BaseController;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.notification.domain.NotificationChannel;
import ekol.notification.repository.NotificationChannelRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/notification-channel")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationChannelController extends BaseController<NotificationChannel> {
	
	private NotificationChannelRepository notificationChannelRepository;

	@Override
	protected ApplicationMongoRepository<NotificationChannel> getApplicationRepository() {
		return notificationChannelRepository;
	}
	
	@PatchMapping("/{id}")
	public NotificationChannel patch(@PathVariable String id, NotificationChannel request) {
		NotificationChannel entity = notificationChannelRepository.findById(id);
		Optional.of(request).map(NotificationChannel::getStatus).ifPresent(entity::setStatus);
		Optional.of(request).map(NotificationChannel::getChannel).ifPresent(entity::setChannel);
		return notificationChannelRepository.save(entity);
	}

}
