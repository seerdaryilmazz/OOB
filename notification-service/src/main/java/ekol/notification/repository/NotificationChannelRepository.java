package ekol.notification.repository;

import java.util.Collection;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.notification.domain.*;

public interface NotificationChannelRepository extends ApplicationMongoRepository<NotificationChannel> {
	void deleteByChannelNotIn(Collection<Channel> channels);
	NotificationChannel findByChannel(Channel channel);
}
