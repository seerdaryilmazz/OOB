package ekol.notification.client.dto;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	private Long id;
	private String username;
	private String displayName;
	private String email;
	private String phoneNumber;
	private String timeZoneId;
	private String sapNumber;
	private String office;
	private String mobileNumber;
	private String thumbnailPath;
	
	public boolean isEmpty() {
		return Objects.isNull(id) && Objects.isNull(username);
	}
	
	public boolean isNotEmpty() {
		return !isEmpty();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getUsername())
				.append(getDisplayName())
				.append(getEmail())
				.append(getPhoneNumber())
				.append(getTimeZoneId())
				.append(getSapNumber())
				.append(getOffice())
				.append(getMobileNumber())
				.append(getThumbnailPath())
				.toHashCode();
	}
    
	public boolean equals(Object object) {
		if (!(object instanceof User))
			return false;
		if (object == this)
			return true;

		User node = User.class.cast(object);
		return new EqualsBuilder().
				append(getId(), node.getId()).
				append(getUsername(), node.getUsername()).
				append(getDisplayName(), node.getDisplayName()).
				append(getEmail(), node.getEmail()).
				append(getPhoneNumber(), node.getPhoneNumber()).
				append(getTimeZoneId(), node.getTimeZoneId()).
				append(getSapNumber(), node.getSapNumber()).
				append(getOffice(), node.getOffice()).
				append(getMobileNumber(), node.getMobileNumber()).
				append(getThumbnailPath(), node.getThumbnailPath()).
				isEquals();
	}
}
