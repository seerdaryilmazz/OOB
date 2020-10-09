package ekol.crm.activity.domain;

import java.time.LocalDateTime;

import ekol.model.CodeNamePair;
import lombok.*;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class Note {
    private String noteId;
    private CodeNamePair type;
    private String content;
    private String createdBy;
    private LocalDateTime createDate;
    private boolean deleted;

}
