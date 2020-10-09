package ekol.crm.inbound.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
    private String documentId;
    private String documentName;
    private boolean ineffaceable = false;
}