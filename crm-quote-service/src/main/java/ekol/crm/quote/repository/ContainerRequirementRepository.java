package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.ContainerRequirement;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface ContainerRequirementRepository extends ApplicationRepository<ContainerRequirement> {

    List<ContainerRequirement> findByQuote(Quote quote);
}
