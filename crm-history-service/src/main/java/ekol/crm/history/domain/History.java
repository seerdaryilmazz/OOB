package ekol.crm.history.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "histories")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class History {
    @Id
    private String id;
    private Item item;
    private List<Change> changes;
    private Object latestData;
}
