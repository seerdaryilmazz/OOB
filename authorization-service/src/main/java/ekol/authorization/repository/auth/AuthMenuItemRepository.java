package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.AuthMenuItem;

/**
 * Created by kilimci on 18/04/2017.
 */
@Repository
public interface AuthMenuItemRepository extends GraphRepository<AuthMenuItem> {

//    @Query("match (u:User{name:{username}})\n" +
//            "match (u)-[m:MEMBER_OF]->(nm)\n" +
//            "where m.level = 300\n" +
//            "and ((not exists(m.start_date)) or (m.start_date <= {today}))\n" +
//            "and ((not exists(m.end_date)) or (m.end_date >= {today}))\n" +
//            "with m, nm\n" +
//            "optional match (nmi)-[:INHERIT*1..]->(nm)\n" +
//            "optional match (nmi)-[:INHERIT*1..]->(inm)\n" +
//            "optional match (nmi)-[cvnmi:CAN_VIEW]->(minmi:MenuItem)\n" +
//            "optional match (inm)-[cvinm:CAN_VIEW]->(miinm:MenuItem)\n" +
//            "unwind [minmi, miinm] as mi\n" +
//            "return distinct(mi)\n" +
//            "union all\n" +
//            "match (u:User{name:{username}})-[m:MEMBER_OF]->(d)-[i:INHERIT*0..]->(di)-[cv:CAN_VIEW]->(mi:MenuItem)\n" +
//            "where m.level >= cv.level\n" +
//            "and ((not exists(m.start_date)) or (m.start_date <= {today}))\n" +
//            "and ((not exists(m.end_date)) or (m.end_date >= {today}))\n" +
//            "return mi\n")
	
	@Query("MATCH(u:User{name:{username}})-[m:MEMBER_OF]->(t) " + 
			"WHERE ((not exists(m.start_date)) or (m.start_date <= {today})) " + 
			"AND ((not exists(m.end_date)) or (m.end_date >= {today})) " + 
			"WITH t, m " + 
			"OPTIONAL MATCH(t)-[INHERIT*0..]->(d:Department) " + 
			"OPTIONAL MATCH(t)-[INHERIT*0..]->(s:Subsidiary) " + 
			"UNWIND[t,d,s] AS n " + 
			"WITH n, m " + 
			"MATCH(n)-[cv:CAN_VIEW]->(mi:MenuItem) " + 
			"WHERE cv.level <= m.level " + 
			"RETURN DISTINCT(mi)")
    List<AuthMenuItem> findMenuItemsByUserName(@Param("username") String username, @Param("today") Long today);

    @Query("match(mi:MenuItem{externalId:{menuId}})<-[r:CAN_VIEW]-(n) return mi,n,r")
    List<AuthMenuItem> findMenuItemRelations(@Param("menuId") Long menuId);

    @Query("MATCH (f), (t) WHERE ID(f) = {fromId} and ID(t) = {toId} MERGE (f)-[a:CAN_VIEW]->(t) SET a.level = {level}")
    void mergeMenuItemRelation(@Param("fromId") Long fromId, @Param("toId") Long toId, @Param("level") Long level);

    @Query("match(mi:MenuItem{externalId:{fromId}})<-[r:CAN_VIEW]-(n) delete r")
    void deleteMenuItemRelation(@Param("fromId") Long fromId);

    @Query("match(mi:MenuItem{externalId:{fromId}})<-[r:CAN_VIEW]-(n) delete r, mi")
    void deleteMenuItemRelationAndMenuItem(@Param("fromId") Long fromId);



}
