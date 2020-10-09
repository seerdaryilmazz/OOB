package ekol.authorization.service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.authorization.domain.*;
import ekol.authorization.domain.auth.*;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.TeamRepository;
import ekol.authorization.repository.auth.*;
import ekol.authorization.service.auth.NodeService;
import ekol.exceptions.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class TeamService {

	private NodeService nodeService;
	private BaseRepository baseRepository;
	private AuthTeamRepository authTeamRepository;
	private TeamRepository teamRepository;
	
	@PostConstruct
	private void init() {
		List<AuthTeam> onlyGraph = authTeamRepository.findByExternalIdIsNull();
		if(!CollectionUtils.isEmpty(onlyGraph)) {
			for (AuthTeam authTeam : onlyGraph) {
				Team team = Team.with(authTeam);
				Team saved = teamRepository.save(team);
				authTeam.setExternalId(saved.getId());
				authTeamRepository.save(authTeam);
			}
		}
	}

	protected void assertIdExists(Long id) {
		if (!baseRepository.exists(id)) {
			throw new ResourceNotFoundException("Can not find entity", id);
		}
	}

	protected void assertExternalIdNotAlreadyIn(Long id, Long externalId) {
		if (externalId == null) {
			return;
		}
		BaseEntity t = nodeService.loadFromExternalId(externalId, AuthTeam.class);

		if (t == null) {
			return;
		}

		if (t.getId().equals(id)) {
			return;
		}

		throw new BadRequestException("Entity already exists");
	}

	public AuthTeam findNodeByExternalIdOrThrowException(Long externalId, int depth) {
		return Optional.ofNullable(authTeamRepository.findByExternalId(externalId, depth)).orElseThrow(() -> new ResourceNotFoundException("Node with externalId {0} not found", String.valueOf(externalId)));
	}
	
	public AuthTeam findNodeOrThrowException(Long id, int depth) {
		return Optional.ofNullable(authTeamRepository.findOne(id, depth)).orElseThrow(() -> new ResourceNotFoundException("Node with id {0} not found", String.valueOf(id)));
	}

	public Team findTeamOrThrowException(Long id) {
		return Optional.ofNullable(teamRepository.findOne(id)).orElseThrow(() -> new ResourceNotFoundException("Team with id {0} not found", String.valueOf(id)));
	}

	public Team findTeamByName(String name) {
		return findTeamByName(name, false);
	}
	
	public Team findTeamByName(String name, boolean ignoreCase) {
		if(ignoreCase) {
			return teamRepository.findByNameIgnoreCase(name);
		}
		return teamRepository.findByName(name);
	}
	
	private void validateTeam(Team team) {
		Team teamEntity = teamRepository.findByName(team.getName());
		if(Objects.nonNull(teamEntity) && !Objects.equals(teamEntity.getId(), team.getId())) {
			throw new BadRequestException("{0} named another {1} is exist.", team.getName(), "Team");
		}
	}
	
	@Transactional
	public Team save(Team team) {
		if (team.getId() != null) {
            throw new BadRequestException("Can not update with this method");
        }

        Team teamSaved = teamRepository.save(team);
        assertExternalIdNotAlreadyIn(null, teamSaved.getId());
        
        AuthTeam authTeam = new AuthTeam();
        authTeam.setExternalId(teamSaved.getId());
        authTeam.setName(teamSaved.getName());
        baseRepository.save(authTeam);
        return teamSaved;
	}

	@Transactional
	public Team update(Team team, Long id) {
		validateTeam(team);
		Team entity = findTeamOrThrowException(id);
		AuthTeam authUpdated = findNodeByExternalIdOrThrowException(entity.getId(), 0);
		
		entity = teamRepository.save(team);
		
		authUpdated.setName(entity.getName());
		authTeamRepository.save(authUpdated);
		return entity;
	}
	
	@Transactional
	public Team updateStatus(Team team, Long id) {
		BaseEntity node = authTeamRepository.findByExternalId(team.getId(), 0);
		if(0 < baseRepository.countInheritAndMemberOfRelations(node.getId())) {
			throw new BadRequestException("Teams which are associated with other nodes or which have active members, cannot be deactivated.");
		}
		Team entity = findTeamOrThrowException(id);
		entity.setStatus(team.getStatus());
		return teamRepository.save(entity);
	}

	@Transactional
	public Team delete(Long id) {
		Team entity = findTeamOrThrowException(id);
		AuthTeam authDeleted = findNodeByExternalIdOrThrowException(entity.getId(), 0);
		if(!CollectionUtils.isEmpty(baseRepository.findRelations(authDeleted.getId()))) {
			throw new BadRequestException("can not delete, team has relation");
		}
		
		entity.setDeleted(true);
		entity = teamRepository.save(entity);
		
		authTeamRepository.delete(authDeleted);
		return entity;
	}

	public Iterable<Team> findAllByStatus(List<EntityStatus> status){
		Iterable<Team> result = null;
		if(CollectionUtils.isEmpty(status)) {
			result = teamRepository.findByStatusIn(Arrays.asList(EntityStatus.ACTIVE));
		}else {
			result = teamRepository.findByStatusIn(status);
		}
		return StreamSupport.stream(result.spliterator(), false).sorted((x,y)->x.getName().compareTo(y.getName())).collect(Collectors.toList());
	}
	
	public Iterable<Team> findByMemberOfLevelAndUser(String username) {
		List<Long> externalIds = authTeamRepository.findByMemberOfLevelAndUser(username).stream().map(AuthTeam::getExternalId).collect(Collectors.toList());
		return StreamSupport.stream(teamRepository.findAll(externalIds).spliterator(),false).filter(t->Objects.equals(t.getStatus(), EntityStatus.ACTIVE)).collect(Collectors.toList());
	}
	
	public Iterable<Team> findTeamByCurrentDepartmentOfUser(String username){
		List<Long> externalIds = authTeamRepository.findTeamsByCurrentDepartmentOfUser(username, LocalDate.now().toEpochDay()).stream().map(AuthTeam::getExternalId).collect(Collectors.toList());
		return StreamSupport.stream(teamRepository.findAll(externalIds).spliterator(),false).filter(t->Objects.equals(t.getStatus(), EntityStatus.ACTIVE)).collect(Collectors.toList());
	}
	
	public Set<Node> listTeamWithHierarchy(List<EntityStatus> status) {
		Iterable<Team> teams = findAllByStatus(status);
		Set<Node> hierarchy =  authTeamRepository.findAllTeamsWithHierarchy()
				.stream()
				.filter(team -> team.getInherits().stream().map(Inherit::getEndNode).filter(AuthTeam.class::isInstance).map(BaseEntity::getId).allMatch(team.getId()::equals))
				.map(this::map)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		return extract(hierarchy, ()->StreamSupport.stream(teams.spliterator(), true));
	}
	
	public Iterable<Node> findByMemberOfLevelAndUserWithHierarchy(String username) {
		List<Long> externalIds = authTeamRepository.findByMemberOfLevelAndUser(username).stream().map(AuthTeam::getExternalId).collect(Collectors.toList());
		return extract(listTeamWithHierarchy(Collections.emptyList()), ()->StreamSupport.stream(teamRepository.findAll(externalIds).spliterator(),false).filter(t->Objects.equals(t.getStatus(), EntityStatus.ACTIVE)));
	}
	
	public Iterable<Node> findByMemberOfLevelAndUserWithHierarchy(Collection<Long> departments, Collection<Long> subsidiaries) {
		List<Long> externalIds = authTeamRepository.findTeamsWithHierarchyOfTeam(departments, subsidiaries).stream().map(AuthTeam::getExternalId).collect(Collectors.toList());
		return extract(listTeamWithHierarchy(Collections.emptyList()), ()->StreamSupport.stream(teamRepository.findAll(externalIds).spliterator(),false).filter(t->Objects.equals(t.getStatus(), EntityStatus.ACTIVE)));
	}
	
	public Iterable<Node> findTeamByCurrentDepartmentOfUserWithHierarchy(String username){
		List<Long> externalIds = authTeamRepository.findTeamsByCurrentDepartmentOfUser(username, LocalDate.now().toEpochDay()).stream().map(AuthTeam::getExternalId).collect(Collectors.toList());
		return extract(listTeamWithHierarchy(Collections.emptyList()), ()->StreamSupport.stream(teamRepository.findAll(externalIds).spliterator(),false).filter(t->Objects.equals(t.getStatus(), EntityStatus.ACTIVE)));
	}
	
	private Set<Node> extract(Set<Node> hierarchy, Supplier<Stream<Team>> teamSupplier) {
		Set<Node> result = new TreeSet<>();
		if(CollectionUtils.isNotEmpty(hierarchy)) {
			for (Node node : hierarchy) {
				if(teamSupplier.get().anyMatch(t->Objects.equals(t.getId(), node.getExternalId()))) {
					node.setChildren(extract(node.getChildren(), teamSupplier));
					result.add(node);
				} else {
					result.addAll(extract(node.getChildren(), teamSupplier));
				}
			}
		}
		return result;
	}
	
	private Node map(AuthTeam team){
    	List<AuthTeam> children = team.getInherits()
    			.stream()
    			.filter(t->Objects.equals(t.getEndNode().getId(), team.getId()))
    			.map(Inherit::getStartNode)
    			.filter(AuthTeam.class::isInstance)
    			.map(AuthTeam.class::cast)
    			.collect(Collectors.toList());
    	
    	Node node = Node.with(team);
    	if(CollectionUtils.isNotEmpty(children)) {
    		node.setChildren(children.stream().map(this::map).collect(Collectors.toCollection(LinkedHashSet::new)));
    	}
    	return node;
    }
}
