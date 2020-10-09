package ekol.note.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "notes")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Note extends BaseEntity{
    private CodeNamePair type;
    private String content;
}
