package ekol.orders.order.domain.dto.json;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.entity.LookupEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor(staticName="with")
@NoArgsConstructor
@EqualsAndHashCode
public class IdCodeNameTrio {
    private Long id;
    private String code;
    private String name;
    
    @JsonIgnoreProperties
    public boolean isValid() {
        if(getId() == null || StringUtils.isEmpty(getName()) || StringUtils.isEmpty(getCode())) {
            return false;
        }
        return true;
    }
    
    public static IdCodeNameTrio with(LookupEntity lookupEntity) {
    	return IdCodeNameTrio.with(lookupEntity.getId(), lookupEntity.getCode(), lookupEntity.getName());
    }
}
