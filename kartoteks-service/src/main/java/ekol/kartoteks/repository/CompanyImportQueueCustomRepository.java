package ekol.kartoteks.repository;


import ekol.hibernate5.domain.embeddable.UtcDateTime_;
import ekol.hibernate5.domain.repository.ApplicationCustomRepository;
import ekol.kartoteks.domain.CompanyImportQueue;
import ekol.kartoteks.domain.CompanyImportQueueFilter;
import ekol.kartoteks.domain.CompanyImportQueue_;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fatmaozyildirim on 4/13/16.
 */

@Repository
@Transactional
public class CompanyImportQueueCustomRepository extends ApplicationCustomRepository<CompanyImportQueue> {

    public CompanyImportQueueCustomRepository() {
        super(CompanyImportQueue.class);
    }


    public List<CompanyImportQueue> searchWithFilter(CompanyImportQueueFilter queueFilter){
        CriteriaQuery<CompanyImportQueue> criteria = createEmptyCriteria();
        Root<CompanyImportQueue> root = criteria.from(CompanyImportQueue.class);
        criteria.select(root);

        CriteriaBuilder builder = getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        if(queueFilter.getStatus() != null){
            predicates.add(builder.equal(root.get(CompanyImportQueue_.status), queueFilter.getStatus()));
        }
        if(queueFilter.getStartDate() != null){
            LocalDateTime startOfDay = LocalDateTime.of(queueFilter.getStartDate(), LocalTime.MIDNIGHT);
            predicates.add(builder.greaterThanOrEqualTo(root.get(CompanyImportQueue_.createDate).get(UtcDateTime_.dateTime), startOfDay));
        }
        if(queueFilter.getEndDate() != null){
            LocalDateTime startOfDay =  LocalDateTime.of(queueFilter.getEndDate().plusDays(1), LocalTime.MIDNIGHT);
            predicates.add(builder.lessThan(root.get(CompanyImportQueue_.createDate).get(UtcDateTime_.dateTime), startOfDay));
        }
        if(StringUtils.isNotBlank(queueFilter.getCompanyName())){
            predicates.add(builder.like(root.get(CompanyImportQueue_.companyName),queueFilter.getCompanyName().toUpperCase() + "%"));
        }
        if(StringUtils.isNotBlank(queueFilter.getCustomerCompanyCode())){
            predicates.add(builder.equal(root.get(CompanyImportQueue_.customerCompanyCode),queueFilter.getCustomerCompanyCode()));
        }
        if(StringUtils.isNotBlank(queueFilter.getOrderCode())){
            predicates.add(builder.equal(root.get(CompanyImportQueue_.orderCode),queueFilter.getOrderCode()));
        }
        if(StringUtils.isNotBlank(queueFilter.getType())){
            SetJoin<CompanyImportQueue, String> typesJoin = root.join(CompanyImportQueue_.companyRoleTypes);
            predicates.add(typesJoin.in(queueFilter.getType()));
        }
        Predicate[] predicateArray = predicates.toArray(new Predicate[]{});
        criteria.where(builder.and(predicateArray));
        criteria.orderBy(builder.desc(root.get(CompanyImportQueue_.createDate)));
        TypedQuery<CompanyImportQueue> query = getEntityManager().createQuery(criteria);

        query.setFirstResult((queueFilter.getPage() - 1) * queueFilter.getSize());
        query.setMaxResults(queueFilter.getSize());


        return query.getResultList();

    }
}
