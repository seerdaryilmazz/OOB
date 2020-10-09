package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeNameEmbeddable {

    private String code;
    private String name;

    public CodeNameEmbeddable() {
    }

    public CodeNameEmbeddable(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public boolean isValid() {
        return (StringUtils.isNotEmpty(getCode()) && StringUtils.isNotEmpty(getName()));
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
        CodeNameEmbeddable pair = (CodeNameEmbeddable) other;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(getCode(), pair.getCode());
        builder.append(getName(), pair.getName());

        return builder.isEquals();

    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(getCode());
        builder.append(getName());

        return builder.toHashCode();
    }
}
