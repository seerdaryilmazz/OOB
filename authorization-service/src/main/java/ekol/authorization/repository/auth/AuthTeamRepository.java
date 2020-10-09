package ekol.authorization.repository.auth;

import java.util.*;

import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.*;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthTeamRepository extends GraphRepository<AuthTeam> {
	
	AuthTeam findByExternalId(Long externalId, @Depth int depth);
	
	@Query("match(cg:CustomerGroup)<-[INHERIT]-(t:Team) where ID(cg)={id} return t")
	List<AuthTeam> findByCustomerGroup(@Param("id") Long id);
	
	@Depth(0)
	List<AuthTeam> findByExternalIdIsNull();
	
	@Query("MATCH (n:Team)-[r:INHERIT*0..]->(d:Department) WHERE ID(d) = {id} RETURN n")
	List<AuthTeam> findByInheritedTeams(@Param("id") Long id);
	
	@Query("MATCH (u:User)-[m:MEMBER_OF]->(t:Team)<-[INHERI*0..]-(it:Team) "
			+ "WHERE u.name={username} "
			+ "RETURN CASE m.level WHEN 300 THEN it WHEN 200 THEN t ELSE null END")
	List<AuthTeam> findByMemberOfLevelAndUser(@Param("username") String username);

	
	@Query("MATCH(u:User{name:{username}})-[m:MEMBER_OF]->(n)-[in:INHERIT*0..]->(d:Department) "
			+ "WHERE (not exists(m.start_date) or m.start_date <= {today}) "
    		+ "AND (not exists(m.end_date) or m.end_date >= {today}) "
			+ "WITH d MATCH(d)<-[id:INHERIT*0..]-(t:Team) "
			+ "RETURN t")
	List<AuthTeam> findTeamsByCurrentDepartmentOfUser(@Param("username") String username, @Param("today") Long today);

	@Query("MATCH (u:User)-[m:MEMBER_OF]->(t:Team) "
			+ "WHERE not exists(m.start_date) AND not exists(m.end_date) "
			+ "WITH u,t "
			+ "MATCH p=(d:Department{externalId:{externalId}})<-[i:INHERIT*0..]-(t) "
			+ "RETURN  u as user, nodes(p) as nodes, length(i) as relationshipLength")
	List<AuthLevel> getAuthLevelOfDepartment(@Param("externalId") Long externalId);
	
	@Query("MATCH (u:User{name:{username}})-[m:MEMBER_OF]->(t:Team) "
			+ "WHERE (not exists(m.start_date) or m.start_date <= {today}) "
			+ "AND (not exists(m.end_date) or m.end_date >= {today}) "
			+ "WITH u,t match p=(d:Department)<-[i:INHERIT*0..]-(t) "
			+ "RETURN  u as user, nodes(p) as nodes, length(i) as relationshipLength")
	List<AuthLevel> getAuthLevelOfUser(@Param("username") String username, @Param("today") Long today);
	
	@Query("MATCH p=()<-[i:INHERIT]-(t:Team) RETURN p")
	List<AuthTeam> findAllTeamsWithHierarchy();
	
	@Query("MATCH (t:Team)<-[INHERIT*0..]-(n:Team),(d:Department),(s:Subsidiary) WHERE d.externalId in {department} and s.externalId in {subsidiary} and  (d)<-[:INHERIT]-(t) AND (s)<-[:INHERIT]-(t) return t,n")
	List<AuthTeam> findTeamsWithHierarchyOfTeam(@Param("department") Collection<Long> department, @Param("subsidiary") Collection<Long> subsidiary);
}
