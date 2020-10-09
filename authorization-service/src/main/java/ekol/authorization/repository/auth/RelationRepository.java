package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.AuthOperation;
import ekol.authorization.domain.auth.BaseRelation;

/**
 * Created by ozer on 09/03/2017.
 */
@Repository
public interface RelationRepository extends GraphRepository<AuthOperation> {
	
	@Query("match(f)-[r:INHERIT]->(t) where ID(f) = {fromId} AND ID(t) = {toId} delete r")
	void deleteInheritRelation(@Param("fromId") Long fromId, @Param("toId") Long toId);

	@Query("match(f)-[m:MEMBER_OF]->(t) where ID(f) = {fromId} AND ID(t) = {toId} delete m")
	void deleteMemberOfRelation(@Param("fromId") Long fromId, @Param("toId") Long toId);

	@Query("match(f)-[r]->(t) where ID(f) = {fromId} AND ID(t) = {toId} return r")
	List<BaseRelation> getRelations(@Param("fromId") Long fromId, @Param("toId") Long toId);
	
    @Query("MATCH (f), (t) WHERE ID(f) = {fromId} and ID(t) = {toId} MERGE (f)-[i:INHERIT]->(t)")
    void mergeInheritRelation(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query("MATCH (f), (t) WHERE ID(f) = {fromId} and ID(t) = {toId} MERGE (f)-[a:AUTHORIZED]->(t) SET a.level = {level}")
    void mergeAuthorizedRelation(@Param("fromId") Long fromId, @Param("toId") Long toId, @Param("level") Long level);

    @Query("MATCH (f), (t) WHERE ID(f) = {fromId} and ID(t) = {toId} MERGE (f)-[m:MEMBER_OF]->(t) SET m.level = {level}, m.start_date = {start_date}, m.end_date = {end_date}")
    void mergeMemberOfRelation(@Param("fromId") Long fromId, @Param("toId") Long toId, @Param("level") Long level, @Param("start_date") Long startDate, @Param("end_date") Long endDate);
}
