package ekol.kartoteks.controller;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.Sector;
import ekol.kartoteks.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by fatmaozyildirim on 3/16/16.
 */
@RestController
@RequestMapping("/sector")
public class SectorController {

    @Autowired
    private SectorRepository sectorRepository;

    @RequestMapping(value="", method = RequestMethod.POST)
    public Sector addParentSector(@RequestBody Sector sector){
        return sectorRepository.save(sector);
    }

    @RequestMapping(value="", method= RequestMethod.GET)
    public List<Sector> getParentSectorList(){
        return sectorRepository.findByParent(null);
    }

    @RequestMapping(value="/{sectorId}", method= RequestMethod.PUT)
    public Sector updateParentSector(@PathVariable Long sectorId, @RequestBody Sector sector){
        if(sector == null)
            throw new BadRequestException("sector is null");
        if(sectorId != sector.getId())
            throw new BadRequestException("sectorIds are not equal");
        Sector s =sectorRepository.findOne(sectorId);
        if(s==null)
            throw new ResourceNotFoundException();

        return sectorRepository.save(sector);
    }

    @RequestMapping(value="/{sectorId}", method= RequestMethod.DELETE)
    public Sector deleteParentSector(@PathVariable Long sectorId){
        Sector sector = sectorRepository.findOne(sectorId);
        if(sector == null)
            throw new ResourceNotFoundException();
        sector.setDeleted(true);
        return sectorRepository.save(sector);
    }

    @RequestMapping(value="/{sectorId}/subsectors",method = RequestMethod.GET)
    public List<Sector> getSubSectors(@PathVariable Long sectorId){
        Sector parent = sectorRepository.findOne(sectorId);
        if(parent == null)
            throw new ResourceNotFoundException();
        return sectorRepository.findByParent(parent);
    }


    @RequestMapping(value="/{sectorId}/subsectors",method = RequestMethod.POST)
    public Sector addSubSector(@PathVariable Long sectorId, @RequestBody Sector sector){
        Sector parent = sectorRepository.findOne(sectorId);
        if(parent == null)
            throw new ResourceNotFoundException();
        sector.setParent(parent);
        return sectorRepository.save(sector);
    }

}
