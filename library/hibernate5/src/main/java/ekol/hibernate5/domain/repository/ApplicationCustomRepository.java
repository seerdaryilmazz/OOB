package ekol.hibernate5.domain.repository;


import ekol.hibernate5.domain.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by kilimci on 12/04/16.
 */
public abstract class ApplicationCustomRepository<T extends BaseEntity> {

    @PersistenceContext
    private EntityManager em;

    private final Class<T> clazz;

    public ApplicationCustomRepository(final Class<T> clazz) {
        this.clazz = clazz;
    }

    protected Class getClazz() {
        return clazz;
    }

    protected EntityManager getEntityManager() {
        return em;
    }
    protected CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }
    protected CriteriaQuery<T> createEmptyCriteria() {
        return getEntityManager().getCriteriaBuilder().createQuery(getClazz());
    }

    protected CriteriaQuery<T> getCriteria() {
        CriteriaQuery<T> criteria = createEmptyCriteria();
        appendNotDeletedRestriction(criteria);
        return criteria;
    }
    protected Root<T> getRoot(CriteriaQuery<T> criteria){
        return criteria.from(getClazz());
    }
    protected void appendNotDeletedRestriction(CriteriaQuery<T> criteria) {
        criteria.where(getCriteriaBuilder().equal( getRoot(criteria).get("deleted"), false));
    }



}
