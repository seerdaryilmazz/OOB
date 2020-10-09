package ekol.crm.quote.queue.importq.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Max;

import org.springframework.format.annotation.DateTimeFormat;

import ekol.crm.quote.queue.importq.enums.Status;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportQueueSearchJson {

	private Long quoteNumber;
	private String externalSystemName;
	
	private List<Status> status;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate minCreateDate;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate maxCreateDate;
	
	private int page;
	
	@Max(100)
	private int pageSize = 20;

}
