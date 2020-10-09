package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.AuthDepartment;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthDepartmentRepository extends GraphRepository<AuthDepartment> {
	
	@Query("MATCH(d:Department)<-[r:INHERIT*1..]-(t:Team) WHERE ID(t)={teamId} RETURN d")
	List<AuthDepartment> findByInheritTeamId(@Param("teamId") Long teamId);

	@Query("MATCH(d:Department)<-[r:INHERIT*1..]-(t:Team) WHERE t.externalId={teamExternalId} RETURN d")
	List<AuthDepartment> findByInheritTeamExternalId(@Param("teamExternalId") Long teamExternalId);
}
