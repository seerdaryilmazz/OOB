package ekol.agreement.repository;

import ekol.agreement.domain.model.Document;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface DocumentRepository extends ApplicationRepository<Document> {

    List<Document> findByAgreement(Agreement agreement);
}
