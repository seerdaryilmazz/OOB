package ekol.agreement.repository;

import ekol.agreement.domain.model.agreement.Agreement;

/**
 * Created by Dogukan Sahinturk on 10.10.2019
 */
public interface CustomAgreementRepository {
    void detach(Agreement agreement);
}
