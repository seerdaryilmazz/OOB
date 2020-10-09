package ekol.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

import com.fasterxml.jackson.annotation.*;

import ekol.exceptions.ApplicationException;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCodeName implements Serializable {

    private Long id;
    private String code;
    private String name;

    public static IdCodeName with(Long id, String code, String name){
        return new IdCodeName(id, code, name);
    }

    public static IdCodeName fromMap(Map<String, Object> map){
        Long id;
        String code;
        String name;

        Object idObject = map.get("id");
        Object codeObject = map.get("code");
        Object nameObject = map.get("name");

        if(idObject instanceof Integer) {
            id = Long.valueOf(String.valueOf(idObject));
        } else if(idObject instanceof Long) {
            id = (Long)idObject;
        } else {
            throw new ApplicationException("Invalid Id Type");
        }

        if(nameObject instanceof String) {
            name = (String) nameObject;
        } else {
            throw new ApplicationException("Invalid Name Type");
        }
        if(codeObject instanceof String) {
        	code = (String) codeObject;
        } else {
        	throw new ApplicationException("Invalid Code Type");
        }


        return new IdCodeName(id, code, name);
    }

    @JsonIgnore
    public boolean isValid() {
        return !(getId() == null || StringUtils.isEmpty(getCode()) || StringUtils.isEmpty(getName()));
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
        if (!(object instanceof IdCodeName))
            return false;
        if (object == this)
            return true;

        IdCodeName pair = (IdCodeName) object;
        return new EqualsBuilder().
                append(getId(), pair.getId()).
                append(getCode(), pair.getCode()).
                append(getName(), pair.getName()).
                isEquals();
    }
}
