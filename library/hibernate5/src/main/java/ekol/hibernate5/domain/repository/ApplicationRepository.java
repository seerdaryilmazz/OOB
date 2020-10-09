package ekol.hibernate5.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Created by kilimci on 16/03/16.
 */
@NoRepositoryBean
public interface ApplicationRepository<T> extends Repository<T, Long> {

    Iterable<T> findAll(Sort var1);
    Page<T> findAll(Pageable var1);
    <S extends T> S save(S var1);
    <S extends T> Iterable<S> save(Iterable<S> var1);
    T findOne(Long var1);
    boolean exists(Long var1);

    Iterable<T> findAll();
    Iterable<T> findAll(Iterable<Long> var1);

    long count();
}