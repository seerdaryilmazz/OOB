package ekol.kartoteks.repository;

import ekol.hibernate5.domain.repository.ApplicationCustomRepository;
import ekol.kartoteks.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 11/12/2017.
 */
@Repository
@Transactional
public class CompanyExportQueueCustomRepository extends ApplicationCustomRepository<CompanyExportQueue> {

    public CompanyExportQueueCustomRepository() {
        super(CompanyExportQueue.class);
    }

    public List<CompanyExportQueue> searchWithFilter(CompanyExportQueueFilter queueFilter){
        CriteriaQuery<CompanyExportQueue> criteria = createEmptyCriteria();
        Root<CompanyExportQueue> root = criteria.from(CompanyExportQueue.class);
        root.fetch(CompanyExportQueue_.company, JoinType.INNER);
        criteria.select(root);

        Join<CompanyExportQueue, Company> companyJoin = root.join(CompanyExportQueue_.company);

        CriteriaBuilder builder = getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        if(queueFilter.getStatus() != null){
            predicates.add(builder.equal(root.get(CompanyExportQueue_.status), queueFilter.getStatus()));
        }
        if(queueFilter.getCompanyId() != null){
            predicates.add(builder.equal(companyJoin.get(Company_.id),queueFilter.getCompanyId()));
        }
        if(StringUtils.isNotBlank(queueFilter.getApplicationCompanyId())){
            predicates.add(builder.equal(root.get(CompanyExportQueue_.applicationCompanyId),queueFilter.getApplicationCompanyId()));
        }
        Predicate[] predicateArray = predicates.toArray(new Predicate[]{});
        criteria.where(builder.and(predicateArray));
        criteria.orderBy(builder.desc(root.get(CompanyExportQueue_.latestExecuteDate)));
        TypedQuery<CompanyExportQueue> query = getEntityManager().createQuery(criteria);


        query.setFirstResult((queueFilter.getPage() - 1) * queueFilter.getSize());
        query.setMaxResults(queueFilter.getSize());
        return query.getResultList();

    }

}
