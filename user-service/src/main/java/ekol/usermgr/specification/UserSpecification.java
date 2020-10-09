package ekol.usermgr.specification;

import ekol.usermgr.domain.User;
import ekol.usermgr.domain.UserAuthenticationType;
import ekol.usermgr.domain.UserStatus;
import ekol.usermgr.domain.User_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by kilimci on 27/12/2017.
 */
public class UserSpecification{

    public static Specification<User> usernameEquals(String username) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get(User_.username), username);
            };
    }
    public static Specification<User> emailEquals(String email) {
    	return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
    		return criteriaBuilder.equal(root.get(User_.email), email);
    	};
    }
    public static Specification<User> displayNameEquals(String displayName) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(User_.displayName), displayName);
        };
    }

    public static Specification<User> displayNameLike(String displayName) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(User_.normalizedName)), "%" + displayName + "%");
        };
    }
    public static Specification<User> usernameStartsWith(String username) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(User_.username)), username.toLowerCase() + "%");
        };
    }
    public static Specification<User> statusEquals(UserStatus status) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(User_.status), status);
        };
    }
    public static Specification<User> authenticationTypeEquals(UserAuthenticationType authenticationType) {
        return (Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(User_.authenticationType), authenticationType);
        };
    }
}
