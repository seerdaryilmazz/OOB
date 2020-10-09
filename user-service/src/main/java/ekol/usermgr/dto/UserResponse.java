package ekol.usermgr.dto;

import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.usermgr.domain.*;
import lombok.Data;

/**
 * Created by kilimci on 24/04/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private Long id;
    private String username;
    private String displayName;
    private String email;
    private UserStatus status;
    private String timeZoneId;
    private UserAuthenticationType authenticationType;
    private String sapNumber;

    public static UserResponse fromUser(User user){
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setAuthenticationType(user.getAuthenticationType());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        response.setId(user.getId());
        response.setStatus(user.getStatus());
        response.setTimeZoneId(user.getTimeZoneId());
        response.setSapNumber(user.getSapNumber());
        return response;
    }
    
    @Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getUsername())
				.append(getDisplayName())
				.append(getEmail())
				.append(getStatus())
				.append(getTimeZoneId())
				.append(getSapNumber())
				.append(getAuthenticationType())
				.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UserResponse))
			return false;
		if (object == this)
			return true;

		UserResponse entity = UserResponse.class.cast(object);
		return new EqualsBuilder()
				.append(entity.getId(), getId())
				.append(entity.getUsername(), getUsername())
				.append(entity.getDisplayName(), getDisplayName())
				.append(entity.getEmail(), getEmail())
				.append(entity.getStatus(), getStatus())
				.append(entity.getTimeZoneId(), getTimeZoneId())
				.append(entity.getSapNumber(), getSapNumber())
				.append(entity.getAuthenticationType(), getAuthenticationType())
				.isEquals();
	}
}
