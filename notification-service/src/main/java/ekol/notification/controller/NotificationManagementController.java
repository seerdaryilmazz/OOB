package ekol.notification.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.notification.domain.*;
import ekol.notification.domain.dto.NotificationConcernDto;
import ekol.notification.service.*;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/management")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationManagementController {
	
	private NotificationConcernDataSampleService notificationConcernDataSampleService;
	private NotificationManagementService notificationManagementService; 
	private CacheConfiguration cacheConfiguration;
	private CacheManager cacheManager;
	
	@Authorize(operations = "notification.manage")
	@GetMapping("/concern")
	public Iterable<NotificationConcernDto> listConcerns(){
		return notificationManagementService.listConcerns();
	}
	
	@Authorize(operations = "notification.manage")
	@PatchMapping("/concern")
	public NotificationConcernDto updateStatus(@RequestParam Concern concern, @RequestParam Status status) {
		return notificationManagementService.updateStatus(concern, status);
	}
	
	@GetMapping("/cache")
	public Set<String> listCacheName() {
		return cacheConfiguration.getCacheNames().stream().collect(Collectors.toCollection(TreeSet::new));
	}
	
	@GetMapping("/cache/{cacheName}")
	public void evictCache(@PathVariable String cacheName, @RequestParam(required = false) String cacheKey) {
		if (StringUtils.isEmpty(cacheKey)) {
			cacheManager.getCache(cacheName).clear();
		} else {
			cacheManager.getCache(cacheName).evict(cacheKey);
		}
	}
	
	@Authorize(operations = "notification.manage")
	@GetMapping("/sample-data")
	public NotificationConcernDataSample getSampleData(@RequestParam Concern concern) {
		return notificationConcernDataSampleService.get(concern);
	}
}
