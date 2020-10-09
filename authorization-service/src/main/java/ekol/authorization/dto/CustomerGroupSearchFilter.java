package ekol.authorization.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import ekol.authorization.domain.CustomerGroup;
import ekol.authorization.specification.CustomerGroupSpecification;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerGroupSearchFilter {
	String groupName;
	String companyName;
	
	public Specification<CustomerGroup> toSpecification(){
		ekol.authorization.specification.Builder<CustomerGroup> builder = CustomerGroupSpecification.builder();
        if(StringUtils.isNotBlank(getGroupName())){
            builder.append(CustomerGroupSpecification.customerGroupNameLike(getGroupName()));
        }
        if(StringUtils.isNotBlank(getCompanyName())){
        	builder.append(CustomerGroupSpecification.companyNameEquals(getCompanyName()));
        }
        return builder.build();
    }
}
