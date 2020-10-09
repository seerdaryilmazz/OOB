package ekol.agreement.repository;

import ekol.agreement.domain.model.agreement.Agreement;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Dogukan Sahinturk on 10.10.2019
 */
@Repository
public class CustomAgreementRepositoryImpl implements CustomAgreementRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void detach(Agreement agreement) {
        entityManager.detach(agreement);
    }
}
