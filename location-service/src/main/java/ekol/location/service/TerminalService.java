package ekol.location.service;

import ekol.location.domain.location.terminal.Terminal;
import ekol.location.domain.location.terminal.TerminalAsset;
import ekol.location.repository.LocationRepository;
import ekol.location.repository.TerminalAssetRepository;
import ekol.location.repository.TerminalRepository;
import ekol.location.validator.TerminalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by kilimci on 28/04/2017.
 */
@Service
public class TerminalService {
    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TerminalValidator terminalValidator;

    @Autowired
    private TerminalAssetRepository terminalAssetRepository;

    @Autowired
    private LinehaulRouteLegStopService linehaulRouteLegStopService;

    @Transactional
    public Terminal save(Terminal terminal){
        terminalValidator.validate(terminal);
        terminal.setEstablishment(establishmentService.save(terminal.getEstablishment()));
        terminal.getLocation().setTimezone(terminal.getCountry().getTimezone());
        terminal.setLocation(locationRepository.save(terminal.getLocation()));
        terminal.setRouteLegStop(linehaulRouteLegStopService.createOrUpdate(terminal.buildRouteLegStop()));
        Terminal saved = terminalRepository.save(terminal);
        saveAssets(terminal.getAssets(), saved);
        return saved;
    }

    private void saveAssets(Set<TerminalAsset> assets, Terminal terminal){
        if(assets != null){
            assets.forEach(asset -> {
                asset.setTerminal(terminal);
                terminalAssetRepository.save(asset);
            });
        }
    }

    @Transactional
    public void delete(Long id){
        Terminal terminal = terminalRepository.findOne(id);
        if(terminal != null){
            linehaulRouteLegStopService.softDelete(terminal.getRouteLegStop().getId());
            terminal.setDeleted(true);
            terminalRepository.save(terminal);
        }
    }

}
