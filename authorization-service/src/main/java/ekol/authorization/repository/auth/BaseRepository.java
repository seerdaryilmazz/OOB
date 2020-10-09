package ekol.authorization.repository.auth;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import ekol.authorization.domain.auth.BaseEntity;
import ekol.authorization.domain.auth.BaseRelation;

public interface BaseRepository extends GraphRepository<BaseEntity>  {

	   
    @Query("match(n) where ID(n) = {id} return n")
    BaseEntity findById(@Param("id") Long id);
    
    @Query("match(n)-[r:INHERIT]-(m) where ID(n) = {id1} and ID(m) = {id2} return r")
    BaseRelation findInheritRelation(@Param("id1") Long node1Id, @Param("id2") Long node2Id);

    @Query("match(n)-[r:INHERIT|:MEMBER_OF]-(m) where ID(n) = {id} return count(r)")
    int countInheritAndMemberOfRelations(@Param("id") Long id);

    @Query("match(n)-[r]-(m) where ID(n) = {id} return count(r)")
    int countRelations(@Param("id") Long id);
    
    @Query("match(n)-[r]-(m) where ID(n) = {id1} return r")
    List<BaseRelation> findRelations(@Param("id1") Long id);
    
    @Query("match(n) where n.externalId = {externalId} return n")
    List<BaseEntity> findByExternalId(@Param("externalId")Long externalId);
}
