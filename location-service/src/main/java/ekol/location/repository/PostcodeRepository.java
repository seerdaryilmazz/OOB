package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.Postcode;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface PostcodeRepository extends ApplicationRepository<Postcode> {

    @EntityGraph(value = "Postcode.allDetails", type = EntityGraph.EntityGraphType.LOAD)
    List<Postcode> findAllWithDetailsDistinctByCountryIsoAlpha3CodeAndValueLike(String countryIsoAlpha3Code, String value);

}
