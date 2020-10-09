package ekol.crm.inbound.service;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.inbound.client.*;
import ekol.crm.inbound.domain.QuoteType;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LookupService {
	
	private static final String[] ELIGABLE_SERVICE_AREA = new String[] {"ROAD","SEA","AIR", "DTR", "CCL"};

	private QuoteServiceClient quoteServiceClient;
	private KartoteksServiceClient kartoteksServiceClient;
	
	public Set<CodeNamePair> list(String lookup, Map<String, String> parameters){
		if("service-area".equalsIgnoreCase(lookup)) {
			return listServiceAreas();
		} 
		if("quote-type".equalsIgnoreCase(lookup)) {
			String serviceArea = parameters.get("serviceArea");
			return listQuoteTypes(serviceArea);
		}
		return Collections.emptySet();
	}
	
	private Set<CodeNamePair> listQuoteTypes(String serviceArea){
		Set<CodeNamePair> quoteTypes = quoteServiceClient.listQuoteTypes();
		if(StringUtils.isBlank(serviceArea)) {
			return quoteTypes;
		}
		return quoteTypes.stream().filter(t->QuoteType.valueOf(t.getCode()).getServicaArea().contains(serviceArea)).collect(Collectors.toSet());
	}
	
	private Set<CodeNamePair> listServiceAreas(){
		return kartoteksServiceClient.listServiceAreas().stream().filter(t->Stream.of(ELIGABLE_SERVICE_AREA).anyMatch(t.getCode()::equalsIgnoreCase)).collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
