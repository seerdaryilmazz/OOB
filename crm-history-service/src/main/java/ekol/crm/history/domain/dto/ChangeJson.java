package ekol.crm.history.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.domain.Change;
import lombok.*;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeJson {
    private String changedBy;
    private String changeTime;
    private String description;
    private Object changeObject;

    public static ChangeJson fromEntity(Change change){
        return new ChangeJson.ChangeJsonBuilder()
                .changedBy(change.getChangedBy())
                .changeTime(change.getChangeTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .changeObject(change.getChangeObject())
                .description(change.getDescription()).build();
    }
}
