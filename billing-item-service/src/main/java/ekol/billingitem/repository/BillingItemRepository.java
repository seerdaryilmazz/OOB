package ekol.billingitem.repository;

import ekol.billingitem.domain.BillingItem;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface BillingItemRepository extends ApplicationRepository<BillingItem> {

    BillingItem findByCodeAndActiveTrue(String code);

    long countByCodeAndActiveTrue(String code);

    long countByIdNotAndCodeAndActiveTrue(Long id, String code);

    long countByParentIdIsNullAndNameAndActiveTrue(String name);

    long countByParentIdAndNameAndActiveTrue(Long parentId, String name);

    long countByIdNotAndParentIdIsNullAndNameAndActiveTrue(Long id, String name);

    long countByIdNotAndParentIdAndNameAndActiveTrue(Long id, Long parentId, String name);

    long countByParentIdAndActiveTrue(Long parentId);

    @EntityGraph(value = "BillingItem.withParent", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<BillingItem> findAllByParentIdNullAndActiveTrue();

    @EntityGraph(value = "BillingItem.withParent", type = EntityGraph.EntityGraphType.LOAD)
    Iterable<BillingItem> findAllByParentIdAndActiveTrue(Long parentId);

}
