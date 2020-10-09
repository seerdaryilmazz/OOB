package ekol.location.service;

import ekol.exceptions.ApplicationException;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import ekol.location.domain.*;
import ekol.location.domain.dto.RouteLegExpeditionResponse;
import ekol.location.domain.dto.RouteResponse;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.repository.LinehaulRouteFragmentRepository;
import ekol.location.repository.LinehaulRouteRepository;
import ekol.location.repository.WarehouseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LinehaulRouteService {

    @Autowired
    private LinehaulRouteRepository linehaulRouteRepository;

    @Autowired
    private LinehaulRouteFragmentRepository linehaulRouteFragmentRepository;

    @Autowired
    private LinehaulRouteFragmentService linehaulRouteFragmentService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private LinehaulRouteLegService linehaulRouteLegService;

    @Autowired
    private RouteLegExpeditionService routeLegExpeditionService;

    private void sortFragments(LinehaulRoute route) {
        if(route.getFragments() == null){
            route.setFragments(new LinkedHashSet<>());
        }else{
            List<LinehaulRouteFragment> list = new ArrayList<>(route.getFragments());
            Collections.sort(list, LinehaulRouteFragment.FRAGMENT_COMPARATOR);
            route.setFragments(new LinkedHashSet<>(list));
        }
    }

    public LinehaulRoute findByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRoute persistedEntity = linehaulRouteRepository.findOne(id);

        if (persistedEntity != null) {
            sortFragments(persistedEntity);
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRoute with specified id cannot be found: " + id);
        }
    }

    public LinehaulRoute findWithDetailsByIdOrThrowResourceNotFoundException(Long id) {

        LinehaulRoute persistedEntity = linehaulRouteRepository.findWithDetailsById(id);

        if (persistedEntity != null) {
            sortFragments(persistedEntity);
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("LinehaulRoute with specified id cannot be found: " + id);
        }
    }

    // TODO: linehaulRouteRepository.findAllWithDetailsDistinctByFromIdOrderByFragmentsOrderNo metodu kayıtları sıralı getirmedi.
    // Gözden kaçan bir nokta olabilir, tekrar kontrol et. Onu düzeltene kadar şimdilik bu metodu kullan.
    public List<LinehaulRoute> findAllWithDetailsByFromId(Long fromId, Long toId) {

        List<LinehaulRoute> list = new ArrayList<>();

        Iterable<LinehaulRoute> iterable;

        if (fromId != null && toId != null) {
            iterable = linehaulRouteRepository.findAllWithDetailsDistinctByFromIdAndToIdOrderByNameAsc(fromId, toId);
        }else if(fromId != null){
            iterable = linehaulRouteRepository.findAllWithDetailsDistinctByFromIdOrderByNameAsc(fromId);
        } else {
            iterable = linehaulRouteRepository.findAllWithDetailsDistinctByOrderByNameAsc();
        }

        for (LinehaulRoute route : iterable) {
            sortFragments(route);
            list.add(route);
        }

        return list;
    }

    public List<LinehaulRoute> findBetweenWarehousesWithDetails(Long fromWarehouseId, Long toWarehouseId) {
        Warehouse fromWarehouse = warehouseRepository.findOne(fromWarehouseId);
        if(fromWarehouse == null){
            throw new ValidationException("There is no warehouse with id {0}", fromWarehouseId);
        }
        Warehouse toWarehouse = warehouseRepository.findOne(toWarehouseId);
        if(toWarehouse == null){
            throw new ValidationException("There is no warehouse with id {0}", toWarehouseId);
        }

        return findAllWithDetailsByFromId(fromWarehouse.getRouteLegStop().getId(), toWarehouse.getRouteLegStop().getId());
    }

    public List<RouteResponse> findBetweenWarehousesWithDetailsAsLocations(Long fromWarehouseId, Long toWarehouseId) {
        Warehouse fromWarehouse = warehouseRepository.findOne(fromWarehouseId);
        if (fromWarehouse == null) {
            throw new ValidationException("There is no warehouse with id {0}", fromWarehouseId);
        }
        Warehouse toWarehouse = warehouseRepository.findOne(toWarehouseId);
        if (toWarehouse == null) {
            throw new ValidationException("There is no warehouse with id {0}", toWarehouseId);
        }

        List<LinehaulRoute> routes = findAllWithDetailsByFromId(fromWarehouse.getRouteLegStop().getId(), toWarehouse.getRouteLegStop().getId());

        List<RouteResponse> result = new ArrayList<>();

        routes.forEach(route -> {
            RouteResponse routeResponse = new RouteResponse();
            routeResponse.setId(route.getId());
            routeResponse.setName(route.getName());

            route.getRouteLegs().forEach(routeLeg -> {
                RouteResponse.RouteLegResponse legResponse = new RouteResponse.RouteLegResponse();
                legResponse.setId(routeLeg.getId());
                legResponse.setRouteLegType(routeLeg.getType());

                if (routeLeg.getFrom() != null) {
                    routeLeg.getFrom().getTypes().forEach(type -> {
                        legResponse.getFromLocations().addAll(
                                linehaulRouteLegService.findLocationsOfRouteLegStop(routeLeg.getFrom().getId(), type));
                    });
                }

                if (routeLeg.getTo() != null) {
                    routeLeg.getTo().getTypes().forEach(type -> {
                        legResponse.getToLocations().addAll(
                                linehaulRouteLegService.findLocationsOfRouteLegStop(routeLeg.getTo().getId(), type));
                    });
                }

                List<RouteLegExpedition> expeditions = routeLegExpeditionService.findRouteLegExpeditionStartingFromNow(routeLeg.getId(),
                        routeLeg.getFrom().getTimezone());
                legResponse.setExpeditions(RouteLegExpeditionResponse.withCollection(expeditions));
                routeResponse.getRouteLegs().add(legResponse);
            });
            result.add(routeResponse);
        });


        return result;
    }

    @Transactional
    public LinehaulRoute createOrUpdate(LinehaulRoute route) {

        if (route == null) {
            throw new BadRequestException("LinehaulRoute cannot be null.");
        }

        if (StringUtils.isBlank(route.getName())) {
            throw new BadRequestException("LinehaulRoute.name must contain at least 1 char.");
        }

        route.setName(route.getName().trim());

        LinehaulRoute routeByName = linehaulRouteRepository.findByName(route.getName());

        if (routeByName != null) {
            if (route.getId() == null || !route.getId().equals(routeByName.getId())) {
                throw new BadRequestException("LinehaulRoute.name is in use.");
            }
        }

        Set<LinehaulRouteFragment> fragments = route.getFragments();
        route.setFragments(null);

        LinehaulRoute persistedRoute = linehaulRouteRepository.save(route);

        processFragments(persistedRoute, fragments);

        LinehaulRouteLegStop fromOfFirstFragment = getFromOfFirstFragment(persistedRoute);
        LinehaulRouteLegStop toOfLastFragment = getToOfLastFragment(persistedRoute);

        persistedRoute.setFrom(fromOfFirstFragment);
        persistedRoute.setTo(toOfLastFragment);

        // TODO: Burada update yapmamak için processFragments metodunun mantığını değiştirmek gerekiyor.
        persistedRoute = linehaulRouteRepository.save(persistedRoute);

        return persistedRoute;
    }

    @Transactional
    public void processFragments(LinehaulRoute route, Set<LinehaulRouteFragment> fragments) {

        if (route == null || route.getId() == null) {
            throw new BadRequestException("LinehaulRoute.id cannot be null.");
        }

        route = findByIdOrThrowResourceNotFoundException(route.getId());

        Map<Long, LinehaulRouteFragment> existingRecordsMap = new HashMap<>();

        for (LinehaulRouteFragment f : linehaulRouteFragmentRepository.findAllByParentId(route.getId())) {
            existingRecordsMap.put(f.getId(), f);
        }

        Set<LinehaulRouteFragment> recordsToBeCreated = new HashSet<>();
        Set<LinehaulRouteFragment> recordsToBeUpdated = new HashSet<>();
        Set<LinehaulRouteFragment> recordsToBeDeleted = new HashSet<>();

        if (fragments != null) {
            for (LinehaulRouteFragment f : fragments) {
                if (f == null) {
                    throw new BadRequestException("LinehaulRouteFragment cannot be null.");
                } else {
                    if (f.getOrderNo() == null) {
                        throw new BadRequestException("LinehaulRouteFragment.orderNo cannot be null.");
                    } else {
                        if (f.getId() == null) {
                            recordsToBeCreated.add(f);
                        } else {
                            recordsToBeUpdated.add(f);
                            existingRecordsMap.remove(f.getId());
                        }
                    }
                }
            }
        }

        recordsToBeDeleted.addAll(existingRecordsMap.values());

        if (recordsToBeUpdated.size() + recordsToBeCreated.size() == 0) {
            throw new BadRequestException("LinehaulRoute must have at least one LinehaulRouteFragment.");
        }

        SortedSet<Integer> sortedOrderNos = new TreeSet<>();

        for (LinehaulRouteFragment f : recordsToBeUpdated) {
            sortedOrderNos.add(f.getOrderNo());
        }

        for (LinehaulRouteFragment f : recordsToBeCreated) {
            sortedOrderNos.add(f.getOrderNo());
        }

        if (sortedOrderNos.first() != 1) {
            throw new BadRequestException("Minimum orderNo must be 1.");
        }

        if (sortedOrderNos.last() != (recordsToBeUpdated.size() + recordsToBeCreated.size())) {
            throw new BadRequestException("Maximum orderNo must be " + (recordsToBeUpdated.size() + recordsToBeCreated.size()) + ".");
        }

        Set<LinehaulRouteFragment> finalRecords = new HashSet<>();

        for (LinehaulRouteFragment f : recordsToBeDeleted) {
            linehaulRouteFragmentService.softDelete(f.getId());
        }

        for (LinehaulRouteFragment f : recordsToBeUpdated) {
            f.setParent(route);
            finalRecords.add(linehaulRouteFragmentService.createOrUpdate(f));
        }

        for (LinehaulRouteFragment f : recordsToBeCreated) {
            f.setParent(route);
            finalRecords.add(linehaulRouteFragmentService.createOrUpdate(f));
        }

        route.setFragments(finalRecords);
    }

    @Transactional
    public void softDelete(Long id) {

        LinehaulRoute persistedEntity = findByIdOrThrowResourceNotFoundException(id);

        if (persistedEntity.getFragments() != null) {
            for (LinehaulRouteFragment f : persistedEntity.getFragments()) {
                linehaulRouteFragmentService.softDelete(f.getId());
            }
        }

        persistedEntity.setDeleted(true);
        linehaulRouteRepository.save(persistedEntity);
    }

    public LinehaulRouteLegStop getFromOfFirstFragment(LinehaulRoute route) {

        LinehaulRouteLegStop from = null;
        LinehaulRouteFragment firstFragment = null;

        for (LinehaulRouteFragment f : route.getFragments()) {
            if (f.getOrderNo() == 1) {
                firstFragment = f;
                break;
            }
        }

        if (firstFragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {
            from = firstFragment.getLeg().getFrom();
        } else if (firstFragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
            from = firstFragment.getRoute().getFrom();
        } else {
            throw new ApplicationException("No implementation for " + firstFragment.getType());
        }

        return from;
    }

    public LinehaulRouteLegStop getToOfLastFragment(LinehaulRoute route) {

        LinehaulRouteLegStop to = null;
        LinehaulRouteFragment lastFragment = null;

        for (LinehaulRouteFragment f : route.getFragments()) {
            if (f.getOrderNo() == route.getFragments().size()) {
                lastFragment = f;
                break;
            }
        }

        if (lastFragment.getType().equals(LinehaulRouteFragmentType.ONE_LEGGED)) {
            to = lastFragment.getLeg().getTo();
        } else if (lastFragment.getType().equals(LinehaulRouteFragmentType.MULTIPLE_LEGGED)) {
            to = lastFragment.getRoute().getTo();
        } else {
            throw new ApplicationException("No implementation for " + lastFragment.getType());
        }

        return to;
    }

    /**
     * LinehaulRoute'ta fromLocation ve toLocation alanlarını tutmadığımız durumda kullanabileceğimiz bir yöntem.
     */
//    // TODO: Bu metodu ve bu metodun çağırdığı diğer metodu daha az veritabanı sorgusu yapar hale nasıl getirebiliriz?
//    public List<LinehaulRoute> findRoutesThatStartWithGivenLocation(Long locationId) {
//
//        List<LinehaulRoute> matchingRoutes = new ArrayList<>();
//
//        List<LinehaulRouteLeg> legsStartingFromLocation = linehaulRouteLegRepository.findByFromLocationId(locationId);
//
//        for (LinehaulRouteLeg leg : legsStartingFromLocation) {
//
//            Iterable<LinehaulRouteFragment> fragments = linehaulRouteFragmentRepository.findAllByOrderNoAndLegId(1, leg.getId());
//
//            for (LinehaulRouteFragment fragment : fragments) {
//                matchingRoutes.add(fragment.getRoute());
//            }
//        }
//
//        List<LinehaulRoute> matchingRoutesTemp = matchingRoutes;
//
//        while (matchingRoutesTemp.size() > 0) {
//            List<LinehaulRoute> resultList = findRoutesThatStartWithGivenRoutes(matchingRoutesTemp);
//            matchingRoutes.addAll(resultList);
//            matchingRoutesTemp = resultList;
//        }
//
//        return matchingRoutes;
//    }
//
//    private List<LinehaulRoute> findRoutesThatStartWithGivenRoutes(List<LinehaulRoute> inputList) {
//
//        List<LinehaulRoute> matchingRoutes = new ArrayList<>();
//
//        for (LinehaulRoute route : inputList) {
//
//            Iterable<LinehaulRouteFragment> fragments = linehaulRouteFragmentRepository.findAllByOrderNoAndRouteId(1, route.getId());
//
//            for (LinehaulRouteFragment fragment : fragments) {
//                matchingRoutes.add(fragment.getRoute());
//            }
//        }
//
//        return matchingRoutes;
//    }
}
