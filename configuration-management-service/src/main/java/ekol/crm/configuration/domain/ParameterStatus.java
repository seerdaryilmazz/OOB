package ekol.crm.configuration.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ParameterStatus {
	ACTIVE,
	INACTIVE,
	;

}
