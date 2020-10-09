package ekol.notification.domain;

import org.apache.commons.lang3.builder.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="notification-template")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationTemplate extends BaseEntity {
	
	@Indexed
	private Concern concern;
	private String subject;
	private String content;
	private File freemarkerContent;
	private String body;
	private String url;
	private String addonClass;
	private String addonText;
	private Channel channel;
	private Status status;
	private Status channelStatus;
	private TemplateType templateType;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getConcern())
				.append(getContent())
				.append(getFreemarkerContent())
				.append(getBody())
				.append(getUrl())
				.append(getAddonClass())
				.append(getAddonText())
				.append(getChannel())
				.append(getStatus())
				.append(getChannelStatus())
				.append(getTemplateType())
				.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof NotificationTemplate))
			return false;
		if (object == this)
			return true;

		NotificationTemplate entity = NotificationTemplate.class.cast(object);
		return new EqualsBuilder()
				.append(entity.getId(), getId())
				.append(entity.getConcern(), getConcern())
				.append(entity.getContent(), getContent())
				.append(entity.getFreemarkerContent(), getFreemarkerContent())
				.append(entity.getBody(), getBody())
				.append(entity.getUrl(), getUrl())
				.append(entity.getAddonClass(), getAddonClass())
				.append(entity.getAddonText(), getAddonText())
				.append(entity.getChannel(), getChannel())
				.append(entity.getStatus(), getStatus())
				.append(entity.getChannelStatus(), getChannelStatus())
				.append(entity.getTemplateType(), getTemplateType())
				.isEquals();
	}
	
}
