package ekol.authorization.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Builder<T>{
	
	Specifications<T> result = null;
	
	public void append(Specification<T> spec){
        result = result == null ? Specifications.where(spec) : result.and(spec);
    }
	
	public Specification<T> build(){
		return result;
	}
	
}