package ekol.crm.quote.queue.exportq.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.queue.common.dto.UserJson;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(value = {"newAccount"}, ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class AccountJson {
    private Long id;
    private String name;
    private IdNamePair company;
    private IdNamePair location;
    private UserJson accountOwner;
}
