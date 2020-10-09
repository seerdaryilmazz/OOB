package ekol.usermgr.validator;

import ekol.exceptions.ValidationException;
import ekol.usermgr.domain.User;
import ekol.usermgr.domain.UserAuthenticationType;
import ekol.usermgr.repository.UserRepository;
import ekol.usermgr.serializer.PasswordSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.TimeZone;

/**
 * Created by kilimci on 28/12/2017.
 */
@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    public void validate(User user){
        validateUsername(user);
        validateEmail(user);
        validateDisplayName(user);
        validateTimezone(user);
        validateAuthenticationType(user);
    }

    private void validateUsername(User user){
        if(StringUtils.isBlank(user.getUsername())){
            throw new ValidationException("User should have a username");
        }
        User existing = userRepository.findByUsername(user.getUsername());
        if(existing != null){
            if(!existing.getId().equals(user.getId())){
                throw new ValidationException("User with username {0} already exists", user.getUsername());
            }
        }
    }
    private void validateEmail(User user){
        if(user.getAuthenticationType().equals(UserAuthenticationType.PASSWORD)){
            return;
        }
        if(StringUtils.isBlank(user.getEmail())){
            throw new ValidationException("User should have an email");
        }
        User existing = userRepository.findByEmail(user.getEmail());
        if(existing != null){
            if(!existing.getId().equals(user.getId())){
                throw new ValidationException("User with email {0} already exists", user.getEmail());
            }
        }
    }
    private void validateDisplayName(User user){
        if(StringUtils.isBlank(user.getDisplayName())){
            throw new ValidationException("User should have a name");
        }
    }
    private void validateTimezone(User user){
        if(StringUtils.isBlank(user.getTimeZoneId())){
            throw new ValidationException("User should have a timezone");
        }
        if(!Arrays.asList(TimeZone.getAvailableIDs()).contains(user.getTimeZoneId())){
            throw new ValidationException("User has invalid timezone {0}", user.getTimeZoneId());
        }
    }
    private void validateAuthenticationType(User user){
        if(user.getAuthenticationType() == null){
            throw new ValidationException("User should have an authentication type");
        }
        if (user.getAuthenticationType() == UserAuthenticationType.ACTIVE_DIRECTORY
                && StringUtils.isNotBlank(user.getPassword())
                && !PasswordSerializer.DEFAULT_PASSWORD_DISPLAY.equals(user.getPassword())) {
            throw new ValidationException("Password can not be specified for active directory users");
        }
        if (user.getAuthenticationType() == UserAuthenticationType.PASSWORD
                && StringUtils.isBlank(user.getPassword())) {
            throw new ValidationException("Password should be specified");
        }
        if (user.getAuthenticationType() == UserAuthenticationType.PASSWORD
                && PasswordSerializer.DEFAULT_PASSWORD_DISPLAY.equals(user.getPassword())
                && user.getId() == null) {
            throw new ValidationException("Password should be specified");
        }
    }

}
