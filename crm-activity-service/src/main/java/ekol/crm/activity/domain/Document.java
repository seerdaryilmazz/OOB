package ekol.crm.activity.domain;

import java.time.LocalDateTime;

import lombok.*;


@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class Document {
    private String documentId;
    private String documentName;
    private String createdBy;
    private LocalDateTime createDate;
    private boolean deleted;
}
