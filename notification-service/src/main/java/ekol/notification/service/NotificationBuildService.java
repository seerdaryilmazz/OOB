package ekol.notification.service;

import java.time.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.*;
import java.util.stream.*;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import ekol.notification.client.*;
import ekol.notification.client.dto.User;
import ekol.notification.domain.*;
import ekol.notification.domain.dto.*;
import ekol.notification.event.dto.NotificationBuildEvent;
import ekol.notification.repository.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationBuildService {
	
	private static final String PARAM_ENTITY_URL = "entityUrl";
	private static final String PARAM_GATEWAY_URL = "gatewayUrl";
	private static final String PARAM_TARGET_USER = "targetUser";
	private static final String PARAM_TODAY = "today";
	private static final String PARAM_NOW = "now";
	
	private FreemarkerComponent freemarkerComponent;
	private NotificationTemplateService notificationTemplateService;
	private NotificationRepository notificationRepository;
	private NotificationChannelRepository notificationChannelRepository;
	private UserPreferenceService userSettingService;
	private UserServiceClient userServiceClient; 
	private AuthorizationServiceClient authorizationServiceClient;
	private NotificationConcernDataSampleService notificationConcernDataSampleService;
	private ApplicationEventPublisher publisher;
	
	public void buildAsync(NotificationBuild build) {
		publisher.publishEvent(NotificationBuildEvent.with(build));
	}
	
	public List<Notification> build(NotificationBuild build) {
		notificationConcernDataSampleService.save(build.getConcern(), build.getSource().getData());
		
		build.getSource().setTargetUsers(this.consolidateUser(build.getSource()));
		return notificationTemplateService.findTemplate(build.getConcern(), Status.ACTIVE)
			.stream()
			.filter(this::filterByChannel)
			.flatMap(template->this.build(template, build.getSource()))
			.collect(Collectors.toList());
	}
	
	private Set<User> consolidateUser(NotificationBuildSource source) {
		Supplier<Stream<User>> userStream = ()->Optional.ofNullable(source.getTargetUsers())
				.orElseGet(Collections::emptySet)
				.stream()
				.filter(User::isNotEmpty)
				.map(this::getUser);

		Supplier<Stream<User>> authOperUserStrem = ()->Optional.ofNullable(source.getAuthOperations())
				.orElseGet(Collections::emptySet)
				.stream()
				.map(authorizationServiceClient::getUser)
				.flatMap(Collection::stream);

		return Stream.concat(userStream.get(), authOperUserStrem.get())
				.filter(Objects::nonNull)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
	
	private User getUser(User user) {
		if(Objects.nonNull(user.getId())) {
			return userServiceClient.getUser(user.getId());
		}
		return userServiceClient.getUser(user.getUsername()); 
	}
		
	private Stream<Notification> build(NotificationTemplate template, NotificationBuildSource builderJson) {
		return builderJson.getTargetUsers().stream()
				.filter(user->this.filterByUserPreferences(user, template))
				.map(user->this.build(template, user,  builderJson.getData()))
				.filter(Objects::nonNull);
	}
	
	private Notification build(NotificationTemplate template, User user, Object data) {
		Map<String, Object> params = extractParameters(template, data, user);
		
		Notification notification = new Notification();
		notification.setTemplate(template);
		notification.setUsername(user.getUsername());
		notification.setEmail(user.getEmail());
		notification.setUrl(Optional.ofNullable(params.get(PARAM_ENTITY_URL)).filter(Objects::nonNull).map(String::valueOf).filter(StringUtils::isNotEmpty).orElse(StrSubstitutor.replace(template.getUrl(), params)));
		notification.setSubject(StrSubstitutor.replace(template.getSubject(), params));
		notification.setContent(this.buildContent(template, data, params));
		Optional.ofNullable(template.getBody()).filter(StringUtils::isNotEmpty).ifPresent(v->notification.setBody(StrSubstitutor.replace(v, params)));
		return notificationRepository.save(notification);
	}
	
	private String buildContent(NotificationTemplate template, Object data, Map<String, Object> params) {
		if(TemplateType.FREEMARKER_TEMPLATE == template.getTemplateType()) {
			return freemarkerComponent.process(template.getConcern(), data);
		}
		return StrSubstitutor.replace(template.getContent(), params);
	}
	
	private boolean filterByChannel(NotificationTemplate template) {
		return Optional.ofNullable(notificationChannelRepository.findByChannel(template.getChannel()))
			.map(NotificationChannel::getStatus)
			.filter(Status.ACTIVE::equals)
			.isPresent();
	}
	
	private boolean filterByUserPreferences(User user, NotificationTemplate template) {
		Set<Channel> channels = userSettingService.getActiveUserPreferences(user.getUsername()).get(template.getConcern());
		if(Objects.isNull(channels)) {
			return false;
		}else if(Channel.WEB_PUSH == template.getChannel()) {
			return true;
		}
		return channels.contains(template.getChannel());
	}
	
	private Map<String, Object> extractParameters(NotificationTemplate template, Object json, User user){
		StringBuilder builder = new StringBuilder();
		Optional.ofNullable(template.getSubject()).ifPresent(builder::append);
		Optional.ofNullable(template.getContent()).ifPresent(builder::append);
		Optional.ofNullable(template.getUrl()).ifPresent(builder::append);
		Optional.ofNullable(template.getBody()).ifPresent(builder::append);
		
		final Matcher matcher = Pattern.compile("\\$\\{(.+?)\\}").matcher(builder.toString());
		Map<String, Object> params = new HashMap<>();
		while (matcher.find()) {
			String ph = matcher.group(1);
			params.put(ph, read(json, ph));
		}
		params.put(PARAM_GATEWAY_URL, userServiceClient.getGatewayUrl());
		params.put(PARAM_ENTITY_URL, generateUrl(template, params));
		params.put(PARAM_TARGET_USER, user.getDisplayName());
		params.put(PARAM_TODAY, ZonedDateTime.now(ZoneId.of(user.getTimeZoneId())).toLocalDate());
		params.put(PARAM_NOW, ZonedDateTime.now(ZoneId.of(user.getTimeZoneId())));
		return params;
	}
	
	private String generateUrl(NotificationTemplate template, Map<String, Object>params) {
		String url = StrSubstitutor.replace(template.getUrl(), params);
		if(Channel.EMAIL == template.getChannel()) {
			return userServiceClient.getGatewayUrl() + url; 
		}
		return url;
	}
	
	private String read(Object json, String path) {
		try {
			return JsonPath.parse(new ObjectMapper().writeValueAsString(json)).read("$." + path, String.class);
		} catch (Exception e) {
			return StringUtils.EMPTY; 
		}
	}
}
