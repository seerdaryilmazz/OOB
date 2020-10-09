package ekol.agreement.domain.dto.kartoteks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Dogukan Sahinturk on 11.10.2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyImportQueue {

    private Long id;
}
