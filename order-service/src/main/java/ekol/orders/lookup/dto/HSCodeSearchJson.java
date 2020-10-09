package ekol.orders.lookup.dto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import ekol.orders.lookup.domain.HSCodeExtended;
import ekol.orders.lookup.domain.HSCodeExtended_;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName="with")
public class HSCodeSearchJson {
	
	private static final char[] TURKISH_CHARS = new char[] {0x131, 0x130, 0xFC, 0xDC, 0xF6, 0xD6, 0x15F, 0x15E, 0xE7, 0xC7, 0x11F, 0x11E};
	private static final char[] ENGLISH_CHARS = new char[] {'i', 'I', 'u', 'U', 'o', 'O', 's', 'S', 'c', 'C', 'g', 'G'};
	
	private String name;
	private String code;
	private int pageSize = 10;
	private int page = 0;

	public Specification<HSCodeExtended> toSpecification(){
		Builder<HSCodeExtended> builder = new Builder<>();
		if(StringUtils.isNotBlank(getName())){
			builder.append(hsCodeDefinitionLike(getName()));
		}
		if(StringUtils.isNotBlank(getCode())){
			builder.append(hsCodeStartsWith(getCode()));
		}
		return builder.build();
	}

	private Specification<HSCodeExtended> hsCodeDefinitionLike(String definition) {
		return (Root<HSCodeExtended> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			return criteriaBuilder.like(clearTurkishCharsInCriteria(criteriaBuilder.upper(root.get(HSCodeExtended_.name)), criteriaBuilder), StringUtils.wrap(clearTurkishChars(definition.toUpperCase()), "%"));
		};
	}

	private Specification<HSCodeExtended> hsCodeStartsWith(String hsCode) {
		return (Root<HSCodeExtended> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
			return criteriaBuilder.like(root.get(HSCodeExtended_.code), hsCode + "%");
		};
	}

	private static String clearTurkishChars(String str) {
		String ret = str;
		for (int i = 0; i < TURKISH_CHARS.length; i++) {
			ret = ret.replaceAll(String.valueOf(TURKISH_CHARS[i]), String.valueOf(ENGLISH_CHARS[i]));
		}
		return ret;
	}
	private static Expression<String> clearTurkishCharsInCriteria(Expression<String> expression, CriteriaBuilder criteriaBuilder) {
		Expression<String> exp = expression;
		for (int i = 0; i < TURKISH_CHARS.length; i++) {
			exp = criteriaBuilder.function("replace", String.class, exp, criteriaBuilder.literal(String.valueOf(TURKISH_CHARS[i])), criteriaBuilder.literal(ENGLISH_CHARS[i]));
		}
		return exp;
	}

	private static class Builder<T>{

		Specifications<T> result = null;

		public void append(Specification<T> spec){
			result = result == null ? Specifications.where(spec) : result.and(spec);
		}

		public Specification<T> build(){
			return result;
		}

	}
}
