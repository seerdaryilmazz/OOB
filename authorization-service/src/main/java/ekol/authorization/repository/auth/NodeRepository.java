package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.*;

/**
 * Created by ozer on 09/03/2017.
 */
@Repository
public interface NodeRepository extends GraphRepository<AuthOperation> {

    @Query("MATCH (n) WHERE NOT n:Operation RETURN n")
    List<Object> findAllNodesExceptOperations();

    @Query("match (u:User{name:{username}})\n" +
            "match (u)-[m:MEMBER_OF]->(nm)\n" +
            "where ((not exists(m.start_date)) or (m.start_date <= {today}))\n" +
            "and ((not exists(m.end_date)) or (m.end_date >= {today}))\n" +
            "with m, nm\n" +
            "optional match (nmi)-[:INHERIT*1..]->(nm)\n" +
            "optional match (nmi)-[:INHERIT*1..]->(inm)\n" +
            "optional match (nm)-[:INHERIT*0..]->(di)\n" +
            "unwind [nm, nmi, inm, di] as manager_nodes\n" +
            "unwind [nm, di] as non_manager_nodes\n" +
            "return distinct(\n" +
            "  case m.level\n" +
            "    when 300 then\n" +
            "      manager_nodes\n" +
            "    else\n" +
            "      non_manager_nodes\n" +
            "  end)")
    List<Object> findNodesByUserName(@Param("username") String username, @Param("today") Long today);

    @Query("match (u:User{name:{username}}) match (u)-[m:MEMBER_OF]->(nm) return m,nm")
    List<Object> findUsersMemberships(@Param("username") String username);

    @Query("match (n) where NOT n:Operation and NOT n:User and NOT n:Xport and NOT n:MenuItem return distinct(labels(n)) as names")
    List<LabelQueryResult> findAllNodeLabelsForAuthorization();

    @Query("match(n{name:{name}}) where ID(n) = {id} return n")
    BaseEntity findByIdAndName(@Param("id") Long id, @Param("name") String name);

    @Query("match(n{name:{name}, externalId:{id}}) return n")
    BaseEntity findByExternalIdAndName(@Param("id") Long id, @Param("name") String name);
    
    @Query("match (u:User{name:{username}}) with u optional match (u)-[r:MEMBER_OF]->(t)-[i:INHERIT*0..]->(n) where \n" +
            "((not exists(r.start_date)) or (r.start_date <= {today})) and ((not exists(r.end_date)) or (r.end_date >= {today})) return n")
    Iterable<BaseEntity> findActiveMembershipsByUsername(@Param("username") String username, @Param("today") Long today);
 
}
