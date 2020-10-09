package ekol.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.entity.BaseEntity;

@NoRepositoryBean
public interface ApplicationRepository <T extends BaseEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
	
	default void delete(T t) {
		t.setDeleted(true);
		save(t);
	}
	
	default void delete(Long id) {
		T t = Optional.of(findOne(id)).orElseThrow(() -> new ResourceNotFoundException("Entity id with {0} can not found", id));
		delete(t);
	}
}
