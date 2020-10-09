package ekol.notification.service;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.ResourceNotFoundException;
import ekol.notification.domain.*;
import ekol.notification.domain.dto.*;
import ekol.notification.event.dto.EmailEvent;
import ekol.notification.repository.*;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationService {

	private SessionOwner sessionOwner;
	private NotificationRepository notificationRepository;
	private NotificationTemplateRepository notificationTemplateRepository;
	private ApplicationEventPublisher applicationEventPublisher;
	private UserPreferenceService userPreferenceService; 
	
	@Transactional
	public NotificationJson my(Channel channel){
		NotificationJson result = new NotificationJson();
		Collection<NotificationTemplate> templates = notificationTemplateRepository.findByChannel(channel);
		Iterable<Notification> notifications = list(10, templates).getContent();
		
		result.setNewNotifications(StreamSupport.stream(notifications.spliterator(), false).filter(notification->!notification.isPushed()).collect(Collectors.toList()));
		result.setLastNotifications(notifications);
		result.setUnreadCount(notificationRepository.countByUsernameAndReadFalseAndTemplateIn(getUsername(), templates));
		
		result.getNewNotifications().forEach(notification->notification.setPushed(true));
		notificationRepository.save(result.getNewNotifications());

		result.setActiveConcerns(userPreferenceService.listActiveConcerns(channel));
		return result;
	}
	
	@Transactional
	public Notification save(Notification notification) {
		Notification saved = notificationRepository.save(notification);
		applicationEventPublisher.publishEvent(NotificationEvent.with(saved));
		return saved;
	}
	
	@Transactional
	public Iterable<Notification> save(Iterable<Notification> notification) {
		Iterable<Notification> saved =  notificationRepository.save(notification);
		StreamSupport.stream(saved.spliterator(),false).map(NotificationEvent::with).forEach(applicationEventPublisher::publishEvent);
		return saved;
	}
	
	@Transactional
	public Notification read(String id, boolean read) {
		Notification notification =  get(id);
		notification.setRead(read);
		return notificationRepository.save(notification);
	}
	
	@Transactional
	public Iterable<Notification> readAll(boolean read, Channel channel) {
		Iterable<Notification> notifications = notificationRepository.findByUsernameAndReadAndTemplateIn(getUsername(), !read, notificationTemplateRepository.findByChannel(channel));
		notifications.forEach(notification->notification.setRead(read));
		return notificationRepository.save(notifications);
	}
	
	public Page<Notification> list(Integer limit, Channel channel){
		return list(limit, notificationTemplateRepository.findByChannel(channel));
	}
	
	public Notification get(String id) {
		return Optional.ofNullable(notificationRepository.findById(id)).orElseThrow(ResourceNotFoundException::new);
	}

	private Page<Notification> list(Integer limit, Collection<NotificationTemplate> templates){
		return notificationRepository.findByUsernameAndTemplateIn(getUsername(), templates, new PageRequest(0, limit, new Sort(Direction.DESC, "createdAt")));
	}
	
	private String getUsername() {
		return sessionOwner.getCurrentUser().getUsername();
	}

	public Iterable<Notification> postEmails() {
		Iterable<Notification> emailNotifications = notificationRepository.findByPushedAndTemplateIn(false, notificationTemplateRepository.findByChannel(Channel.EMAIL));
		for (Iterator<Notification> iterator = emailNotifications.iterator(); iterator.hasNext();) {
			Notification notification = iterator.next();
			applicationEventPublisher.publishEvent(new EmailEvent("NOTIFICATION", Arrays.asList(notification.getTemplate().getConcern().name()), Arrays.asList(notification.getEmail()), notification.getSubject(), String.valueOf(notification.getContent()), Boolean.TRUE));
			notification.setPushed(true);
			notification.setRead(true);
		}
		return notificationRepository.save(emailNotifications);
	}
}
