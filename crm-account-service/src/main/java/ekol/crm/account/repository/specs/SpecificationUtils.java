package ekol.crm.account.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class SpecificationUtils {

    public static <T> Specifications<T> append(Specifications<T> appendTo, Specification<T> spec) {
        return appendTo == null ? Specifications.where(spec) : appendTo.and(spec);
    }
}
