package ekol.authorization.dto;

import java.util.Set;

import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItem {
	private String url;
	private String name;
	private Set<MenuItem> children;
	
	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MenuItem)) {
            return false;
        }
        MenuItem menu = MenuItem.class.cast(obj);
        return new EqualsBuilder()
                .append(getName(), menu.getName())
                .append(getUrl(), menu.getUrl())
                .append(getChildren(), menu.getChildren())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getName())
                .append(getUrl())
                .append(getChildren())
                .toHashCode();
    }
}
