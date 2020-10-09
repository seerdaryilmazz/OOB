package ekol.crm.quote.queue.exportq.domain;

import java.util.Objects;

import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.queue.exportq.dto.QuoteJson;
import ekol.crm.quote.queue.exportq.enums.DmlType;
import ekol.crm.quote.queue.exportq.enums.Status;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Document(collection = "QuoteQueue")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportQuoteQueueItem extends BaseEntity implements Persistable<String>{
	private Long quoteId;
	private Long quoteNumber;
	private QuoteJson quote;
	private DmlType operation;
	private Status status;
	private int retryCount;

	public void increaseRetryCount(){
		setRetryCount(getRetryCount()+1);
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(getId());
	}
}
