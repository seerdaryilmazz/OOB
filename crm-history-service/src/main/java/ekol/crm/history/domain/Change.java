package ekol.crm.history.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Change {
    private String changedBy;
    private LocalDateTime changeTime;
    private String description;
    private Object changeObject;
}
