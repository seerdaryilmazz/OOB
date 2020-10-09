package ekol.location.repository.specs;

import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.enumeration.WarehouseOwnerType;
import ekol.location.domain.location.warehouse.Warehouse;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ekol.location.domain.location.warehouse.Warehouse_;
import ekol.location.domain.location.comnon.CustomsDetails_;
import ekol.location.domain.location.customs.CustomsOffice_;
import ekol.location.domain.location.comnon.Establishment_;
import ekol.location.domain.location.comnon.Address_;
import ekol.location.domain.Country_;


public class WarehouseSpecification {

    public static Specifications<Warehouse> append(Specifications<Warehouse> appendTo, Specification<Warehouse> spec){
        return appendTo == null ? Specifications.where(spec) : appendTo.and(spec);
    }

    public static Specification<Warehouse> havingOwnerType(final WarehouseOwnerType ownerType) {
        return new Specification<Warehouse>() {
            @Override
            public Predicate toPredicate(Root<Warehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Warehouse_.warehouseOwnerType), ownerType);
            }
        };
    }

    public static Specification<Warehouse> havingCustomsDetailsCustomsType(final WarehouseCustomsType customsType) {
        return new Specification<Warehouse>() {
            @Override
            public Predicate toPredicate(Root<Warehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Warehouse_.customsDetails).get(CustomsDetails_.customsType), customsType);
            }
        };
    }

    public static Specification<Warehouse> havingCustomsDetailsCustomsOffice(final Long customsOfficeId) {
        return new Specification<Warehouse>() {
            @Override
            public Predicate toPredicate(Root<Warehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Warehouse_.customsDetails).get(CustomsDetails_.customsOffice).get(CustomsOffice_.id), customsOfficeId);
            }
        };
    }

    public static Specification<Warehouse> havingAdressCountryCode(final String countryCode) {
        return new Specification<Warehouse>() {
            @Override
            public Predicate toPredicate(Root<Warehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(Warehouse_.establishment).get(Establishment_.address).get(Address_.country).get(Country_.iso), countryCode);
            }
        };
    }

    public static Specification<Warehouse> havingAdressPostalCode(final String postalCode) {
        return new Specification<Warehouse>() {
            @Override
            public Predicate toPredicate(Root<Warehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.like(root.get(Warehouse_.establishment).get(Establishment_.address).get(Address_.postalCode), postalCode + "%");
            }
        };
    }
}
