package ekol.authorization.repository.auth;

import ekol.authorization.domain.auth.AuthLocation;
import ekol.authorization.domain.auth.AuthUser;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by ozer on 08/03/2017.
 */
@Repository
public interface AuthLocationRepository extends GraphRepository<AuthLocation> {

    @Query("match (u:User{name:{username}}) \n" +
            "match (u)-[m:MEMBER_OF]->(n) \n" +
            "where ((not exists(m.start_date)) or (m.start_date <= {today})) \n" +
            "and ((not exists(m.end_date)) or (m.end_date >= {today})) \n" +
            "with m, n \n" +
            "match (n)-[m1:INHERIT*0..]->(n1:Location) \n" +
            "return n1;")
    Collection<AuthLocation> retrieveUserLocations(@Param("username") String username, @Param("today") Long today);

    @Query("match (n:Location{externalId:{locationId}})-[r:INHERIT]-(u:User) return u")
    List<AuthUser> getUsersOfLocation(@Param("locationId") Long locationId);
}
