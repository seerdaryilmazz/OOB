package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.model.Note;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
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
public class NoteJson {

    private Long id;
    private String noteId;
    private CodeNamePair type;
    private UtcDateTime createDate;
    private String createdBy;

    public Note toEntity(){
        return Note.builder()
                .id(getId())
                .noteId(getNoteId())
                .type(getType())
                .createDate(getCreateDate())
                .createdBy(getCreatedBy()).build();
    }

    public static NoteJson fromEntity(Note note){
        return new NoteJsonBuilder()
                .id(note.getId())
                .noteId(note.getNoteId())
                .type(note.getType())
                .createDate(note.getCreateDate())
                .createdBy(note.getCreatedBy()).build();
    }

}
