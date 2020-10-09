package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.AuthUser;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthUserRepository extends GraphRepository<AuthUser> {

    @Query("match (u)-[:MEMBER_OF{level:{level}}]->(n) where id(n) = {nodeId} return u")
    List<AuthUser> findDirectMembersOfANodeByNodeIdAndLevel(@Param("nodeId") Long nodeId, @Param("level") Long level);

    @Query("match (u)-[r:MEMBER_OF]->(n) where id(n) = {nodeId} return u,r,n")
    List<AuthUser> findDirectMembersOfANodeByNodeId(@Param("nodeId") Long nodeId);

    @Query("match (u:User{name:{username}}) with u optional match (u)-[r:MEMBER_OF]->(n) return u,r,n")
    AuthUser findByUsername(@Param("username") String username);
    

    @Query("match (u:User{name:{username}}) with u optional match (u)-[r:MEMBER_OF]->(n) where \n" +
            "((not exists(r.start_date)) or (r.start_date <= {today})) and ((not exists(r.end_date)) or (r.end_date >= {today})) return u,r,n")
    AuthUser findActiveMembershipsByUsername(@Param("username") String username, @Param("today") Long today);
    
    
    //retrieve by user name and team
    
    @Query("match (u:User {name:{username}})-[:MEMBER_OF]->(t:Team)<-[:MEMBER_OF]-(a:User) unwind[a,u] as r return distinct r")
    Iterable<AuthUser> retrieveByUsernameAndTeam(@Param("username") String username);

    @Query("match (u:User{name:{username}})-[r:MEMBER_OF]->(n) delete r")
    void deleteMembershipsByUsername(@Param("username") String username);

    @Query("MATCH (t)<-[r2:MEMBER_OF]-(m:User) "
    		+ "WHERE ID(t) = {id} "
    		+ "AND (not exists(r2.start_date) or r2.start_date <= {today}) "
    		+ "AND (not exists(r2.end_date) or r2.end_date >= {today}) "
    		+ "RETURN m,r2")
    List<AuthUser> findActiveMembershipsByNodeId(@Param("id") Long id, @Param("today") Long today);
    
    @Query("MATCH (u:User)-[r1:MEMBER_OF]->(t)<-[r2:MEMBER_OF]-(m:User) "
    		+ "WHERE u.name={username} "
    		+ "AND r1.level>r2.level "
    		+ "AND ID(t) = {id} "
    		+ "AND (not exists(r1.start_date) or r1.start_date <= {today}) "
    		+ "AND (not exists(r1.end_date) or r1.end_date >= {today}) "
    		+ "AND (not exists(r2.start_date) or r2.start_date <= {today}) "
    		+ "AND (not exists(r2.end_date) or r2.end_date >= {today}) "
    		+ "RETURN m,r2")
    List<AuthUser> findActiveSubLevelMemberOfNodeAndUsername(@Param("username") String username, @Param("id") Long id, @Param("today") Long today);
    
    @Query("MATCH(o:Operation)<-[AUTHORIZED]-()<-[i:INHERIT*0..]-()<-[m:MEMBER_OF]-(u:User) "
    		+ "WHERE o.name={operationName} "
    		+ "AND (not exists(m.start_date) or m.start_date <= {today}) "
    		+ "AND (not exists(m.end_date) or m.end_date >= {today}) "
    		+ "RETURN u")
    List<AuthUser> findByAuthorizedOperation(@Param("operationName") String operationName, @Param("today") Long today);
    
    @Query("MATCH (u:User)-[r:MEMBER_OF]->(t)<-[r1:MEMBER_OF]-(m1:User) "
    		+ "WHERE u.name={username} "
    		+ "AND r.level>=r1.level "
    		+ "AND ID(t) = {id} "
    		+ "AND (not exists(r.start_date) or r.start_date <= {today}) "
    		+ "AND (not exists(r.end_date) or r.end_date >= {today}) "
    		+ "AND (not exists(r1.start_date) or r1.start_date <= {today}) "
    		+ "AND (not exists(r1.end_date) or r1.end_date >= {today}) "
    		+ "WITH t, m1, r "
    		+ "OPTIONAL MATCH (t)<-[INHERIT*1..]-(it)<-[r2:MEMBER_OF]-(m2:User) "
    		+ "WHERE (not exists(r2.start_date) or r2.start_date <= {today}) "
    		+ "AND (not exists(r2.end_date) or r2.end_date >= {today}) "
    		+ "UNWIND [m1, m2] as m "
    		+ "RETURN CASE WHEN r.level = 300 THEN m WHEN r.level = 200 THEN m1 ELSE null END")
    List<AuthUser> findActiveSubLevelAllMemberOfNodeAndUsernameByUsernameAndNode(@Param("username") String username, @Param("id") Long id, @Param("today") Long today);

    @Query("MATCH (t)<-[INHERIT*0..]-(it)<-[r2:MEMBER_OF]-(m2:User) "
    		+ "WHERE ID(t) = {id}"
    		+ "AND (not exists(r2.start_date) or r2.start_date <= {today}) "
    		+ "AND (not exists(r2.end_date) or r2.end_date >= {today}) "
    		+ "RETURN m2")
    List<AuthUser> findActiveSubLevelAllMemberOfNodeAndUsernameByNode(@Param("id") Long id, @Param("today") Long today);
    
    @Query("MATCH(u:User)-[m:MEMBER_OF]->(t:Team)<-[m1:MEMBER_OF]-(y1:User)"
    		+ "WHERE u.name={username} "
    		+ "AND m1.level>m.level "
    		+ "AND (not exists(m.start_date) or m.start_date <= {today}) "
    		+ "AND (not exists(m.end_date) or m.end_date >= {today}) "
    		+ "AND (not exists(m1.start_date) or m1.start_date <= {today}) "
    		+ "AND (not exists(m1.end_date) or m1.end_date >= {today}) "
    		+ "WITH t, m, m1,y1 "
    		+ "OPTIONAL MATCH(t)-[i:INHERIT*1..]->(u:Team)<-[m2:MEMBER_OF]-(y2:User) " 
    		+ "WHERE m2.level>m.level "
    		+ "AND (not exists(m2.start_date) or m2.start_date <= {today}) "
    		+ "AND (not exists(m2.end_date) or m2.end_date >= {today}) "
    		+ "RETURN y1,y2")
    List<AuthUser> findUpLevelUsersOfUser(@Param("username")String username, @Param("today") Long today);
}
