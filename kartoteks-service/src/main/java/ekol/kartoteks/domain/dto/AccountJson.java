package ekol.kartoteks.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.IdNamePair;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class AccountJson {

    private IdNamePair company;
    private String accountOwner;
}
