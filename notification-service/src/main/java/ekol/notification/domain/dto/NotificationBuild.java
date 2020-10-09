package ekol.notification.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.notification.domain.Concern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationBuild {
	Concern concern;
	NotificationBuildSource source;
}
