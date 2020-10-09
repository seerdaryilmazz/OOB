package ekol.notification.repository;


import java.util.Collection;

import org.springframework.data.domain.*;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.notification.domain.*;

public interface NotificationRepository extends ApplicationMongoRepository<Notification> {
	int countByUsernameAndReadFalseAndTemplateIn(String username, Collection<NotificationTemplate> templates);
	
	Page<Notification> findByUsernameAndTemplateIn(String username, Collection<NotificationTemplate> templates, Pageable pagable);
	Iterable<Notification> findByUsernameAndReadAndTemplateIn(String username, boolean read, Collection<NotificationTemplate> templates);
	Iterable<Notification> findByPushedAndTemplateIn(boolean pushed, Collection<NotificationTemplate> templates);
}
