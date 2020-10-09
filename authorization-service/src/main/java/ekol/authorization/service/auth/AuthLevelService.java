package ekol.authorization.service.auth;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.Department;
import ekol.authorization.domain.auth.AuthLevel;
import ekol.authorization.domain.auth.BaseEntity;
import ekol.authorization.dto.User;
import ekol.authorization.repository.DepartmentRepository;
import ekol.authorization.repository.auth.AuthTeamRepository;
import ekol.authorization.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class AuthLevelService {

	private static final int MAX_DEPTH = 3;

	private DepartmentRepository departmentRepository;
	private AuthTeamRepository authTeamRepository;
	private UserService userService;

	public List<AuthLevel> listTeamInheritenceByUser(String username) {
		return authTeamRepository.getAuthLevelOfUser(username, LocalDate.now().toEpochDay());
	}

	public List<User> prepareUserList(String departmentCode) {
		Department department = departmentRepository.findByCode(departmentCode);
		List<AuthLevel> authLevels = authTeamRepository.getAuthLevelOfDepartment(department.getId());

		return authLevels.stream()
				.map(this::resolveRelationLevelBetweenUserAndDepartment)
				.collect(Collectors.toList());
	}

	private User resolveRelationLevelBetweenUserAndDepartment(AuthLevel authLevel) {
		User user = new User();
		user.setDeleted(false);
		user.setId(authLevel.getUser().getExternalId());
		user.setUsername(authLevel.getUser().getName());

		List<BaseEntity> nodes = authLevel.getNodes().stream().filter(node -> !node.getType().equalsIgnoreCase("Department")).collect(Collectors.toList());

		if (authLevel.getRelationshipLength() > 1 && authLevel.getRelationshipLength() < MAX_DEPTH) {
			user.setFirstLevel(nodes.get(0).getName());
			user.setSecondLevel(nodes.get(1).getName());
			user.setThirdLevel(nodes.get(1).getName());
		}
		else if (authLevel.getRelationshipLength() >= MAX_DEPTH) {
			user.setFirstLevel(nodes.get(0).getName());
			user.setSecondLevel(nodes.get(1).getName());
			user.setThirdLevel(nodes.get(2).getName());
		}
		else {
			String level = nodes.get(0).getName();
			user.setFirstLevel(level);
			user.setSecondLevel(level);
			user.setThirdLevel(level);
		}

		try {
			String displayName = userService.getUserByAccountName(authLevel.getUser().getName(), User.class).getDisplayName();
			user.setDisplayName(displayName);
		} catch (Exception e) {

		}

		return user;
	}
}
