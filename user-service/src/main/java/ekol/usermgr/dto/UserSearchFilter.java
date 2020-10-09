package ekol.usermgr.dto;


import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.*;

import ekol.usermgr.domain.*;
import ekol.usermgr.specification.UserSpecification;
import lombok.Data;


/**
 * Created by kilimci on 27/12/2017.
 */
@Data
public class UserSearchFilter {

    private String username;
    private String displayName;
    private String email;
    private boolean inactiveUsers;
    private UserAuthenticationType authenticationType;

    public static UserSearchFilter createWith(String username, String displayName, String email, boolean inactiveUsers,UserAuthenticationType authenticationType){
        UserSearchFilter filter = new UserSearchFilter();
        filter.setUsername(username);
        filter.setDisplayName(Optional.ofNullable(displayName).map(StringUtils::stripAccents).map(String::toLowerCase).orElse(null));
        filter.setEmail(email);
        filter.setInactiveUsers(inactiveUsers);
        filter.setAuthenticationType(authenticationType);
        return filter;
    }

	public Specification<User> toSpecification(){
        Specifications<User> specs = null;
        if(StringUtils.isNotBlank(getUsername())){
            specs = appendSpecification(specs, UserSpecification.usernameStartsWith(getUsername()));
        }
        if(StringUtils.isNotBlank(getDisplayName())){
            specs = appendSpecification(specs, UserSpecification.displayNameLike(getDisplayName()));
        }
        if(StringUtils.isNotBlank(getEmail())){
        	specs = appendSpecification(specs, UserSpecification.emailEquals(getEmail()));
        }
        if(!isInactiveUsers()){
            specs = appendSpecification(specs, UserSpecification.statusEquals(UserStatus.ACTIVE));
        }
        if(Objects.nonNull(getAuthenticationType())) {
        	specs = appendSpecification(specs, UserSpecification.authenticationTypeEquals(getAuthenticationType()));
        }
        return specs;
    }

    private Specifications<User> appendSpecification(Specifications<User> appendTo, Specification<User> spec){
        return appendTo == null ? Specifications.where(spec) : appendTo.and(spec);
    }
}
