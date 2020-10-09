package ekol.authorization.repository.auth;

import ekol.authorization.domain.auth.AuthSubsidiary;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthSubsidiaryRepository extends GraphRepository<AuthSubsidiary> {

    @Query("match (u:User{name:{username}}) \n" +
            "match (u)-[m:MEMBER_OF]->(n) \n" +
            "where ((not exists(m.start_date)) or (m.start_date <= {today})) \n" +
            "and ((not exists(m.end_date)) or (m.end_date >= {today})) \n" +
            "with m, n \n" +
            "match (n)-[m1:INHERIT*0..]->(n1:Subsidiary) \n" +
            "return n1;")
    Collection<AuthSubsidiary> retrieveUserParentSubsidiaries(@Param("username") String username, @Param("today") Long today);

    AuthSubsidiary findByName(String name);

    AuthSubsidiary findByExternalId(Long externalId);

    AuthSubsidiary findByNameOrExternalId(String name, Long externalId);

    @Query("match (n:Subsidiary)-[r]-() where id(n) = {id} return count(r);")
    long countRelations(@Param("id") Long id);

}
