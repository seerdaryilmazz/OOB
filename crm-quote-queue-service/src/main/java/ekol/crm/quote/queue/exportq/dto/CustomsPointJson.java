package ekol.crm.quote.queue.exportq.dto;

import lombok.Data;

@Data
public class CustomsPointJson {
	private String clearanceResponsible;
	private String clearanceType;
	private Long customsOfficeId;
	private Long customsLocationId;
	private String externalCustomsOfficeCode;
	private String externalCustomsLocationCode;
	private String customsLocationCountry;
	private String customsLocationPostal;
}
