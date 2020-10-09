package ekol.notification.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import ekol.notification.domain.*;
import ekol.notification.domain.dto.NotificationConcernDto;
import ekol.notification.repository.*;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserPreferenceService {
	
	private SessionOwner sessionOwner;
	private UserPreferenceCacheComponent userPreferenceCacheComponent;
	private NotificationChannelRepository notificationChannelRepository;
	private NotificationTemplateRepository notificationTemplateRepository;
	private NotificationManagementService notificationManagementService;
	
	public Set<UserPreference> my(){
		Set<Concern> availableConcerns = notificationManagementService
				.listConcerns()
				.parallelStream()
				.filter(t->Status.ACTIVE == t.getStatus())
				.filter(t->CollectionUtils.isNotEmpty(t.getTemplates()))
				.map(NotificationConcernDto::getConcern)
				.collect(Collectors.toSet());
		return userPreferenceCacheComponent
				.getUserPreferences(getUsername())
				.parallelStream()
				.filter(t->availableConcerns.contains(t.getConcern()))
				.sorted(Comparator.comparing(UserPreference::getConcern))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	public Map<Concern, Set<Channel>> getActiveUserPreferences(String username){
		Map<Concern, Set<Channel>> options = new EnumMap<>(Concern.class);
		userPreferenceCacheComponent.getUserPreferences(username).parallelStream().filter(p->Status.ACTIVE == p.getStatus()).forEach(preference->{
			Set<Channel> channels = preference.getChannels().entrySet().parallelStream().filter(v->Status.ACTIVE == v.getValue()).map(Map.Entry::getKey).collect(Collectors.toSet());
			options.put(preference.getConcern(), channels);
		});
		return options;
	}
	
	public void updateStatus(String id, Status status) {
		userPreferenceCacheComponent.updateStatus(id, status);
	}
	
	public void updateChannelStatus(String id, Channel channel, Status status) {
		userPreferenceCacheComponent.updateChannelStatus(id, channel, status);
	}
	
	private String getUsername() {
		return sessionOwner.getCurrentUser().getUsername();
	}

	public void cacheEvict(String username) {
		userPreferenceCacheComponent.cacheEvict(username);
	}
	
	public Set<Concern> listActiveConcerns(@RequestParam Channel channel){
		boolean active = Optional.ofNullable(notificationChannelRepository.findByChannel(channel))
			.map(NotificationChannel::getStatus)
			.filter(Status.ACTIVE::equals)
			.isPresent();
		if(active) {
			Set<Concern> templateConcerns = notificationTemplateRepository.findByChannel(channel).parallelStream()
				.filter(t->Objects.equals(t.getChannelStatus(), Status.ACTIVE))
				.map(NotificationTemplate::getConcern)
				.collect(Collectors.toSet());
				
			Set<Concern> userConcerns =  my().parallelStream()
					.filter(p->this.filterAciveChannels(p.getChannels()).contains(channel))
					.map(UserPreference::getConcern)
					.collect(Collectors.toSet());
			return SetUtils.intersection(templateConcerns, userConcerns);
		}
		return Collections.emptySet();
	}
	
	private Set<Channel> filterAciveChannels(Map<Channel, Status> channels) {
		return channels.entrySet().parallelStream().filter(t->Objects.equals(Status.ACTIVE, t.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet());
	}
}
