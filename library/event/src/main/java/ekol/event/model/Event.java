package ekol.event.model;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by ozer on 25/10/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private static final String REGISTER_PREFIX = "oneorder-";
    private String name;

    @JsonDeserialize(using = Event.CustomDataDeserializer.class)
    private Object data;

    private String id;
    private String producer;

    private int delay;
    private boolean transactional;

    public Event() {
    }

    public Event(String producer, String name, Object data, int delay, boolean transactional) {
        this.id = UUID.randomUUID().toString();
        this.producer = producer;
        this.name = name;
        this.data = data;
        this.delay = delay;
        this.transactional = transactional;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getRegisteredName() {
        return REGISTER_PREFIX + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    
	public boolean isTransactional() {
		return transactional;
	}

	public void setTransactional(boolean transactional) {
		this.transactional = transactional;
	}

	public static class CustomDataDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            return node.toString();
        }
    }
}
