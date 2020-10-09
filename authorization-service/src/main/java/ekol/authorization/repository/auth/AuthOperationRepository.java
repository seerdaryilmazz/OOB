package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.AuthOperation;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthOperationRepository extends GraphRepository<AuthOperation> {

//    @Query("match (u:User{name:{username}})\n" +
//            "match (u)-[m:MEMBER_OF]->(nm)\n" +
//            "where m.level = 300\n" +
//            "and ((not exists(m.start_date)) or (m.start_date <= {today}))\n" +
//            "and ((not exists(m.end_date)) or (m.end_date >= {today}))\n" +
//            "with m, nm\n" +
//            "optional match (nmi)-[:INHERIT*1..]->(nm)\n" +
//            "optional match (nmi)-[:INHERIT*1..]->(inm)\n" +
//            "optional match (nmi)-[anmi:AUTHORIZED]->(onmi:Operation)\n" +
//            "optional match (inm)-[ainm:AUTHORIZED]->(oinm:Operation)\n" +
//            "unwind [onmi, oinm] as o\n" +
//            "return distinct(o)\n" +
//            "union all\n" +
//            "match (u:User{name:{username}})-[m:MEMBER_OF]->(d)-[i:INHERIT*0..]->(di)-[a:AUTHORIZED]->(o:Operation)\n" +
//            "where m.level >= a.level\n" +
//            "and ((not exists(m.start_date)) or (m.start_date <= {today}))\n" +
//            "and ((not exists(m.end_date)) or (m.end_date >= {today}))\n" +
//            "return o\n")
	
	@Query("MATCH(u:User{name:{username}})-[m:MEMBER_OF]->(t) " + 
			"WHERE ((not exists(m.start_date)) or (m.start_date <= {today})) " + 
			"AND ((not exists(m.end_date)) or (m.end_date >= {today})) " + 
			"WITH t, m " + 
			"OPTIONAL MATCH(t)-[INHERIT*0..]->(d:Department) " + 
			"OPTIONAL MATCH(t)-[INHERIT*0..]->(s:Subsidiary) " + 
			"UNWIND[t,d,s] AS n " + 
			"WITH n, m " + 
			"MATCH(n)-[a:AUTHORIZED]->(o:Operation) " + 
			"WHERE a.level <= m.level " + 
			"RETURN DISTINCT(o)")
    List<AuthOperation> findAuthorizedOperationsByUserName(@Param("username") String username, @Param("today") Long today);

    @Query("match(op:Operation{name:{name}})<-[r:AUTHORIZED]-(n) return op,n,r")
    List<AuthOperation> findAuthorizedRelations(@Param("name") String name);

    @Query("match(op:Operation{name:{name}})<-[r:AUTHORIZED]-(n) delete r")
    void deleteOperationAuthRelation(@Param("name") String name);
    
//    @Query("match(t)-[INHERIT*0..]->(n)-[AUTHORIZED]->(o:Operation) where ID(t) = {id} return o")
    @Query("MATCH(t)-[INHERIT*0..]->(s:Subsidiary)-[AUTHORIZED]->(o:Operation) WHERE ID(t) = {id} return distinct(o) " + 
    		"UNION " + 
    		"MATCH(t)-[INHERIT*0..]->(d:Department)-[AUTHORIZED]->(o:Operation) WHERE ID(t) = {id} return distinct(o)")
    List<AuthOperation> findInheritedAutorizedOperations(@Param("id") Long id);

    @Query("match(t)-[AUTHORIZED]->(o:Operation) where ID(t) = {id} return o")
    List<AuthOperation> findAutorizedOperations(@Param("id") Long id);

}
