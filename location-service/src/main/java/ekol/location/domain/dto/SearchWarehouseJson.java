package ekol.location.domain.dto;

import java.util.Objects;

import javax.persistence.criteria.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.location.domain.*;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchWarehouseJson {
	private String companyLocationName;
	private String localName;
	private Country country;
	private WarehouseCustomsType customsType;
	
	private int page = 0;
	private int pageSize = 20;
	
	public <T> Specification<T> toSpecification(){
		Builder<T> builder = new Builder<>();
		if(StringUtils.isNotEmpty(getCompanyLocationName())){
			builder.and(companyLocationNameLike(getCompanyLocationName()));
		}
		if(StringUtils.isNotEmpty(getLocalName())){
			builder.and(localNameLike(getLocalName()));
		}
		if(Objects.nonNull(getCountry())){
			builder.and(countryEqual(getCountry()));
		}
		if(Objects.nonNull(getCustomsType())){
			builder.and(WarehouseCustomsType.NON_BONDED_WAREHOUSE == getCustomsType() ? customsDetailsIsNull() : customsTypeEqual(getCustomsType()));
		}
		return builder.build();
	}
	
	private <T> Specification<T> companyLocationNameLike(String companyLocationName) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.like(root.get("companyLocation").get("name"), StringUtils.wrap(companyLocationName, "%"));
	}
	private <T> Specification<T> localNameLike(String localName) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.like(root.get("localName"), StringUtils.wrap(localName, "%"));
	}
	
	private <T> Specification<T> countryEqual(Country country) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get("establishment").get("address").get("country").get("iso"), country.getIso());
	}
	
	private <T> Specification<T> customsTypeEqual(WarehouseCustomsType customsType) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->  criteriaBuilder.equal(root.get("customsDetails").get("customsType"), customsType);
	}

	private <T> Specification<T> customsDetailsIsNull() {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->  criteriaBuilder.isNull(root.get("customsDetails"));
	}

	
	
	private static class Builder<T>{

		Specifications<T> result = null;

		public void and(Specification<T> spec){
			result = result == null ? Specifications.where(spec) : result.and(spec);
		}
		public void or(Specification<T> spec){
			result = result == null ? Specifications.where(spec) : result.or(spec);
		}

		public Specification<T> build(){
			return result;
		}
	}
}
