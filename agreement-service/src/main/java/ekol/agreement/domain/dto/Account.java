package ekol.agreement.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;
import lombok.Data;

/**
 * Created by Dogukan Sahinturk on 11.10.2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private Long id;
    private String name;
    private IdNamePair company;
    private String accountOwner;
}

