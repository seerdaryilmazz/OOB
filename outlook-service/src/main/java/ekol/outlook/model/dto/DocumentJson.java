package ekol.outlook.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.outlook.model.OutlookMail;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class DocumentJson {
    private String documentId;
    private String documentName;
    private byte[] bytes;
}
