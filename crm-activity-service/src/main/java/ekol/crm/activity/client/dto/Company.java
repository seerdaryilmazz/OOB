package ekol.crm.activity.client.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company implements Serializable {
	private Long id;
	List<Contact> companyContacts;
}
