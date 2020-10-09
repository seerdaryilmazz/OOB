package ekol.note.domain.dto;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.exceptions.ValidationException;
import ekol.model.CodeNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import ekol.note.domain.Note;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class NoteJson {
    private String id;
    private CodeNamePair type;
    private String content;
    private UtcDateTime createDate;
    private String createdBy;

    public Note toEntity(){
        Note note = Note.builder()
                .type(getType())
                .content(getContent()).build();
        note.setId(getId());
        return note;
    }

    public static NoteJson fromEntity(Note note){
        return new NoteJson.NoteJsonBuilder()
                .id(note.getId())
                .type(note.getType())
                .content(note.getContent())
                .createDate(Optional.ofNullable(note.getCreatedAt()).map(UtcDateTime::withLocalTime).orElse(null))
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
