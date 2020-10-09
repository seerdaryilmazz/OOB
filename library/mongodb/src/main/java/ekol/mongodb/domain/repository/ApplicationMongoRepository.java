package ekol.mongodb.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by burak on 06/06/16.
 */

@NoRepositoryBean
public interface ApplicationMongoRepository<T> extends MongoRepository<T, String> {

    @Override
    List<T> findAll(Sort var1);

    @Override
    Page<T> findAll(Pageable var1);

    <S extends T> S save(S var1);

    @Override
    <S extends T> List<S> save(Iterable<S> var1);

    T findOne(String var1);
    boolean exists(String var1);

    @Override
    List<T> findAll();

    @Override
    Iterable<T> findAll(Iterable<String> var1);

    T findById(String id);

    @Override
    long count();
}
