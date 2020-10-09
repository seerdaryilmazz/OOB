package ekol.authorization.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.Department;
import ekol.authorization.domain.EntityStatus;
import ekol.authorization.domain.Team;
import ekol.authorization.domain.auth.AuthDepartment;
import ekol.authorization.domain.auth.AuthTeam;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.DepartmentRepository;
import ekol.authorization.repository.TeamRepository;
import ekol.authorization.repository.auth.AuthTeamRepository;
import ekol.authorization.service.auth.NodeService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class DepartmentManagementService {
	
	private TeamRepository teamRepository;
	private AuthTeamRepository authTeamRepository;
	private DepartmentRepository departmentRepository;
	private NodeService nodeService;

	public List<Node> listTeamsByDepartmentCode(String code, EntityStatus status) {
		Supplier<Stream<AuthTeam>> authTeamSupplier = 
				()->Optional.ofNullable(departmentRepository.findByCode(code))
							.map(Department::getId)
							.map(this::findNodeByExternalId)
							.map(AuthDepartment::getId)
							.map(authTeamRepository::findByInheritedTeams)
							.map(List::stream)
							.orElseGet(Stream::empty);
				
		if(Objects.isNull(status)){
			return authTeamSupplier.get().map(Node::with).collect(Collectors.toList());
		} else {
			Iterable<Team> teams = teamRepository.findAll(authTeamSupplier.get().map(AuthTeam::getExternalId).collect(Collectors.toSet()));
			return StreamSupport.stream(teams.spliterator(), false)
								.filter(t->Objects.equals(t.getStatus(), status)).flatMap(t->authTeamSupplier.get().filter(a->Objects.equals(a.getExternalId(), t.getId())))
								.map(Node::with)
								.collect(Collectors.toList());
		}
	}
	
	public AuthDepartment findNodeByExternalId(Long externalId) {
		return nodeService.loadFromExternalId(externalId, AuthDepartment.class);
	}
}
