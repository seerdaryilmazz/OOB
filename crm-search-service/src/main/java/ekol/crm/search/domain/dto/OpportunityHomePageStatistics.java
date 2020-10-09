package ekol.crm.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dogukan Sahinturk on 27.01.2020
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class OpportunityHomePageStatistics {

    private Long totalCount;

    private List<ServiceAreaBasedCount> serviceAreaBasedCounts;
}
