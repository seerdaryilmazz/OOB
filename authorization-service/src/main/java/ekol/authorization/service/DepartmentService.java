package ekol.authorization.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.Department;
import ekol.authorization.domain.auth.AuthDepartment;
import ekol.authorization.repository.DepartmentRepository;
import ekol.authorization.repository.auth.AuthDepartmentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class DepartmentService {

	private DepartmentRepository departmentRepository;
	private AuthDepartmentRepository authDepartmentRepository;
	
	public Iterable<Department> findByInheritTeamId(Long teamId) {
		Set<Long> externalIds = authDepartmentRepository.findByInheritTeamId(teamId).parallelStream().map(AuthDepartment::getExternalId).collect(Collectors.toSet());
		return departmentRepository.findAll(externalIds);
	}
	
	public Iterable<Department> findByInheritTeamExternalId(Long teamExternalId) {
		Set<Long> externalIds = authDepartmentRepository.findByInheritTeamExternalId(teamExternalId).parallelStream().map(AuthDepartment::getExternalId).collect(Collectors.toSet());
		return departmentRepository.findAll(externalIds);
	}
}
