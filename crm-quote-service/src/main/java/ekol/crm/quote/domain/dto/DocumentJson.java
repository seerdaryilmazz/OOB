package ekol.crm.quote.domain.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.Document;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DocumentJson {

    private Long id;
    private String documentId;
    private String documentName;
    private boolean ineffaceable = false;
    private UtcDateTime createDate;
    private String createdBy;
    private Set<String> emails;

    public Document toEntity(){
        return Document.builder()
                .id(getId())
                .documentId(getDocumentId())
                .documentName(getDocumentName())
                .ineffaceable(isIneffaceable())
                .createDate(getCreateDate())
                .createdBy(getCreatedBy())
                .emails(getEmails())
                .build();
    }

    public static DocumentJson fromEntity(Document document){
        return new DocumentJsonBuilder()
                .id(document.getId())
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .ineffaceable(document.isIneffaceable())
                .createDate(document.getCreateDate())
                .createdBy(document.getCreatedBy())
                .emails(document.getEmails())
                .build();
    }

}
