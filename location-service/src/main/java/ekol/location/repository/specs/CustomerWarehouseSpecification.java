package ekol.location.repository.specs;

import ekol.location.domain.Country_;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.location.comnon.Address_;
import ekol.location.domain.location.comnon.CustomsDetails_;
import ekol.location.domain.location.comnon.Establishment_;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse_;
import ekol.location.domain.location.customs.CustomsOffice_;
import ekol.location.domain.location.warehouse.Warehouse;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomerWarehouseSpecification {

    public static Specifications<CustomerWarehouse> append(Specifications<CustomerWarehouse> appendTo, Specification<CustomerWarehouse> spec){
        return appendTo == null ? Specifications.where(spec) : appendTo.and(spec);
    }

    public static Specification<CustomerWarehouse> havingCustomsDetailsCustomsType(final WarehouseCustomsType customsType) {
        return new Specification<CustomerWarehouse>() {
            @Override
            public Predicate toPredicate(Root<CustomerWarehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(CustomerWarehouse_.customsDetails).get(CustomsDetails_.customsType), customsType);
            }
        };
    }

    public static Specification<CustomerWarehouse> havingCustomsDetailsCustomsOffice(final Long customsOfficeId) {
        return new Specification<CustomerWarehouse>() {
            @Override
            public Predicate toPredicate(Root<CustomerWarehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(CustomerWarehouse_.customsDetails).get(CustomsDetails_.customsOffice).get(CustomsOffice_.id), customsOfficeId);
            }
        };
    }

    public static Specification<CustomerWarehouse> havingCompanyType(final WarehouseCompanyType type) {
        return new Specification<CustomerWarehouse>() {
            @Override
            public Predicate toPredicate(Root<CustomerWarehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(CustomerWarehouse_.companyType), type);
            }
        };
    }

    public static Specification<CustomerWarehouse> havingAdressCountryCode(final String countryCode) {
        return new Specification<CustomerWarehouse>() {
            @Override
            public Predicate toPredicate(Root<CustomerWarehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get(CustomerWarehouse_.establishment).get(Establishment_.address).get(Address_.country).get(Country_.iso), countryCode);
            }
        };
    }

    public static Specification<CustomerWarehouse> havingAdressPostalCode(final String postalCode) {
        return new Specification<CustomerWarehouse>() {
            @Override
            public Predicate toPredicate(Root<CustomerWarehouse> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.like(root.get(CustomerWarehouse_.establishment).get(Establishment_.address).get(Address_.postalCode), postalCode + "%");
            }
        };
    }

}
