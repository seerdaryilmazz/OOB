package ekol.authorization.repository;

import ekol.authorization.domain.Department;
import ekol.hibernate5.domain.repository.LookupRepository;

import java.util.Optional;

public interface DepartmentRepository extends LookupRepository<Department> {

    Optional<Department> findById(Long id);
}
