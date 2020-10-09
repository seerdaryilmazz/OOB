package ekol.crm.configuration.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.configuration.domain.ConfigurationKey;
import ekol.crm.configuration.repository.ConfigurationKeyRepository;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationKeyService {
	
	private ConfigurationKeyRepository configurationKeyRepository;
	
	public List<ConfigurationKey> findAll(){
		return configurationKeyRepository.findAll();
	}
	
	public ConfigurationKey findByCode(String code) {
		return configurationKeyRepository.findByCode(code);
	}
	
	public ConfigurationKey findById(String id) {
		return Optional.ofNullable(configurationKeyRepository.findById(id)).orElseThrow(ResourceNotFoundException::new);
	}
	
	public ConfigurationKey save(ConfigurationKey configurationKey) {
		return configurationKeyRepository.save(configurationKey);
	}
	
	public ConfigurationKey update(String id, ConfigurationKey configurationKey) {
		ConfigurationKey entity = findById(id);
		
		Optional.of(configurationKey).map(ConfigurationKey::getCode).ifPresent(entity::setCode);
		Optional.of(configurationKey).map(ConfigurationKey::getName).ifPresent(entity::setName);
		Optional.of(configurationKey).map(ConfigurationKey::getDataSource).ifPresent(entity::setDataSource);
		Optional.of(configurationKey).map(ConfigurationKey::getValueType).ifPresent(entity::setValueType);
		Optional.of(configurationKey).map(ConfigurationKey::getVersion).ifPresent(entity::setVersion);
		Optional.of(configurationKey).map(ConfigurationKey::getAuthorizations).ifPresent(entity::setAuthorizations);
		return configurationKeyRepository.save(entity);
	}
}
