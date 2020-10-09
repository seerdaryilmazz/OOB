package ekol.crm.quote.queue.importq.validation;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import ekol.crm.quote.queue.importq.dto.ImportQuoteJson;
import ekol.exceptions.BadRequestException;

@Component
public class ImportValidation {

	public void validate(ImportQuoteJson json) {
		if(Objects.isNull(json)) {
			throw new BadRequestException("Request body is invalid or not sent");
		}
		if(StringUtils.isEmpty(json.getExternalSystemCode())) {
			throw new BadRequestException("externalSystemCode must be sent");
		}
		if(StringUtils.isEmpty(json.getExternalSystemName())) {
			throw new BadRequestException("externalSystemName must be sent");
		}
		if(Objects.isNull(json.getQuoteCode())) {
			throw new BadRequestException("quoteCode must be sent");
		}
	}
}
