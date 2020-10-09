package ekol.crm.activity.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	private Long id;
	private String name;
	private IdNamePair company;
}
