package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.Coordinate;

import java.util.List;

public interface CoordinateRepository extends ApplicationRepository<Coordinate> {

    public List<Coordinate> findAllByRingIdOrderByOrderNo(Long ringId);

}
