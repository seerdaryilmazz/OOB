package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.terminal.Terminal;

import java.util.List;

/**
 * Created by kilimci on 28/04/2017.
 */
public interface TerminalRepository extends ApplicationRepository<Terminal> {

    public List<Terminal> findByRouteLegStopId(Long routeLegStopId);

}
