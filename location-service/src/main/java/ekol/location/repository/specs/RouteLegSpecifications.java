package ekol.location.repository.specs;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime_;
import ekol.location.domain.LinehaulRouteLeg_;
import ekol.location.domain.RouteLegExpedition;
import ekol.location.domain.RouteLegExpeditionStatus;
import ekol.location.domain.RouteLegExpedition_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;


/**
 * Created by burak on 14/12/17.
 */
public class RouteLegSpecifications {

    public static Specification<RouteLegExpedition> havingRouteLeg(final Long routeLegId) {
        return new Specification<RouteLegExpedition>() {
            @Override
            public Predicate toPredicate(Root<RouteLegExpedition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(RouteLegExpedition_.linehaulRouteLeg).get(LinehaulRouteLeg_.id), routeLegId);
            }
        };
    }

    public static Specification<RouteLegExpedition> notHavingStatus(final RouteLegExpeditionStatus status) {
        return new Specification<RouteLegExpedition>() {
            @Override
            public Predicate toPredicate(Root<RouteLegExpedition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.notEqual(root.get(RouteLegExpedition_.status), status);
            }
        };
    }

    public static Specification<RouteLegExpedition> departureGreaterThanOrEqual(final LocalDateTime dateTime) {
        return new Specification<RouteLegExpedition>() {
            @Override
            public Predicate toPredicate(Root<RouteLegExpedition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.greaterThanOrEqualTo(root.get(RouteLegExpedition_.departure).get(FixedZoneDateTime_.dateTime), dateTime);
            }
        };
    }

    public static Specification<RouteLegExpedition> departureLessThanOrEqual(final LocalDateTime dateTime) {
        return new Specification<RouteLegExpedition>() {
            @Override
            public Predicate toPredicate(Root<RouteLegExpedition> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.lessThanOrEqualTo(root.get(RouteLegExpedition_.departure).get(FixedZoneDateTime_.dateTime), dateTime);
            }
        };
    }

}

