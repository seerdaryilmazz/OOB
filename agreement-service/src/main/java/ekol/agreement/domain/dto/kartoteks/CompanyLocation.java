package ekol.agreement.domain.dto.kartoteks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Set;
/**
 * Created by Dogukan Sahinturk on 11.10.2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyLocation {

    private Long id;
    private String name;
    private Set<CompanyLocationIdMapping> mappedIds;
    private boolean active;
    private boolean isDefault;
    private boolean isTemp;


}
