package ekol.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StringIdCodeName {

    private String id;
    private String code;
    private String name;

    public static StringIdCodeName with(String id, String code, String name){
        return new StringIdCodeName(id, code, name);
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isEmpty(getId()) || StringUtils.isEmpty(getCode()) || StringUtils.isEmpty(getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getId()).
                append(getCode()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StringIdCodeName))
            return false;
        if (object == this)
            return true;

        StringIdCodeName pair = StringIdCodeName.class.cast(object);
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getCode(), pair.getCode()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
