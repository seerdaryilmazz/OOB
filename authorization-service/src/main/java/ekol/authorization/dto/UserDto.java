package ekol.authorization.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	
    private Long id;
	private String username;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String timeZoneId;
    private String sapNumber;
    private String office;
    private String mobileNumber;
    
    @JsonInclude(Include.NON_EMPTY)
    private List<NodeMembership> teams = new ArrayList<>();
	
}
