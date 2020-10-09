package ekol.billingitem.repository;

import ekol.billingitem.domain.BillingItemGroup;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface BillingItemGroupRepository extends ApplicationRepository<BillingItemGroup> {

    long countByParentIdIsNullAndNameAndActiveTrue(String name);

    long countByParentIdAndNameAndActiveTrue(Long parentId, String name);

    long countByIdNotAndParentIdIsNullAndNameAndActiveTrue(Long id, String name);

    long countByIdNotAndParentIdAndNameAndActiveTrue(Long id, Long parentId, String name);

    long countByParentIdAndActiveTrue(Long parentId);

    @EntityGraph(value = "BillingItemGroup.withParent", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<BillingItemGroup> findAllByParentIdNullAndActiveTrue();

    @EntityGraph(value = "BillingItemGroup.withParent", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<BillingItemGroup> findAllByParentIdAndActiveTrue(Long parentId);

}
