package ekol.crm.quote.queue.importq.domain;

import java.util.Objects;

import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.queue.importq.dto.ImportQuoteJson;
import ekol.crm.quote.queue.importq.enums.Status;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=true)
@Document(collection = "ImportQuoteQueue")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportQuoteQueueItem extends BaseEntity implements Persistable<String> {
	private String externalSystemName;
	private Long quoteNumber;
	private Status status;
	private ImportQuoteJson data;
	private int retryCount;

	public void increaseRetryCount(){
		setRetryCount(getRetryCount()+1);
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(getId());
	}
}
