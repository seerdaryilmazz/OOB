package ekol.authorization.repository.auth;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ekol.authorization.domain.auth.AuthCustomerGroup;

@Repository
public interface AuthCustomerGroupRepository extends GraphRepository<AuthCustomerGroup> {

	AuthCustomerGroup findByName(String name);

	AuthCustomerGroup findByExternalId(Long externalId);

	Iterable<AuthCustomerGroup> findByExternalIdIn(Iterable<Long> externalId);

	@Query("match(cg:CustomerGroup)<-[r:INHERIT]-(t) where ID(t) = {id} return cg,r,t")
	Iterable<AuthCustomerGroup> findByInheritNodeId(@Param("id") Long id);

	@Query("match(cg:CustomerGroup)<-[r:INHERIT]-(t) where t.externalId = {externalId} return cg,r,t")
	Iterable<AuthCustomerGroup> findByInheritNodeExternalId(@Param("externalId") Long externalId);

	@Query("match(cg:CustomerGroup)<-[r:INHERIT]-(t) where t.externalId in ({externalIds}) return cg,r,t")
	Iterable<AuthCustomerGroup> findByInheritNodeExternalIdIn(@Param("externalIds") Iterable<Long> externalIds);
	
}
