package ekol.crm.activity.domain;

import java.util.*;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@org.springframework.data.mongodb.core.mapping.Document(collection = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends BaseEntity implements Persistable<String> {
    private IdNamePair account;
    private List<CodeNamePair> serviceAreas;
    private CodeNamePair scope;
    private CodeNamePair tool;
    private CodeNamePair type;
    private Calendar calendar;
    private Map<String, String> activityAttributes = new HashMap<>();
    private ActivityStatus status;
    private List<Document> documents = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

	@JsonIgnore
	public boolean isNew() {
		return Objects.isNull(getId());
	}
}
