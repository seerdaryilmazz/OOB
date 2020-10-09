package ekol.crm.search.config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchConfig {
    private int page = 1;
    
    @Max(value = 100)
    private int size = 10;
    
    private List<MatchFilter> matchFilters = new ArrayList<>();

    private List<String> teammates;
    
    public void nextPage() {
    	page++;
    }
    public void previousPage() {
    	page--;
    }

}