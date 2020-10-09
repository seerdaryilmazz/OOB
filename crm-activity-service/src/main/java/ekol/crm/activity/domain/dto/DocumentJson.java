package ekol.crm.activity.domain.dto;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.activity.domain.Document;
import ekol.exceptions.ValidationException;
import ekol.mongodb.domain.datetime.UtcDateTime;
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
    private String createdBy;
    private UtcDateTime createDate;

    public Document toEntity(){
        return Document.builder()
                .documentId(getDocumentId())
                .documentName(getDocumentName())
                .createDate(Optional.ofNullable(getCreateDate()).map(UtcDateTime::getDateTime).orElse(null))
                .createdBy(getCreatedBy()).build();
    }

    public static DocumentJson fromEntity(Document document){
        return new DocumentJson.DocumentJsonBuilder()
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .createDate(Optional.ofNullable(document.getCreateDate()).map(UtcDateTime::withLocalTime).orElse(null))
                .createdBy(document.getCreatedBy()).build();
    }

    public void validate() {
        if (StringUtils.isEmpty(getDocumentId())) {
            throw new ValidationException("Document should have an id");
        }
        if (StringUtils.isEmpty(getDocumentName())) {
            throw new ValidationException("Document should have a name");
        }
    }
}
