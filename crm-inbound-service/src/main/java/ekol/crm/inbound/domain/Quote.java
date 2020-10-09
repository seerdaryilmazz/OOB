package ekol.crm.inbound.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.*;

import ekol.json.serializers.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
	private Long id;
	private String name;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime createdAt;
	private String createdBy;
}
