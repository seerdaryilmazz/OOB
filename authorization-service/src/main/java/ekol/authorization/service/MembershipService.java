package ekol.authorization.service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.authorization.domain.auth.AuthUser;
import ekol.authorization.domain.dto.UserMemberships;
import ekol.authorization.domain.dto.UserMemberships.Membership;
import ekol.authorization.dto.*;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.auth.AuthUserRepository;
import ekol.authorization.service.auth.AuthOperationService;
import ekol.authorization.service.userdetails.UserDetailsService;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MembershipService {

	private TeamService teamService;
	private AuthUserRepository authUserRepository;
	private UserService userService;
	private UserDetailsService userDetailsService;
	private AuthOperationService authOperationService;

	public Map<String, Object> listSublevelUsers(String username, boolean getSubhierarchy) {
		Map<String,Object> result = new LinkedHashMap<>();
		UserDto user = userService.getUserByAccountName(username, UserDto.class);
		user.setTeams(listTeamMemberships(user.getUsername()));
		
		Supplier<Iterable<Node>> teamSupplier = null;
		Function<Node, TeamUser> iterateFunc = null;
		if(getSubhierarchy && authOperationService.hasOpeartion("authorization.get-subhierarchy-of-user")) {
			UserMemberships ms =UserMemberships.createWith(userDetailsService.findUserWithActiveMemberships(user.getUsername()));
			Set<Long> departments = ms.getDepartments().stream().map(Membership::getMemberOf).map(IdNamePair::getId).collect(Collectors.toSet());
			Set<Long> subsidiaries = ms.getSubsidiaries().stream().map(Membership::getMemberOf).map(IdNamePair::getId).collect(Collectors.toSet());
			teamSupplier = ()->teamService.findByMemberOfLevelAndUserWithHierarchy(departments, subsidiaries);
			iterateFunc = node->this.iterateNodes(node, null);
		} else {
			teamSupplier = ()->teamService.findByMemberOfLevelAndUserWithHierarchy(username);
			iterateFunc = node->this.iterateNodes(node, user);
		}
		
		List<TeamUser> teams= StreamSupport.stream(teamSupplier.get().spliterator(), false)
				.map(iterateFunc::apply)
				.collect(Collectors.toList());
		result.put("user", user);
		result.put("teams", teams);
		return result;
	}

	private TeamUser iterateNodes(Node node, UserDto user) {
		TeamUser teamUser = new TeamUser();
		teamUser.setTeam(node);
		List<AuthUser> authUsers = null;
		if(Objects.nonNull(user)) {
			authUsers = authUserRepository.findActiveSubLevelAllMemberOfNodeAndUsernameByUsernameAndNode(user.getUsername(), node.getId(), LocalDate.now().toEpochDay());
		} else {
			authUsers = authUserRepository.findActiveSubLevelAllMemberOfNodeAndUsernameByNode(node.getId(), LocalDate.now().toEpochDay());
		}
		Set<String> usernames = authUsers.parallelStream().map(AuthUser::getName).collect(Collectors.toSet());
		List<UserDto> users =  userService.getUsersByUsername(usernames,"ACTIVE", "ACTIVE_DIRECTORY", UserDto[].class);
		users.forEach(c->c.setTeams(listTeamMemberships(c.getUsername())));
		teamUser.setUsers(users);
		return teamUser;
	}

	public List<AuthUser> findDirectMembersOfANodeByNodeIdAndLevel(Long nodeId, Long level) {
		return authUserRepository.findDirectMembersOfANodeByNodeIdAndLevel(nodeId, level);
	}

	public List<MemberOfRelation> findDirectMembersOfANodeByNodeId(Long nodeId) {
		return mapMembershipRelation(()->authUserRepository.findDirectMembersOfANodeByNodeId(nodeId), nodeId);
	}

	public List<MemberOfRelation> findActiveMembersOfANodeByNodeId(Long nodeId) {
		return mapMembershipRelation(()->authUserRepository.findActiveMembershipsByNodeId(nodeId, LocalDate.now().toEpochDay()), nodeId);
	}

	public List<MemberOfRelation> findSubLevelMembersOfANodeByNodeId(Long nodeId, String username) {
		List<MemberOfRelation> relations = mapMembershipRelation(()->authUserRepository.findActiveSubLevelMemberOfNodeAndUsername(username, nodeId, LocalDate.now().toEpochDay()), nodeId);
		if(CollectionUtils.isEmpty(relations)) {
			relations = findActiveMembersOfANodeByNodeId(nodeId).stream().filter(t->!Objects.equals(t.getFrom().getName(), username)).collect(Collectors.toList());
		}
		return relations;
	}

	public List<AuthUser> findByAuthorizedOperation(String name){
		return authUserRepository.findByAuthorizedOperation(name, LocalDate.now().toEpochDay());
	}
	
	public List<UserDto> listManagerOfUser(String username) {
		List<AuthUser> authUsers =  authUserRepository.findUpLevelUsersOfUser(username, LocalDate.now().toEpochDay());
		Set<String> usernames = authUsers.parallelStream().map(AuthUser::getName).collect(Collectors.toSet());
		return userService.getUsersByUsername(usernames,"ACTIVE", "ACTIVE_DIRECTORY", UserDto[].class);
	}

	private List<MemberOfRelation> mapMembershipRelation(Supplier<Collection<AuthUser>> supplier, Long nodeId){
		return supplier.get().stream()
				.flatMap(t -> t.getMemberships().stream())
				.filter(t -> Objects.equals(t.getEntity().getId(), nodeId))
				.map(MemberOfRelation::fromEntity).collect(Collectors.toList());
	}
	
	private List<NodeMembership> listTeamMemberships(String username){
		return authUserRepository
				.findActiveMembershipsByUsername(username, LocalDate.now().toEpochDay())
				.getMemberships()
				.stream()
				.filter(relation ->"Team".equalsIgnoreCase(relation.getEntity().getType()))
				.map(NodeMembership::with)
				.collect(Collectors.toList());
	}
}
