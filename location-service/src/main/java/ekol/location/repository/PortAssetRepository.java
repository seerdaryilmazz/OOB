package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.port.Port;
import ekol.location.domain.location.port.PortAsset;

/**
 * Created by burak on 11/04/17.
 */
public interface PortAssetRepository extends ApplicationRepository<PortAsset> {

    void removeByPort(Port port);

}