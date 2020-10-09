package ekol.crm.quote.queue.common.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Max;

import org.springframework.format.annotation.DateTimeFormat;

import ekol.crm.quote.queue.exportq.enums.Status;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteExportSearchJson {
	private Long quoteNumber;
	
	private List<Status> status;
	
	private Integer revisionNumber;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate minCreateDate;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate maxCreateDate;
	
	private int page;
	
	@Max(100)
	private int pageSize = 20;
}
