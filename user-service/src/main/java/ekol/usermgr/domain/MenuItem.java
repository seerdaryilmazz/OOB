package ekol.usermgr.domain;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.*;

import lombok.Data;

@Data
public class MenuItem implements Comparable<MenuItem>, Serializable {
	private String key;
	private String name;
	private String url;
	private Integer rank;
	
	private Set<MenuItem> children;
	
	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MenuItem)) {
            return false;
        }
        MenuItem menu = MenuItem.class.cast(obj);
        return new EqualsBuilder()
        		.append(getKey(), menu.getKey())
                .append(getName(), menu.getName())
                .append(getUrl(), menu.getUrl())
                .append(getRank(), menu.getRank())
                .append(getChildren(), menu.getChildren())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
        		.append(getKey())
                .append(getName())
                .append(getUrl())
                .append(getRank())
                .append(getChildren())
                .toHashCode();
    }

	@Override
	public int compareTo(MenuItem obj) {
		return getRank().compareTo(obj.getRank());
	}
}
