package ekol.crm.configuration.service;

import java.util.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ekol.crm.configuration.domain.*;
import ekol.crm.configuration.repository.ConfigurationRepository;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);
	
	public static final String CACHE_NAME = "config-options";
	
	private CacheManager cacheManager;
	private ConfigurationRepository configurationRepository;
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public String clearCache() {
		cacheManager.getCache(CACHE_NAME).clear();
		LOGGER.info("{} cache is evicted", CACHE_NAME);
		return HttpStatus.OK.name();
	}
	
	public List<Configuration> findAll(){
		return configurationRepository.findAll();
	}
	
	public List<Configuration> findBySubsidiary(Long subsidiaryId){
		return configurationRepository.findBySubsidiaryId(subsidiaryId);
	}

	public List<Configuration> findDefaults(){
		return configurationRepository.findBySubsidiaryIsNull();
	}
	
	public List<Configuration> findByKey(ConfigurationKey key){
		return configurationRepository.findByKey(key);
	}
	
	public Configuration findById(String id) {
		return Optional.ofNullable(configurationRepository.findById(id)).orElseThrow(ResourceNotFoundException::new);
	}
	 
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public Configuration upsert(Configuration configuration) {
		if(null == configuration.getId()) {
			return save(configuration);
		}
		return update(configuration.getId(), configuration);
	}
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public Configuration save(Configuration configuration) {
		return configurationRepository.save(configuration);
	}
	
	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public Configuration update(String id, Configuration configuration) {
		Configuration entity = findById(id);
		
		Optional.of(configuration).map(Configuration::getKey).ifPresent(entity::setKey);
		Optional.of(configuration).map(Configuration::getStatus).ifPresent(entity::setStatus);
		Optional.of(configuration).map(Configuration::getValue).ifPresent(entity::setValue);
		Optional.of(configuration).map(Configuration::getSubsidiary).ifPresent(entity::setSubsidiary);
		Optional.of(configuration).map(Configuration::getVersion).ifPresent(entity::setVersion);
		entity.setValue(configuration.getValue());
		return configurationRepository.save(entity);
	}

	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public void delete(String id) {
		configurationRepository.delete(findById(id));
	}
}
