package ekol.kartoteks.domain.validator;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.CompanyRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 26/07/16.
 */
@Component
public class CompanyRoleValidator {

    public void validate(CompanyRole role){
        if(role.getRoleType() == null){
            throw new BadRequestException("Company role type can not be empty");
        }
        if(role.getSegmentType() == null){
            throw new BadRequestException("Company segment type can not be empty");
        }
        role.getEmployeeRelations().forEach(employeeRelation -> {
            if(StringUtils.isBlank(employeeRelation.getEmployeeAccount())){
                throw new BadRequestException(
                        String.format("Company role '%s': Please enter an account for '%s' relation",
                                role.getRoleType().getName(), employeeRelation.getRelation().getName()));
            }
        });
    }
}
