package ekol.crm.inbound.domain;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import ekol.crm.inbound.util.BeanUtils;
import ekol.resource.oauth2.SessionOwner;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
	private String id;
	private String subject;
	private String from;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime sentTime;
	private List<Attachment> attachments;
	private String userEmail;
	
	private static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
		
		private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

	    @Override
	    public void serialize(LocalDateTime localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
	            throws IOException {
	    	
	    	String userTimezone = BeanUtils.getBean(SessionOwner.class).getCurrentUser().getTimeZoneId();
	        jsonGenerator.writeString(localDate.atZone(ZoneId.of(userTimezone)).format(FORMATTER_WITH_TIMEZONE));
	    }

	    @Override
	    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
	        if (visitor != null) visitor.expectObjectFormat(type);
	    }
	}
}
