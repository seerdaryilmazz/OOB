package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.product.Product;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends ApplicationRepository<Product> {

    Optional<Product> findById(Long id);
    List<Product> findByQuote(Quote quote);
}
