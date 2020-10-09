package ekol.crm.account.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Component
public class CrmSearchServiceClient {
	
	@Value("${oneorder.crm-search-service}")
	private String crmSearchServiceUrl;
	
	@Value("${oneorder.crm-activity-service}")
	private String activitySearchServiceUrl;
	
	@Value("${oneorder.crm-opportunity-service}")
	private String opportunitySearchServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Page<Object> searchByAccount(Long accountId, String status, String document){
		SearchConfig config = new SearchConfig()
			.addFilter(MatchFilter.builder().name("Account").val(accountId).build())
			.addFilter(MatchFilter.builder().name("Status Code").val(status).not(true).build());
		return restTemplate.postForObject(crmSearchServiceUrl + "/search/{document}/query", config, RestResponsePage.class, document);
	}
	
	public Page<Object> searchActivitiesByAccount(Long accountId){
		return restTemplate.getForObject(activitySearchServiceUrl + "/activity/search?accountId={accountId}", RestResponsePage.class, accountId );
	}
	
	@Data
	public static class SearchConfig {
		private int page = 1;
	    private int size = 10;
	    private List<MatchFilter> matchFilters = new ArrayList<>();
	    
	    public SearchConfig addFilter(MatchFilter filter) {
	    	matchFilters.add(filter);
	    	return this;
	    }
	}
	
	@Data
	@Builder
	public static class MatchFilter {
	    private String name;
	    private Object val;
	    private boolean not; 
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class RestResponsePage<T> extends PageImpl<T> {

		@JsonCreator
		public RestResponsePage(@JsonProperty("content") List<T> content,
				@JsonProperty("number") int number,
				@JsonProperty("size") int size,
				@JsonProperty("totalElements") Long totalElements) {
			super(content, new PageRequest(number, size), totalElements);
		}
	}
}
