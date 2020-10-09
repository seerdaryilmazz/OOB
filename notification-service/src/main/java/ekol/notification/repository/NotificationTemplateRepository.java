package ekol.notification.repository;

import java.util.*;
import java.util.stream.Stream;

import ekol.mongodb.domain.repository.ApplicationMongoRepository;
import ekol.notification.domain.*;

public interface NotificationTemplateRepository extends ApplicationMongoRepository<NotificationTemplate> {
	NotificationTemplate findByConcernAndChannelAndStatus(Concern concern, Channel channel, Status status);
	List<NotificationTemplate> findByConcernAndStatus(Concern concern, Status status);
	List<NotificationTemplate> findByChannelAndStatus(Channel channel, Status status);
	List<NotificationTemplate> findByConcern(Concern concern);
	List<NotificationTemplate> findByChannel(Channel channel);
}
