package ekol.hibernate5.domain.repository;

import ekol.hibernate5.domain.entity.LookupEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface LookupRepository<T extends LookupEntity> extends ApplicationRepository<T> {

    /**
     * Bu metodun ismini değiştirirken dikkat etmek gerekli. Daha detaylı bilgi için
     * aşağıdaki sayfaya bakınız.
     *
     * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
     */

    List<T> findByOrderByCode();

    /**
     * Bu metodun ismini değiştirirken dikkat etmek gerekli. Daha detaylı bilgi için
     * aşağıdaki sayfaya bakınız.
     *
     * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
     */

    List<T> findByOrderByName();

    T findByCode(String code);
}
