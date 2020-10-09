package ekol.crm.account.domain.dto.potential;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Max;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PotentialSearchJson {
	private Long accountId;
	private String serviceArea;
	private Long fromCountryId;
	private Set<Long> fromCountryPointId;
	private Long toCountryId;
	private Set<Long> toCountryPointId;
	private String loadWeightTypeCode;
	private String shipmentLoadingTypeCode;
	private Boolean active;
	private String createdBy;
	private LocalDate minCreationDate;
	private LocalDate maxCreationDate;
	private int page;
	@Max(100)
	private int pageSize;
}
