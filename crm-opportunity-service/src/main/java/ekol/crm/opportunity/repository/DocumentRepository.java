package ekol.crm.opportunity.repository;

import ekol.crm.opportunity.domain.model.Document;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

/**
 * Created by Dogukan Sahinturk on 21.11.2019
 */
public interface DocumentRepository extends ApplicationRepository<Document> {
    List<Document> findByOpportunity(Opportunity opportunity);
}
