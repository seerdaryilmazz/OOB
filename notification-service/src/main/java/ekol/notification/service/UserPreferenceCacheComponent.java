package ekol.notification.service;

import java.util.*;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ekol.notification.domain.*;
import ekol.notification.repository.UserPreferenceRepository;
import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserPreferenceCacheComponent {
	
	private static final String CACHE_NAME = "user-preferences";
	
	private CacheManager cacheManager;
	private UserPreferenceRepository userPreferenceRepository;
	
	@PostConstruct
	private void init() {
		cacheManager.getCache(CACHE_NAME).clear();
	}

	@Cacheable(value = CACHE_NAME, key="'notification-service:user-preferences:' + #a0")
	public Set<UserPreference> getUserPreferences(String username){
		Set<UserPreference> preferences = userPreferenceRepository.findByUsername(username);
		Arrays.asList(Concern.values()).forEach(concern->{
			UserPreference preference = UserPreference.with(username, concern, Arrays.asList(Channel.values()));
			if(!preferences.contains(preference)) {
				preferences.add(preference);
			}
		});
		preferences.stream().filter(UserPreference::isNew).forEach(userPreferenceRepository::save);
		return userPreferenceRepository.findByUsername(username);
	}
	
	@Transactional
	@CacheEvict(value = CACHE_NAME, key = "'notification-service:user-preferences:' + #result.username")
	public UserPreference updateStatus(String id, Status status) {
		UserPreference preference = userPreferenceRepository.findById(id);
		preference.setStatus(status);
		preference.getChannels().entrySet().forEach(t->t.setValue(status));
		return userPreferenceRepository.save(preference);
	}
	
	@Transactional
	@CacheEvict(value = CACHE_NAME, key = "'notification-service:user-preferences:' + #result.username")
	public UserPreference updateChannelStatus(String id, Channel channel, Status status) {
		UserPreference preference = userPreferenceRepository.findById(id);
		preference.getChannels().put(channel, status);
		return userPreferenceRepository.save(preference);
	}

	public void cacheEvict(String username) {
		if(StringUtils.isEmpty(username)) {
			cacheManager.getCache(CACHE_NAME).clear();
		} else {
			cacheManager.getCache(CACHE_NAME).evict("notification-service:user-preferences:"+username);
		}
		
	}
}
