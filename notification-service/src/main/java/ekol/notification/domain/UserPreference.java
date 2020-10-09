package ekol.notification.domain;

import java.util.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user-preference")
public class UserPreference extends BaseEntity{
	
	@Indexed
	private String username;
	private Concern concern;
	private Map<Channel, Status> channels = new EnumMap<>(Channel.class);
	private Status status;
	
	public static UserPreference with(String username, Concern concern, Collection<Channel> channels) {
		UserPreference instance = new UserPreference();
		channels.stream().forEach(t->instance.getChannels().putIfAbsent(t, Status.ACTIVE));
		instance.setConcern(concern);
		instance.setUsername(username);
		instance.setStatus(Status.ACTIVE);
		return instance;
	}
	
	public int hashCode() {
    	return new HashCodeBuilder()
    			.append(getUsername())
    			.append(getConcern())
    			.toHashCode();
    }

	@Override
	public boolean equals(Object obj) {

    	if (!(obj instanceof UserPreference))
            return false;
        if (obj == this)
            return true;

        UserPreference node = UserPreference.class.cast(obj);
        return new EqualsBuilder().
                append(getUsername(), node.getUsername()).
                append(getConcern(), node.getConcern()).
                isEquals();
    
		
	}
}
