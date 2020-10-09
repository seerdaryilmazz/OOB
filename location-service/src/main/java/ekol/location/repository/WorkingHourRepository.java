package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.comnon.Establishment;
import ekol.location.domain.location.comnon.workinghour.WorkingHour;

/**
 * Created by burak on 11/04/17.
 */
public interface WorkingHourRepository extends ApplicationRepository<WorkingHour> {

    void removeByEstablishment(Establishment establishment);

}