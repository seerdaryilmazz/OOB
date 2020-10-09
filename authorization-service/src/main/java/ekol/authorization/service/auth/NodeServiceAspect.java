package ekol.authorization.service.auth;

import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.authorization.domain.EntityStatus;
import ekol.authorization.domain.Team;
import ekol.authorization.domain.auth.AuthTeam;
import ekol.authorization.domain.auth.BaseEntity;
import ekol.authorization.dto.Node;
import ekol.authorization.repository.TeamRepository;
import ekol.authorization.repository.auth.AuthTeamRepository;
import lombok.AllArgsConstructor;

@Aspect
@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class NodeServiceAspect {
	
	private AuthTeamRepository authTeamRepository;
	private TeamRepository teamRepository;
	private Neo4jContextHelper neo4jContextHelper;
	private NodeService nodeService;
	
	@Pointcut("execution(* ekol.authorization.service.auth.NodeService.createOrLoadFromNode(..))")
	public void createOrLoadFromNode() {}
	
	@Transactional
	@Around("createOrLoadFromNode() && args(node)")
	public BaseEntity aroundCreateOrLoadFromNode(ProceedingJoinPoint joinPoint, Node node) throws Throwable {
		Class<?> nodeType = neo4jContextHelper.findNodeEntityClassFromNodeType(node.getType());
		if(AuthTeam.class.equals(nodeType)) {
			Long id = checkAndCreateTeam(node);
			node.setExternalId(id);
		}
		return (BaseEntity)joinPoint.proceed();
	}
	
	private Long checkAndCreateTeam(Node node) {
		if(Objects.isNull(node.getExternalId())) {
			AuthTeam authTeam = null;
			if(Objects.nonNull(node.getId())) {
				authTeam = authTeamRepository.findOne(node.getId(), 0);
			} else {
				authTeam = nodeService.loadFromName(node.getName(), AuthTeam.class);
			}
			if(Optional.ofNullable(authTeam).map(AuthTeam::getExternalId).isPresent()) {
				node.setExternalId(authTeam.getExternalId());
			} else {
				Team team = new Team();
				team.setCode(StringUtils.substring(node.getName(), 0, 10));
				team.setName(node.getName());
				team.setStatus(EntityStatus.ACTIVE);
				team = teamRepository.save(team);
				node.setExternalId(team.getId());
			}
		}
		return node.getExternalId();
	}
}
