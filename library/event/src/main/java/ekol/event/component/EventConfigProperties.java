package ekol.event.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "oneorder")
public class EventConfigProperties {

    private Map<String, Object> events = new HashMap<>();

    public boolean isEnabled(String eventName){
        if(!events.containsKey(eventName)){
            return true;
        }
        Map<String, Object> eventConfig = (Map<String, Object>)events.get(eventName);
        return (boolean)eventConfig.getOrDefault("enabled", true);

    }

    public Map<String, Object> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Object> events) {
        this.events = events;
    }
}
