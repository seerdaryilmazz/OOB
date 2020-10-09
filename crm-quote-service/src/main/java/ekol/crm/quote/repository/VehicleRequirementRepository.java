package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.VehicleRequirement;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface VehicleRequirementRepository extends ApplicationRepository<VehicleRequirement> {

    List<VehicleRequirement> findByQuote(Quote quote);
}
