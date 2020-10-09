package ekol.crm.activity.sf.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import ekol.crm.activity.domain.*;
import ekol.model.*;
import lombok.*;

@Data
@Document(collection = "activities")
@Builder
public class Activity implements Serializable {
	@Id
	private String id;
	private String sfId;

	private Long version;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime lastUpdated;
	private String lastUpdatedBy;

	private boolean deleted;

	private IdNamePair account;
	private List<CodeNamePair> serviceAreas;
	private CodeNamePair scope;
	private CodeNamePair tool;
	private CodeNamePair type;
	private Calendar calendar;
	private ActivityStatus status;
	private String jobParam;
}
