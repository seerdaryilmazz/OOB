package ekol.location.domain.location.comnon;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import lombok.Data;

@Data
@Embeddable
@Access(AccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalSystemId {
	private String externalId;
	
	@JsonIgnore
	private String entityName;
	
	@Enumerated(EnumType.STRING)
	private ExternalSystem externalSystem;

	public static class CollectionDeserializer extends JsonDeserializer<Collection<ExternalSystemId>> implements ContextualDeserializer{

		private EntityNameValue entityName = null;
		private ExternalSystemValue externalSystem = null;
		
		@Override
		public Collection<ExternalSystemId> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JavaType type = ctxt.getTypeFactory().constructCollectionType(Collection.class, ExternalSystemId.class);
			Collection<ExternalSystemId> collection = p.getCodec().readValue(p, type);
			if(Objects.isNull(collection)) {
				return null;
			}
			collection.forEach(t->{
				if(StringUtils.isEmpty(t.getEntityName()) && Objects.nonNull(entityName)) {
					t.setEntityName(entityName.value());
				}
				if(Objects.isNull(t.getExternalSystem()) && Objects.nonNull(externalSystem)) {
					t.setExternalSystem(externalSystem.value());
				}
			});
			return collection;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
			if (Objects.nonNull(property)) {
				entityName = property.getAnnotation(EntityNameValue.class);
				externalSystem = property.getAnnotation(ExternalSystemValue.class);
		    }
			return this;
		}
		
	}
	
	public static class Deserializer extends JsonDeserializer<ExternalSystemId> implements ContextualDeserializer{

		private EntityNameValue entityName = null;
		private ExternalSystemValue externalSystem = null;
		
		@Override
		public ExternalSystemId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			ExternalSystemId obj = p.readValueAs(ExternalSystemId.class);
			if(Objects.nonNull(obj)) {
				return null;
			}
			
			if(StringUtils.isEmpty(obj.getEntityName()) && Objects.nonNull(entityName)) {
				obj.setEntityName(entityName.value());
			}
			if(Objects.isNull(obj.getExternalSystem()) && Objects.nonNull(externalSystem)) {
				obj.setExternalSystem(externalSystem.value());
			}
			return obj;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
			if (Objects.nonNull(property)) {
				entityName = property.getAnnotation(EntityNameValue.class);
				externalSystem = property.getAnnotation(ExternalSystemValue.class);
		    }
			return this;
		}
		
	}
	
	@Target({METHOD, FIELD})
	@Retention(RUNTIME)
	public static @interface EntityNameValue{
		public String value();
	}

	@Target({METHOD, FIELD})
	@Retention(RUNTIME)
	public static @interface ExternalSystemValue{
		public ExternalSystem value();
	}
}
