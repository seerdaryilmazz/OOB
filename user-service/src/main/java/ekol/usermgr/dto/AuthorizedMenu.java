package ekol.usermgr.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Created by kilimci on 22/04/2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizedMenu {
    private Long externalId;
    private Set<Object> viewers;
}
