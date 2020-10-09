package ekol.agreement.domain.dto;

import lombok.Data;

@Data
public class NotificationParametersJson {
	private Long day;
	private String concern;
	private String jobName;

}
