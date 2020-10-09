package ekol.crm.configuration.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.configuration.domain.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OptionService {
	
	private ConfigurationKeyService configurationKeyService;
	private ConfigurationService configurationService;
	
	public Collection<Option> list(){
		return configurationService.findAll().parallelStream().map(Option::from).collect(Collectors.toList());
	}

	public Collection<Option> list(Long subsidiaryId){
		Map<String, Configuration> defaults = configurationService.findDefaults().parallelStream().collect(Collectors.toMap(k->k.getKey().getCode(), v->v));
		Map<String, Configuration> options = configurationService.findBySubsidiary(subsidiaryId).parallelStream().collect(Collectors.toMap(k->k.getKey().getCode(), v->v));
		
		for (Map.Entry<String, Configuration> option : options.entrySet()) {
			Configuration configuration = option.getValue();
			ValueType type = configuration.getKey().getValueType();
			if(!type.getEmptyVerification().isEmpty(configuration.getValue())) {
				defaults.put(option.getKey(), configuration);
			}
		}
		
		return defaults.entrySet().parallelStream().map(Map.Entry::getValue).map(Option::from).collect(Collectors.toList());
	}
	
	public Collection<Option> list(String code){
		ConfigurationKey key = configurationKeyService.findByCode(StringUtils.upperCase(code));
		if(Objects.isNull(key)) {
			return Collections.emptyList();
		}
		return configurationService.findByKey(key).stream().map(Option::from).collect(Collectors.toList());
	}
	
	public Option get(String code, Long subsidiaryId) {
		return list(subsidiaryId)
				.parallelStream()
				.filter(t->code.equalsIgnoreCase(t.getKey()))
				.findFirst()
				.orElse(null);
	}
}
