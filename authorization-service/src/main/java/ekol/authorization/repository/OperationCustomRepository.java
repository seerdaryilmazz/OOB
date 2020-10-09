package ekol.authorization.repository;

import ekol.authorization.domain.Operation;
import ekol.authorization.domain.OperationUrl;
import ekol.authorization.domain.OperationUrl_;
import ekol.authorization.domain.Operation_;
import ekol.authorization.dto.OperationFilterRequest;
import ekol.hibernate5.domain.repository.ApplicationCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozer on 03/03/2017.
 */
@Repository
public class OperationCustomRepository extends ApplicationCustomRepository<Operation> {

    public OperationCustomRepository() {
        super(Operation.class);
    }

    public List<Operation> findUnusedOperations() {
        CriteriaBuilder builder = getCriteriaBuilder();

        CriteriaQuery<Operation> operationCriteriaQuery = builder.createQuery(Operation.class);

        Root<Operation> operation = operationCriteriaQuery.from(Operation.class);
        operationCriteriaQuery.select(operation);

        Subquery<OperationUrl> operationUrlSubquery = operationCriteriaQuery.subquery(OperationUrl.class);

        Root<OperationUrl> operationUrl = operationUrlSubquery.from(OperationUrl.class);
        operationUrlSubquery.select(operationUrl);

        List<Predicate> operationUrlSubqueryPredicates = new ArrayList<>();
        operationUrlSubqueryPredicates.add(builder.equal(operation.get(Operation_.id), operationUrl.get(OperationUrl_.operation)));

        operationUrlSubquery.where(operationUrlSubqueryPredicates.toArray(new Predicate[]{}));

        List<Predicate> operationCriteriaQueryPredicates = new ArrayList<Predicate>();
        operationCriteriaQueryPredicates.add(builder.not(builder.exists(operationUrlSubquery)));

        operationCriteriaQuery.where(operationCriteriaQueryPredicates.toArray(new Predicate[]{}));
        return getEntityManager().createQuery(operationCriteriaQuery).getResultList();
    }

    public List<Operation> search(OperationFilterRequest filter) {
        CriteriaQuery<Operation> criteria = createEmptyCriteria();
        Root<Operation> root = criteria.from(Operation.class);
        criteria.select(root);

        CriteriaBuilder builder = getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.isNotBlank(filter.getName())){
            predicates.add(builder.like(root.get(Operation_.name), filter.getNameStartsWith()));
        }

        if(StringUtils.isNotBlank(filter.getDescription())){
            predicates.add(builder.like(root.get(Operation_.description), filter.getDescriptionLike()));
        }

        if(filter.getStartDate() != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get(Operation_.createdAt), filter.getStartDateAsLocalDate()));
        }
        if(filter.getEndDate() != null){
            predicates.add(builder.lessThanOrEqualTo(root.get(Operation_.createdAt), filter.getEndDateAsLocalDate()));
        }

        Predicate[] predicateArray = predicates.toArray(new Predicate[]{});
        criteria.where(builder.and(predicateArray));
        TypedQuery<Operation> query = getEntityManager().createQuery(criteria);

        return query.getResultList();
    }
}
