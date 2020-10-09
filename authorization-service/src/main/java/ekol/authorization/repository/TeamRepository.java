package ekol.authorization.repository;

import java.util.List;

import ekol.authorization.domain.EntityStatus;
import ekol.authorization.domain.Team;
import ekol.hibernate5.domain.repository.LookupRepository;

public interface TeamRepository extends LookupRepository<Team>{
	
	public Team findByName(String name);
	
	public Team findByNameIgnoreCase(String name);
	
	public List<Team> findByStatusIn(List<EntityStatus> status);

}
