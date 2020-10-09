package ekol.crm.configuration.domain;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Getter
@Setter
@Document(collection = "configuration-key")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationKey extends BaseEntity implements Comparable<ConfigurationKey>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	private ValueType valueType;
	private Object dataSource;
	private Set<String> authorizations = new LinkedHashSet<>();
	
	@Override
	public boolean equals(Object obj) {

    	if (!(obj instanceof ConfigurationKey))
            return false;
        if (obj == this)
            return true;

        ConfigurationKey node = ConfigurationKey.class.cast(obj);
        return new EqualsBuilder()
                .append(getId(), node.getId())
                .append(getCode(), node.getCode())
                .isEquals();
    }
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.append(getCode())
				.toHashCode();
	}

	@Override
	public int compareTo(ConfigurationKey o) {
		return Comparator
				.comparing(ConfigurationKey::getName)
				.compare(this, o);
	}
}
