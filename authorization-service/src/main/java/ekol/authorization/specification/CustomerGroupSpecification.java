package ekol.authorization.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import ekol.authorization.domain.CustomerGroup;
import ekol.authorization.domain.CustomerGroup_;
import ekol.authorization.dto.IdNamePair;
import ekol.authorization.dto.IdNamePair_;


public class CustomerGroupSpecification {
	
	public static Builder<CustomerGroup> builder() {
		return new Builder<CustomerGroup>();
	}

	public static Specification<CustomerGroup> customerGroupNameEquals(String customerGroupName) {
		return (Root<CustomerGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			return criteriaBuilder.equal(criteriaBuilder.lower(root.get(CustomerGroup_.name)), customerGroupName.toLowerCase());
		};
	}
	public static Specification<CustomerGroup> customerGroupNameStartsWith(String customerGroupName) {
		return (Root<CustomerGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(CustomerGroup_.name)), customerGroupName.toLowerCase() + "%");
		};
	}

	public static Specification<CustomerGroup> customerGroupNameLike(String customerGroupName) {
		return (Root<CustomerGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			return criteriaBuilder.like(criteriaBuilder.lower(root.get(CustomerGroup_.name)), StringUtils.wrap(customerGroupName.toLowerCase(), "%"));
		};
	}
	
	public static Specification<CustomerGroup> companyNameEquals(String companyName) {
		return (Root<CustomerGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			Join<CustomerGroup, IdNamePair> companiesJoin = root.join(CustomerGroup_.companies);
			return criteriaBuilder.equal(companiesJoin.get(IdNamePair_.name), companyName);
		};
	}

}
