package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embeddable;

/**
 * Created by burak on 13/04/17.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdNameEmbeddable {

    private Long id;
    private String name;

    public IdNameEmbeddable() {
    }

    public IdNameEmbeddable(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public boolean isValid() {
        return (getId() != null && StringUtils.isNotEmpty(getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return true;
        }
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        IdNameEmbeddable pair = (IdNameEmbeddable) other;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getId(), pair.getId());
        builder.append(getName(), pair.getName());

        return builder.isEquals();

    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(getId());
        builder.append(getName());

        return builder.toHashCode();
    }
}
