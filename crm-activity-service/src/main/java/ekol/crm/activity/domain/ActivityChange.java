package ekol.crm.activity.domain;

import ekol.crm.activity.domain.dto.ActivityJson;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class ActivityChange {
	private String id;
	private ActivityJson oldData;
	private ActivityJson newData;
}
