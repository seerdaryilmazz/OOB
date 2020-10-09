package ekol.usermgr.dto;

import java.time.*;
import java.util.Objects;

import javax.persistence.criteria.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginSearch {
	private String username;
	private String clientId;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate endDate;
	
	private int page = 0;
	private int size = 20;

	public <T> Specification<T> toSpecification(){
		Builder<T> builder = new Builder<>();
		if(StringUtils.isNotEmpty(getUsername())){
			builder.and(equals(getUsername(), "username"));
		}
		if(StringUtils.isNotEmpty(getClientId())){
			builder.and(equals(getClientId(), "clientId"));
		}
		if(Objects.nonNull(getStartDate())) {
			builder.and(gte(getStartDate(), "loginTime"));			
		}
		if(Objects.nonNull(getEndDate())) {
			builder.and(lte(getEndDate(), "loginTime"));			
		}
		return builder.build();
	}
	
	public Pageable toPage() {
		return new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "loginTime")));
	}
	
	private <T> Specification<T> equals(String value, String field) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value);
	}
	
	private <T> Specification<T> gte(LocalDate value, String field) {
		LocalDateTime s = value.atStartOfDay();
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field).get("dateTime"), s);
	}
	
	private <T> Specification<T> lte(LocalDate value, String field) {
		LocalDateTime s = value.plusDays(1).atStartOfDay();
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(field).get("dateTime"), s);
	}
	
	private static class Builder<T>{
		Specifications<T> result = null;
		public void and(Specification<T> spec){
			result = result == null ? Specifications.where(spec) : result.and(spec);
		}
		public Specification<T> build(){
			return result;
		}
	}
}
