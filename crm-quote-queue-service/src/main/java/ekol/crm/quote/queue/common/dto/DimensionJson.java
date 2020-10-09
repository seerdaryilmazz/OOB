package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DimensionJson {

    private Integer width;
    private Integer length;
    private Integer height;
}
