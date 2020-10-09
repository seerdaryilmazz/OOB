package ekol.crm.account.domain.dto;

import java.io.Serializable;

import ekol.crm.account.domain.enumaration.BlockageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName="with")
public class CompanyBlockageDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4464816466523845479L;
	
	private Long companyId;
	private BlockageType blockageType;
}
