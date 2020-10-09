package ekol.authorization.repository.auth;

import ekol.authorization.domain.auth.AuthHomepage;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AuthHomepageRepository extends GraphRepository<AuthHomepage>{
//	@Query("match (u:User{name:{username}}) \n" +
//            "match (u)-[m:MEMBER_OF]->(n) \n" +
//            "where ((not exists(m.start_date)) or (m.start_date <= {today})) \n" +
//            "and ((not exists(m.end_date)) or (m.end_date >= {today})) \n" +
//            "with m, n \n" +
//            "match (n)-[m1:USES*0..]->(h:Homepage) \n" +
//            "return h;")
    @Query("MATCH (u:User{name:{username}})-[m:MEMBER_OF]->(n)-[INHERIT*0..]->(d) " + 
    		"WHERE ((NOT exists(m.start_date)) or (m.start_date <= {today})) " + 
    		"AND ((NOT exists(m.end_date)) or (m.end_date >= {today})) " + 
    		"UNWIND [n, d] as t " + 
    		"WITH t " + 
    		"MATCH (t)-[u:USES]->(h:Homepage) " + 
    		"RETURN distinct(h)")
    Collection<AuthHomepage> retrieveUserHomepages(@Param("username") String username, @Param("today") Long today);
}
