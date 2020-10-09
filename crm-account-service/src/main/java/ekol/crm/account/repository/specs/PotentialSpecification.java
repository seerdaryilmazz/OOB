package ekol.crm.account.repository.specs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.*;
import ekol.crm.account.domain.model.potential.*;
import ekol.hibernate5.domain.embeddable.UtcDateTime_;

public class PotentialSpecification {
	
	private PotentialSpecification() {
		throw new UnsupportedOperationException();
	}

    public static Specification<Potential> accountIdEqual(final Long accountId) {
    	return (root, criteriaQuery, cb) -> cb.equal(root.get(Potential_.account).get(Account_.id), accountId);
    }

    public static Specification<Potential> serviceAreaEqual(final String serviceArea) {
    	return (root, criteriaQuery, cb) -> cb.equal(root.get(Potential_.serviceArea), serviceArea);
    }

    public static Specification<Potential> fromCountryIdEqual(final Long fromCountryId) {
    	return (root, criteriaQuery, cb) -> cb.equal(root.get(Potential_.fromCountry).get(Country_.id), fromCountryId);
    }

    public static Specification<Potential> fromCountryPointIdIn(final Set<Long> fromCountryPointId) {
    	if(fromCountryPointId.isEmpty()) {
    		return (root, criteriaQuery, cb) -> cb.isEmpty(root.get(Potential_.fromCountryPoint));
    	} else {
    		return (root, criteriaQuery, cb) -> {
    			criteriaQuery.distinct(true);
    			return root.join(Potential_.fromCountryPoint, JoinType.LEFT).get(CountryPoint_.id).in(fromCountryPointId);
    		};
    	}
    }

    public static Specification<Potential> toCountryIdEqual(final Long toCountryId) {
    	return (root, criteriaQuery, cb) -> cb.equal(root.get(Potential_.toCountry).get(Country_.id), toCountryId);
    }

    public static Specification<Potential> toCountryPointIdIn(final Set<Long> toCountryPointId) {
    	if(toCountryPointId.isEmpty()) {
    		return (root, criteriaQuery, cb) -> cb.isEmpty(root.get(Potential_.toCountryPoint));
    	} else {
    		return (root, criteriaQuery, cb) -> {
    			criteriaQuery.distinct(true);
    			return root.join(Potential_.toCountryPoint, JoinType.LEFT).get(CountryPoint_.id).in(toCountryPointId);
    		};
    	}
    }

    /**
     * İşin içinde inheritance olduğunda koşulların nasıl ekleneceği burada açıklanıyor: https://stackoverflow.com/a/48530552
     */
    public static Specification<Potential> loadWeightTypeIn(final List<LoadWeightType> loadWeightTypes) {
    	return (root, criteriaQuery, cb) -> cb.treat(root, RoadPotential.class).get(RoadPotential_.loadWeightType).in(loadWeightTypes);
    }

    public static Specification<Potential> shipmentLoadingTypesIn(final List<ShipmentLoadingType> shipmentLoadingTypes) {
    	return (root, criteriaQuery, cb) -> root.join(Potential_.shipmentLoadingTypes).in(shipmentLoadingTypes);
    }

    public static Specification<Potential> validityPeriodContains(final LocalDate date) {
    	return (root, criteriaQuery, cb) -> cb.and(
                cb.or(
                    cb.isNull(root.get(Potential_.validityStartDate)),
                    cb.lessThanOrEqualTo(root.get(Potential_.validityStartDate), date)
                ),
                cb.or(
                    cb.isNull(root.get(Potential_.validityEndDate)),
                    cb.greaterThanOrEqualTo(root.get(Potential_.validityEndDate), date)
                )
        );
    }

    public static Specification<Potential> validityPeriodDoesNotContain(final LocalDate date) {
    	return (root, criteriaQuery, cb) -> cb.or(
                cb.greaterThan(root.get(Potential_.validityStartDate), date),
                cb.lessThan(root.get(Potential_.validityEndDate), date)
        );
    }

    public static Specification<Potential> createdByEqual(final String createdBy) {
    	return (root, criteriaQuery, cb) -> cb.equal(root.get(Potential_.createdBy), createdBy);
    }

    public static Specification<Potential> createdAtGreaterThanOrEqual(final LocalDateTime dateTime) {
    	return (root, criteriaQuery, cb) -> cb.greaterThanOrEqualTo(root.get(Potential_.createdAt).get(UtcDateTime_.dateTime), dateTime);
    }

    public static Specification<Potential> createdAtLessThanOrEqual(final LocalDateTime dateTime) {
    	return (root, criteriaQuery, cb) -> cb.lessThanOrEqualTo(root.get(Potential_.createdAt).get(UtcDateTime_.dateTime), dateTime);
    }
}
