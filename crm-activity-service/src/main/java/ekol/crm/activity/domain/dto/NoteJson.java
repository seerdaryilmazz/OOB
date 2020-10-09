package ekol.crm.activity.domain.dto;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.activity.domain.Note;
import ekol.exceptions.ValidationException;
import ekol.model.CodeNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NoteJson {
    private String noteId;
    private CodeNamePair type;
    private String content;
    private UtcDateTime createDate;
    private String createdBy;

    public Note toEntity(){
        return Note.builder()
                .noteId(getNoteId())
                .type(getType())
                .content(getContent())
                .createDate(Optional.ofNullable(getCreateDate()).map(UtcDateTime::getDateTime).orElse(null))
                .createdBy(getCreatedBy()).build();
    }

    public static NoteJson fromEntity(Note note){
        return new NoteJsonBuilder()
                .noteId(note.getNoteId())
                .type(note.getType())
                .content(note.getContent())
                .createDate(Optional.ofNullable(note.getCreateDate()).map(UtcDateTime::withLocalTime).orElse(null))
                .createdBy(note.getCreatedBy()).build();
    }

    public void validate() {
        if (getType() == null || StringUtils.isBlank(getType().getCode())) {
            throw new ValidationException("Note should have a type");
        }
        if (StringUtils.isBlank(getContent())) {
            throw new ValidationException("Note should have a content");
        }
    }

}
