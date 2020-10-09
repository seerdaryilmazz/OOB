package ekol.crm.quote.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cache-cleaner")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CacheCleaner {

	private CacheManager cacheManager;
	
	@GetMapping("/clean")
	public void clean(@RequestParam(required = false) String cacheName) {
		if(Objects.isNull(cacheName)) {
			cacheManager.getCacheNames().forEach(this::clean);
		} else {
			cacheManager.getCache(cacheName).clear();
		}
	}

}
