package ekol.oneorder.configuration;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.oneorder.configuration.dto.*;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigurationApi {
	
	private ConfigurationService configurationService;
	
	public Map<String,Option<?>> list(Long subsidiary){
		return configurationService.list(subsidiary);
	}
	
	public <T> T get(String key, Long subsidiary, Class<T> type ){
		Option<?> option = this.list(subsidiary).get(key);
		return type.cast(option);
	}
	
	public TextOption getText(String key, Long subsidiary){
		return Optional.ofNullable(get(key, subsidiary, TextOption.class)).orElseGet(TextOption::new);
	}
	
	public NumberOption getNumber(String key, Long subsidiary){
		return Optional.ofNullable(get(key, subsidiary, NumberOption.class)).orElseGet(NumberOption::new);
	}
	
	public BooleanOption getBoolean(String key, Long subsidiary){
		return Optional.ofNullable(get(key, subsidiary, BooleanOption.class)).orElseGet(BooleanOption::new);
	}
	
	public ListOption getList(String key, Long subsidiary){
		return Optional.ofNullable(get(key, subsidiary, ListOption.class)).orElseGet(ListOption::new);
	}
	
	public <T> T evaluate(String key, Long subsidiary, Object facts, Class<T> returnType) {
		return configurationService.evaluate(key, subsidiary, facts, returnType);
	}
}
