package ekol.notification.service;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import ekol.exceptions.ResourceNotFoundException;
import ekol.notification.domain.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationTemplateService {
	
	private NotificationTemplateCacheComponent notificationTemplateCacheComponent;
	
	public NotificationTemplate findActiveTemplate(Concern concern, Channel channel) {
		return notificationTemplateCacheComponent.findAll()
				.parallelStream()
				.filter(t -> Objects.equals(channel, t.getChannel()))
				.filter(t -> Objects.equals(concern, t.getConcern()))
				.filter(t -> Objects.equals(Status.ACTIVE, t.getStatus()))
				.findFirst()
				.orElse(null);
	}
	
	public List<NotificationTemplate> findTemplate(Channel channel, Status status) {
		Supplier<Stream<NotificationTemplate>> stream = () -> notificationTemplateCacheComponent.findAll()
				.parallelStream()
				.filter(t -> Objects.equals(channel, t.getChannel()));
		if(Objects.isNull(status)) {
			return stream.get().collect(Collectors.toList());
		}
		return stream.get().filter(t -> Objects.equals(status, t.getStatus())).collect(Collectors.toList());
	}
	
	public List<NotificationTemplate> findTemplate(Concern concern, Status status) {
		Supplier<Stream<NotificationTemplate>> stream = () -> notificationTemplateCacheComponent.findAll()
				.parallelStream()
				.filter(t -> Objects.equals(concern, t.getConcern()));
		if(Objects.isNull(status)) {
			return stream.get().collect(Collectors.toList());
		}
		return stream.get().filter(t -> Objects.equals(status, t.getStatus())).collect(Collectors.toList());
	}
	
    public NotificationTemplate add(NotificationTemplate entity) {
        return notificationTemplateCacheComponent.add(entity);
    }
    
    public NotificationTemplate update(String id, NotificationTemplate entity) {
        return notificationTemplateCacheComponent.update(id, entity);
    }
    
    public List<NotificationTemplate> updateStatus(Concern concern, Status status) {
    	return notificationTemplateCacheComponent.updateStatus(concern, status);
    }
    
    public NotificationTemplate patch(String id, NotificationTemplate entity) {
    	return notificationTemplateCacheComponent.patch(id, entity);
    }
    
    public void delete(String id) {
    	notificationTemplateCacheComponent.delete(id);
    }

    public NotificationTemplate get(@PathVariable String id) {
        return notificationTemplateCacheComponent.findAll()
        		.parallelStream()
        		.filter(t->Objects.equals(id, t.getId()))
        		.findAny()
        		.orElseThrow(ResourceNotFoundException::new);
    }
    
    public List<NotificationTemplate> findAll() {
    	return notificationTemplateCacheComponent.findAll();
    }

	public void cacheEvict() {
		notificationTemplateCacheComponent.cacheEvict();
	}
}
