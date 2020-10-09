package ekol.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeNamePair implements Serializable {

    private String code;
    private String name;

    public static CodeNamePair with(String code, String name){
        return new CodeNamePair(code, name);
    }

    public CodeNamePair() {

    }
    public CodeNamePair(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String id) {
        this.code = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(getCode()) && StringUtils.isNotBlank(getName());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 23).
                append(getCode()).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CodeNamePair))
            return false;
        if (object == this)
            return true;

        CodeNamePair pair = (CodeNamePair) object;
        return new EqualsBuilder().
                append(getCode(), pair.getCode()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
