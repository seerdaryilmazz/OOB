package ekol.kartoteks.domain.validator;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.EmailWithType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 07/06/16.
 */
@Component
public class EmailValidator {

    public void validate(EmailWithType emailWithType){
        if(StringUtils.isBlank(emailWithType.getEmail().getEmailAddress())){
            throw new BadRequestException("Email address can not be empty");
        }
      

    }
}
