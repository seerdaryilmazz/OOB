package ekol.crm.account.repository.specs;

import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.enumaration.SegmentType;
import ekol.crm.account.domain.model.*;
import ekol.hibernate5.domain.embeddable.UtcDateTime_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;


public class AccountSpecification {

    public static Specifications<Account> append(Specifications<Account> appendTo, Specification<Account> spec){
        return appendTo == null ? Specifications.where(spec) : appendTo.and(spec);
    }

    public static Specification<Account> havingCountryCode(final String countryCode) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.country).get(IsoNamePair_.iso), countryCode);
            }
        };
    }

    public static Specification<Account> likeAccountName(final String name) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.like(cb.lower(root.get(Account_.name)), "%"+name.toLowerCase()+ "%");
            }
        };
    }

    public static Specification<Account> havingId (final Long id){
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.id),id);
            }
        };
    }


    public static Specification<Account> havingAccountType(final AccountType accountType) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.accountType), accountType);
            }
        };
    }

    public static Specification<Account> havingSegmentType(final SegmentType segmentType) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.segment), segmentType);
            }
        };
    }

    public static Specification<Account> havingAccountOwner(final String accountOwner) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.accountOwner), accountOwner);
            }
        };
    }

    public static Specification<Account> havingCompanyId(final List<Long> companyIds) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return root.get(Account_.company).get(IdNamePair_.id).in(companyIds);
            }
        };
    }

    public static Specification<Account> havingParentSector(final String parentSectorCode) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.parentSector).get(CodeNamePair_.code), parentSectorCode);
            }
        };
    }

    public static Specification<Account> havingSubSector(final String subSectorCode) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Account_.subSector).get(CodeNamePair_.code), subSectorCode);
            }
        };
    }

    public static Specification<Account> createdAtGreaterThanOrEqual(final LocalDateTime dateTime) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.greaterThanOrEqualTo(root.get(Account_.createdAt).get(UtcDateTime_.dateTime), dateTime);
            }
        };
    }

    public static Specification<Account> createdAtLessThanOrEqual(final LocalDateTime dateTime) {
        return new Specification<Account>() {
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.lessThanOrEqualTo(root.get(Account_.createdAt).get(UtcDateTime_.dateTime), dateTime);
            }
        };
    }

}
