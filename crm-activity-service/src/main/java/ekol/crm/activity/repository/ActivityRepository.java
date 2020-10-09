package ekol.crm.activity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ekol.crm.activity.domain.*;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;


public interface ActivityRepository extends ApplicationMongoRepository<Activity> {
    Page<Activity> findAllByDeletedFalse(Pageable pageable);
    List<Activity> findByAccountIdAndDeletedFalseOrderByCreatedAtDesc(Long accountId);
    
    Page<Activity> findAllByDeletedFalseAndCalendarNotNull(Pageable pageable);
    
    List<Activity> findByDeletedFalseAndStatus(ActivityStatus status);
}
