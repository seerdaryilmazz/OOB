package ekol.notification.service;

import java.util.Set;
import java.util.stream.*;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.notification.domain.*;
import ekol.notification.repository.NotificationChannelRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationChannelService {
	
	private NotificationChannelRepository notificationChannelRepository;
	
	@PostConstruct
	private void init() {
		Set<Channel> channels = Stream.of(Channel.values()).collect(Collectors.toSet());
		notificationChannelRepository.deleteByChannelNotIn(channels);
		
		Set<Channel> saved = notificationChannelRepository.findAll().stream().map(NotificationChannel::getChannel).collect(Collectors.toSet());
		if(!SetUtils.isEqualSet(saved, channels)) {
			SetUtils.difference(channels, saved).stream().map(channel->new NotificationChannel(channel, Status.ACTIVE)).forEach(notificationChannelRepository::save);
		}
	}
}
