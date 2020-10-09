package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.model.Document;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;

/**
 * Created by Dogukan Sahinturk on 21.11.2019
 */
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

    public Document toEntity(){
        return Document.builder()
                .id(getId())
                .documentId(getDocumentId())
                .documentName(getDocumentName())
                .ineffaceable(isIneffaceable())
                .createDate(getCreateDate())
                .createdBy(getCreatedBy()).build();
    }

    public static DocumentJson fromEntity(Document document){
        return new DocumentJsonBuilder()
                .id(document.getId())
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .ineffaceable(document.isIneffaceable())
                .createDate(document.getCreateDate())
                .createdBy(document.getCreatedBy()).build();
    }
}