package ekol.crm.inbound.domain;

import java.util.*;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Getter
@Setter
@Document(collection = "inbound-mail")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inbound extends BaseEntity {
	private Message message;
	private User owner;
	private List<Quote> quotes = new ArrayList<>();
}
