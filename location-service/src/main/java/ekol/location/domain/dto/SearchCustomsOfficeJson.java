package ekol.location.domain.dto;

import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchCustomsOfficeJson {
	private String name;
	private String localName;
	private String customsCode;
	Boolean borderCustoms;
	Boolean freeZone;
	
	private int page = 0;
	private int pageSize = 20;
	
	public PageRequest paging() {
		return new PageRequest(page, pageSize);
	}
	
	public <T> Specification<T> toSpecification(){
		Builder<T> builder = new Builder<>();
		if(StringUtils.isNotEmpty(getName())){
			builder.and(like(getName(),"name"));
		}
		if(StringUtils.isNotEmpty(getLocalName())){
			builder.and(like(getLocalName(), "localName"));
		}
		if(StringUtils.isNotEmpty(getCustomsCode())){
			builder.and(startsWith(getCustomsCode(), "customsCode"));
		}
		if(Objects.nonNull(getBorderCustoms())){
			builder.and(equals(getBorderCustoms(), "borderCustoms"));
		}
		if(Objects.nonNull(getFreeZone())){
			builder.and(equals(getFreeZone(), "freeZone"));
		}
		return builder.build();
	}
	
	private <T> Specification<T> like(String value, String field) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.like(root.get(field), StringUtils.wrap(value, "%"));
	}
	
	private <T> Specification<T> startsWith(String value, String field) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> criteriaBuilder.like(root.get(field), StringUtils.appendIfMissing(value, "%"));
	}
	
	private <T> Specification<T> equals(Object value, String field) {
		return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) ->  criteriaBuilder.equal(root.get(field), value);
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
