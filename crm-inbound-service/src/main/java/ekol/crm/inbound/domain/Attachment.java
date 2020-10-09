package ekol.crm.inbound.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
	private String id;
	private String name;
	private byte[] content;
}
