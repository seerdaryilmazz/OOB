package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.LinehaulRouteLegStop;
import ekol.location.domain.LocationType;
import ekol.location.domain.RouteLegType;
import ekol.location.repository.LinehaulRouteLegStopRepository;
import ekol.location.util.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LinehaulRouteLegStopService {

    @Autowired
    private LinehaulRouteLegStopRepository linehaulRouteLegStopRepository;

    public LinehaulRouteLegStop findByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteLegStop persistedEntity = linehaulRouteLegStopRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRouteLegStop with specified id cannot be found: " + id);
        }
    }

    public LinehaulRouteLegStop findWithDetailsByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteLegStop persistedEntity = linehaulRouteLegStopRepository.findWithDetailsById(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRouteLegStop with specified id cannot be found: " + id);
        }
    }

    public Iterable<LinehaulRouteLegStop> findByRouteLegTypeWithDetails(RouteLegType routeLegType) {
        LocationType typeToSearch = null;
        if(RouteLegType.RAILWAY.equals(routeLegType)){
            typeToSearch = LocationType.TRAIN_TERMINAL;
        }else if(RouteLegType.SEAWAY.equals(routeLegType)){
            typeToSearch = LocationType.PORT;
        }

        return findByLocationTypeWithDetails(typeToSearch);
    }

    public Iterable<LinehaulRouteLegStop> findByLocationTypeWithDetails(LocationType locationType) {
        Iterable<LinehaulRouteLegStop> allStops = linehaulRouteLegStopRepository.findAllWithDetailsDistinctByOrderByName();
        if(locationType != null){
            return StreamSupport.stream(allStops.spliterator(), false).filter(item -> item.getTypes().contains(locationType)).collect(Collectors.toList());
        }

        return allStops;
    }

    public List<LinehaulRouteLegStop> findAllWithDetails() {

        List<LinehaulRouteLegStop> list = new ArrayList<>();

        for (LinehaulRouteLegStop s : linehaulRouteLegStopRepository.findAllWithDetailsDistinctByOrderByName()) {
            list.add(s);
        }

        return list;
    }

    @Transactional
    public LinehaulRouteLegStop createOrUpdate(LinehaulRouteLegStop stop) {

        if (stop == null) {
            throw new BadRequestException("LinehaulRouteLegStop cannot be null.");
        }

        if (stop.getId() != null) {
            findByIdOrThrowResourceNotFoundException(stop.getId());
        }

        if (StringUtils.isBlank(stop.getName())) {
            throw new BadRequestException("LinehaulRouteLegStop.name must contain at least 1 char.");
        }

        stop.setName(stop.getName().trim());

        if (stop.getPointOnMap() == null) {
            throw new BadRequestException("LinehaulRouteLegStop.pointOnMap cannot be null.");
        }

        if (stop.getPointOnMap().getLat() == null) {
            throw new BadRequestException("LinehaulRouteLegStop.pointOnMap.lat cannot be null.");
        }

        ServiceUtils.ensureLatitudeIsValid(stop.getPointOnMap().getLat(), "LinehaulRouteLegStop.pointOnMap.lat");

        if (stop.getPointOnMap().getLng() == null) {
            throw new BadRequestException("LinehaulRouteLegStop.pointOnMap.lng cannot be null.");
        }

        ServiceUtils.ensureLongitudeIsValid(stop.getPointOnMap().getLng(), "LinehaulRouteLegStop.pointOnMap.lng");

        if (stop.getTypes() == null || stop.getTypes().size() == 0) {
            throw new BadRequestException("LinehaulRouteLegStop.types must have at least one element.");
        }

        LinehaulRouteLegStop stopByName = linehaulRouteLegStopRepository.findByName(stop.getName());

        if (stopByName != null) {
            if (stop.getId() == null || !stop.getId().equals(stopByName.getId())) {
                throw new BadRequestException("LinehaulRouteLegStop.name is in use.");
            }
        }

        return linehaulRouteLegStopRepository.save(stop);
    }

    @Transactional
    public void softDelete(Long id) {
        LinehaulRouteLegStop persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        linehaulRouteLegStopRepository.save(persistedEntity);
    }
}
