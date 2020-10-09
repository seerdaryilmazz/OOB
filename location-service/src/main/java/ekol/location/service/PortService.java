package ekol.location.service;

import ekol.location.domain.location.port.Port;
import ekol.location.domain.location.port.PortAsset;
import ekol.location.repository.PortAssetRepository;
import ekol.location.repository.PortRepository;
import ekol.location.validator.PortValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by burak on 05/04/17.
 */
@Service
public class PortService {

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PortValidator portValidator;

    @Autowired
    private PortAssetRepository portAssetRepository;

    @Autowired
    private LinehaulRouteLegStopService linehaulRouteLegStopService;

    @Transactional
    public Port save(Port port){
        portValidator.validate(port);
        port.setEstablishment(establishmentService.save(port.getEstablishment()));
        port.getLocation().setTimezone(port.getCountry().getTimezone());
        port.setLocation(locationService.save(port.getLocation()));
        port.setRouteLegStop(linehaulRouteLegStopService.createOrUpdate(port.buildRouteLegStop()));
        Port saved = portRepository.save(port);
        saveAssets(port.getAssets(), saved);
        return saved;
    }

    private void saveAssets(Set<PortAsset> assets, Port port){
        if(assets != null){
            assets.forEach(asset -> {
                asset.setPort(port);
                portAssetRepository.save(asset);
            });
        }
    }

    @Transactional
    public void delete(Long id){
        Port port = portRepository.findOne(id);
        if(port != null){
            linehaulRouteLegStopService.softDelete(port.getRouteLegStop().getId());
            port.setDeleted(true);
            portRepository.save(port);
        }
    }
}
