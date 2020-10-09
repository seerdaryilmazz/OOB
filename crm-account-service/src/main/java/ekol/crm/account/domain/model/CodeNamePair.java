package ekol.crm.account.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeNamePair {

    @Column
    private String code;

    @Column
    private String name;

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(code)
                .append(name)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CodeNamePair rhs = (CodeNamePair) obj;
        return new EqualsBuilder()
                .append(code, rhs.code)
                .append(name, rhs.name)
                .isEquals();
    }
}
