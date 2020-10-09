package ekol.notification.service;

import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import ekol.exceptions.ResourceNotFoundException;
import ekol.notification.domain.*;
import ekol.notification.repository.NotificationTemplateRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationTemplateCacheComponent {
	
	private static final String CACHE_NAME = "notification-templates";

	private CacheManager cacheManager;
	private NotificationTemplateRepository notificationTemplateRepository;
	
	@PostConstruct
	private void init() {
		cacheEvict();
	}
	
	protected void assertEntityExists(String id) {
        if (!notificationTemplateRepository.exists(id)) {
            throw new ResourceNotFoundException("Entity with id {0} not found", id);
        }
    }
	
	@Cacheable(cacheNames =  CACHE_NAME)
	public List<NotificationTemplate> findAll() {
        return notificationTemplateRepository.findAll();
    }
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public NotificationTemplate add(NotificationTemplate entity) {
        return notificationTemplateRepository.save(entity);
    }
    
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public NotificationTemplate update(String id, NotificationTemplate entity) {
        assertEntityExists(id);
        return notificationTemplateRepository.save(entity);
    }
    
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public List<NotificationTemplate> updateStatus(Concern concern, Status status) {
    	List<NotificationTemplate> result = notificationTemplateRepository.findByConcern(concern);
    	result.forEach(t->t.setStatus(status));
    	return notificationTemplateRepository.save(result);
    }

	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public void delete(@PathVariable String id) {
        assertEntityExists(id);
        NotificationTemplate entity = notificationTemplateRepository.findOne(id);
        entity.setDeleted(true);
        notificationTemplateRepository.save(entity);
    }
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public NotificationTemplate patch(String id, NotificationTemplate request) {
		assertEntityExists(id);
		NotificationTemplate entity = notificationTemplateRepository.findById(id);
		Optional.of(request).map(NotificationTemplate::getAddonClass).ifPresent(entity::setAddonClass);
		Optional.of(request).map(NotificationTemplate::getAddonText).ifPresent(entity::setAddonText);
		Optional.of(request).map(NotificationTemplate::getBody).ifPresent(entity::setBody);
		Optional.of(request).map(NotificationTemplate::getChannel).ifPresent(entity::setChannel);
		Optional.of(request).map(NotificationTemplate::getConcern).ifPresent(entity::setConcern);
		Optional.of(request).map(NotificationTemplate::getContent).ifPresent(entity::setContent);
		Optional.of(request).map(NotificationTemplate::getStatus).ifPresent(entity::setStatus);
		Optional.of(request).map(NotificationTemplate::getChannelStatus).ifPresent(entity::setChannelStatus);
		Optional.of(request).map(NotificationTemplate::getSubject).ifPresent(entity::setSubject);
		Optional.of(request).map(NotificationTemplate::getUrl).ifPresent(entity::setUrl);
		return notificationTemplateRepository.save(entity);
	}
	
	public void cacheEvict() {
		cacheManager.getCache(CACHE_NAME).clear();
		
	}

}
