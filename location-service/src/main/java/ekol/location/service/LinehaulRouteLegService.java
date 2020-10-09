package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.*;
import ekol.location.domain.dto.RouteLegExpeditionResponse;
import ekol.location.domain.dto.RouteResponse;
import ekol.location.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LinehaulRouteLegService {

    @Autowired
    private LinehaulRouteLegRepository linehaulRouteLegRepository;

    @Autowired
    private LinehaulRouteLegScheduleRepository linehaulRouteLegScheduleRepository;

    @Autowired
    private LinehaulRouteLegNonScheduleRepository linehaulRouteLegNonScheduleRepository;

    @Autowired
    private LinehaulRouteLegScheduleService linehaulRouteLegScheduleService;

    @Autowired
    private LinehaulRouteLegNonScheduleService linehaulRouteLegNonScheduleService;

    @Autowired
    private LinehaulRouteLegStopService linehaulRouteLegStopService;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private RouteLegExpeditionService routeLegExpeditionService;

    public LinehaulRouteLeg findByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteLeg persistedEntity = linehaulRouteLegRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRouteLeg with specified id cannot be found: " + id);
        }
    }

    public LinehaulRouteLeg findWithDetailsByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRouteLeg persistedEntity = linehaulRouteLegRepository.findWithDetailsById(id);

        if (persistedEntity != null) {
            LinehaulRouteLegStop from =
                    linehaulRouteLegStopService.findWithDetailsByIdOrThrowResourceNotFoundException(persistedEntity.getFrom().getId());
            LinehaulRouteLegStop to =
                    linehaulRouteLegStopService.findWithDetailsByIdOrThrowResourceNotFoundException(persistedEntity.getTo().getId());
            persistedEntity.setFrom(from);
            persistedEntity.setTo(to);
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRouteLeg with specified id cannot be found: " + id);
        }
    }

    public List<LinehaulRouteLeg> findAllWithDetails() {

        List<LinehaulRouteLeg> list = new ArrayList<>();

        for (LinehaulRouteLeg l : linehaulRouteLegRepository.findAllWithDetailsDistinctBy()) {
            list.add(l);
        }

        return list;
    }

    public List<LinehaulRouteLeg> findAllWithDetailsByType(RouteLegType type) {

        List<LinehaulRouteLeg> list = new ArrayList<>();

        for (LinehaulRouteLeg l : linehaulRouteLegRepository.findAllWithDetailsDistinctByType(type)) {
            list.add(l);
        }

        return list;
    }

    public List<LinehaulRouteLeg> findAllWithDetailsByFromId(Long fromId) {

        List<LinehaulRouteLeg> list = new ArrayList<>();

        for (LinehaulRouteLeg l : linehaulRouteLegRepository.findAllWithDetailsDistinctByFromId(fromId)) {
            list.add(l);
        }

        return list;
    }

    public List<LinehaulRouteLeg> findAllBetweenLegStops(Long fromId, Long toId) {

        List<LinehaulRouteLeg> list = new ArrayList<>();
        Iterable<LinehaulRouteLeg> result = linehaulRouteLegRepository.findAllWithDetailsDistinctByFromIdAndToId(fromId, toId);
        for (LinehaulRouteLeg l : result) {
            list.add(l);
        }

        return list;
    }

    public List<RouteResponse.RouteLegResponse> findAllBetweenLegStopsAsLocations(Long fromId, Long toId) {

        List<RouteResponse.RouteLegResponse> list = new ArrayList<>();
        Iterable<LinehaulRouteLeg> result = linehaulRouteLegRepository.findAllWithDetailsDistinctByFromIdAndToId(fromId, toId);
        for (LinehaulRouteLeg leg : result) {
            RouteResponse.RouteLegResponse legResponse = new RouteResponse.RouteLegResponse();
            legResponse.setId(leg.getId());
            legResponse.setRouteLegType(leg.getType());

            if(leg.getFrom() != null) {
                leg.getFrom().getTypes().forEach(type -> {
                    legResponse.getFromLocations().addAll(findLocationsOfRouteLegStop(leg.getFrom().getId(), type));
                });
            }

            if(leg.getTo() != null) {
                leg.getTo().getTypes().forEach(type -> {
                    legResponse.getToLocations().addAll(findLocationsOfRouteLegStop(leg.getTo().getId(), type));
                });
            }

            List<RouteLegExpedition> expeditions = routeLegExpeditionService.findRouteLegExpeditionStartingFromNow(leg.getId(),
                    leg.getFrom().getTimezone());
            legResponse.setExpeditions(RouteLegExpeditionResponse.withCollection(expeditions));

            list.add(legResponse);
        }

        return list;
    }

    public List<RouteResponse.RouteLegStopResponse> findLocationsOfRouteLegStop(Long routeLegStopId, LocationType locationType) {

        if(locationType == LocationType.CROSSDOCK_WAREHOUSE || locationType == LocationType.CUSTOMER_WAREHOUSE) {
            return warehouseRepository.findByRouteLegStopId(routeLegStopId).stream().map(warehouse -> {
                return RouteResponse.RouteLegStopResponse.with(warehouse);
            }).collect(Collectors.toList());
        } else if(locationType == LocationType.PORT) {
            return portRepository.findByRouteLegStopId(routeLegStopId).stream().map(port -> {
                return RouteResponse.RouteLegStopResponse.with(port);
            }).collect(Collectors.toList());
        } else if(locationType == LocationType.TRAIN_TERMINAL) {
            return terminalRepository.findByRouteLegStopId(routeLegStopId).stream().map(terminal -> {
                return RouteResponse.RouteLegStopResponse.with(terminal);
            }).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }

    }

    @Transactional
    public LinehaulRouteLeg createOrUpdate(LinehaulRouteLeg routeLeg) {

        if (routeLeg == null) {
            throw new BadRequestException("LinehaulRouteLeg cannot be null.");
        }

        if (routeLeg.getId() != null) {
            findByIdOrThrowResourceNotFoundException(routeLeg.getId());
        }

        if (routeLeg.getType() == null) {
            throw new BadRequestException("LinehaulRouteLeg.type cannot be null.");
        }

        if (routeLeg.getFrom() == null || routeLeg.getFrom().getId() == null) {
            throw new BadRequestException("LinehaulRouteLeg.from.id cannot be null.");
        }

        LinehaulRouteLegStop from = linehaulRouteLegStopService.findByIdOrThrowResourceNotFoundException(routeLeg.getFrom().getId());
        routeLeg.setFrom(from);

        if (routeLeg.getTo() == null || routeLeg.getTo().getId() == null) {
            throw new BadRequestException("LinehaulRouteLeg.to.id cannot be null.");
        }

        LinehaulRouteLegStop to = linehaulRouteLegStopService.findByIdOrThrowResourceNotFoundException(routeLeg.getTo().getId());
        routeLeg.setTo(to);

        if (from.getId().equals(to.getId())) {
            throw new BadRequestException("LinehaulRouteLeg.from cannot be same with LinehaulRouteLeg.to.");
        }

        LinehaulRouteLeg routeLegByTypeAndFromIdAndToId =
                linehaulRouteLegRepository.findByTypeAndFromIdAndToId(routeLeg.getType(), from.getId(), to.getId());

        if (routeLegByTypeAndFromIdAndToId != null) {
            if (routeLeg.getId() == null || !routeLeg.getId().equals(routeLegByTypeAndFromIdAndToId.getId())) {
                throw new BadRequestException("There is already one LinehaulRouteLeg with same type, from and to: "
                        + routeLeg.getType() + ", " + from.getId() + ", " + to.getId());
            }
        }

        if (routeLeg.getType().equals(RouteLegType.RAILWAY)) {

            if (!from.getTypes().contains(LocationType.TRAIN_TERMINAL)) {
                throw new BadRequestException("LinehaulRouteLeg.from is not a " + LocationType.TRAIN_TERMINAL + ".");
            }
            if (!to.getTypes().contains(LocationType.TRAIN_TERMINAL)) {
                throw new BadRequestException("LinehaulRouteLeg.to is not a " + LocationType.TRAIN_TERMINAL + ".");
            }

        } else if (routeLeg.getType().equals(RouteLegType.SEAWAY)) {

            if (!from.getTypes().contains(LocationType.PORT)) {
                throw new BadRequestException("LinehaulRouteLeg.from is not a " + LocationType.PORT + ".");
            }
            if (!to.getTypes().contains(LocationType.PORT)) {
                throw new BadRequestException("LinehaulRouteLeg.to is not a " + LocationType.PORT + ".");
            }

        } else {
            // TODO: Bununla ilgili başka kural var mı?
        }

        Set<LinehaulRouteLegSchedule> schedules = routeLeg.getSchedules();
        Set<LinehaulRouteLegNonSchedule> nonSchedules = routeLeg.getNonSchedules();

        routeLeg.setSchedules(null);
        routeLeg.setNonSchedules(null);

        LinehaulRouteLeg persistedRouteLeg = linehaulRouteLegRepository.save(routeLeg);

        processSchedules(persistedRouteLeg, schedules);
        processNonSchedules(persistedRouteLeg, nonSchedules);

        routeLegExpeditionService.generateExpeditionsOfRouteLegForTwoWeeks(persistedRouteLeg.getId());

        return persistedRouteLeg;
    }

    @Transactional
    public void processSchedules(LinehaulRouteLeg routeLeg, Set<LinehaulRouteLegSchedule> schedules) {

        if (routeLeg == null || routeLeg.getId() == null) {
            throw new BadRequestException("A linehaul route leg must be specified.");
        }

        routeLeg = findByIdOrThrowResourceNotFoundException(routeLeg.getId());

        Map<Long, LinehaulRouteLegSchedule> existingRecordsMap = new HashMap<>();

        for (LinehaulRouteLegSchedule elem : linehaulRouteLegScheduleRepository.findAllByParentId(routeLeg.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<LinehaulRouteLegSchedule> recordsToBeCreated = new HashSet<>();
        Set<LinehaulRouteLegSchedule> recordsToBeUpdated = new HashSet<>();
        Set<LinehaulRouteLegSchedule> recordsToBeDeleted = new HashSet<>();

        if (schedules != null) {
            for (LinehaulRouteLegSchedule elem : schedules) {
                if (elem == null) {
                    throw new BadRequestException("A linehaul route leg schedule must be specified.");
                } else {
                    if (elem.getId() == null) {
                        recordsToBeCreated.add(elem);
                    } else {
                        recordsToBeUpdated.add(elem);
                        existingRecordsMap.remove(elem.getId());
                    }
                }
            }
        }

        recordsToBeDeleted.addAll(existingRecordsMap.values());

        Set<LinehaulRouteLegSchedule> finalRecords = new HashSet<>();

        for (LinehaulRouteLegSchedule elem : recordsToBeDeleted) {
            linehaulRouteLegScheduleService.softDelete(elem.getId());
        }

        for (LinehaulRouteLegSchedule elem : recordsToBeUpdated) {
            elem.setParent(routeLeg);
            finalRecords.add(linehaulRouteLegScheduleService.createOrUpdate(elem));
        }

        for (LinehaulRouteLegSchedule elem : recordsToBeCreated) {
            elem.setParent(routeLeg);
            finalRecords.add(linehaulRouteLegScheduleService.createOrUpdate(elem));
        }

        routeLeg.setSchedules(finalRecords);
    }

    @Transactional
    public void processNonSchedules(LinehaulRouteLeg routeLeg, Set<LinehaulRouteLegNonSchedule> nonSchedules) {

        if (routeLeg == null || routeLeg.getId() == null) {
            throw new BadRequestException("A linehaul route leg must be specified.");
        }

        routeLeg = findByIdOrThrowResourceNotFoundException(routeLeg.getId());

        Map<Long, LinehaulRouteLegNonSchedule> existingRecordsMap = new HashMap<>();

        for (LinehaulRouteLegNonSchedule elem : linehaulRouteLegNonScheduleRepository.findAllByParentId(routeLeg.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<LinehaulRouteLegNonSchedule> recordsToBeCreated = new HashSet<>();
        Set<LinehaulRouteLegNonSchedule> recordsToBeUpdated = new HashSet<>();
        Set<LinehaulRouteLegNonSchedule> recordsToBeDeleted = new HashSet<>();

        if (nonSchedules != null) {
            for (LinehaulRouteLegNonSchedule elem : nonSchedules) {
                if (elem == null) {
                    throw new BadRequestException("A linehaul route leg non-schedule must be specified.");
                } else {
                    if (elem.getId() == null) {
                        recordsToBeCreated.add(elem);
                    } else {
                        recordsToBeUpdated.add(elem);
                        existingRecordsMap.remove(elem.getId());
                    }
                }
            }
        }

        recordsToBeDeleted.addAll(existingRecordsMap.values());

        Set<LinehaulRouteLegNonSchedule> finalRecords = new HashSet<>();

        for (LinehaulRouteLegNonSchedule elem : recordsToBeDeleted) {
            linehaulRouteLegNonScheduleService.softDelete(elem.getId());
        }

        for (LinehaulRouteLegNonSchedule elem : recordsToBeUpdated) {
            elem.setParent(routeLeg);
            finalRecords.add(linehaulRouteLegNonScheduleService.createOrUpdate(elem));
        }

        for (LinehaulRouteLegNonSchedule elem : recordsToBeCreated) {
            elem.setParent(routeLeg);
            finalRecords.add(linehaulRouteLegNonScheduleService.createOrUpdate(elem));
        }

        routeLeg.setNonSchedules(finalRecords);
    }

    @Transactional
    public void softDelete(Long id) {

        LinehaulRouteLeg persistedEntity = findByIdOrThrowResourceNotFoundException(id);

        if (persistedEntity.getSchedules() != null) {
            for (LinehaulRouteLegSchedule elem : persistedEntity.getSchedules()) {
                linehaulRouteLegScheduleService.softDelete(elem.getId());
            }
        }

        if (persistedEntity.getNonSchedules() != null) {
            for (LinehaulRouteLegNonSchedule elem : persistedEntity.getNonSchedules()) {
                linehaulRouteLegNonScheduleService.softDelete(elem.getId());
            }
        }

        persistedEntity.setDeleted(true);
        linehaulRouteLegRepository.save(persistedEntity);
    }
}
