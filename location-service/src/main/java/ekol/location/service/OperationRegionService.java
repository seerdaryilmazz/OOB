package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.*;
import ekol.location.domain.dto.DataForRegionMap;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.repository.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OperationRegionService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OperationRegionRepository operationRegionRepository;

    @Autowired
    private OperationRegionToPolygonRegionService operationRegionToPolygonRegionService;

    @Autowired
    private OperationRegionToPolygonRegionRepository operationRegionToPolygonRegionRepository;

    @Autowired
    private PolygonRegionRepository polygonRegionRepository;

    @Autowired
    private PolygonRegionService polygonRegionService;

    @Autowired
    private OperationRegionTagService operationRegionTagService;

    @Autowired
    private OperationRegionTagRepository operationRegionTagRepository;

    @Autowired
    private CollectionRegionRepository collectionRegionRepository;

    @Autowired
    private CollectionRegionService collectionRegionService;

    @Autowired
    private DistributionRegionRepository distributionRegionRepository;

    @Autowired
    private DistributionRegionService distributionRegionService;

    @Autowired
    private WarehouseService warehouseService;

    public OperationRegion findByIdOrThrowResourceNotFoundException(Long id) {

        OperationRegion persistedEntity = operationRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Operation region with specified id cannot be found: " + id);
        }
    }

    public OperationRegion findAccordingToQueryOneByIdOrThrowResourceNotFoundException(Long id) {

        OperationRegion persistedEntity = operationRegionRepository.findAccordingToQueryOneById(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Operation region with specified id cannot be found: " + id);
        }
    }

    public OperationRegion findAccordingToQueryFiveByIdOrThrowResourceNotFoundException(Long id) {

        OperationRegion persistedEntity = operationRegionRepository.findAccordingToQueryFiveById(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Operation region with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public OperationRegion createOrUpdate(OperationRegion operationRegion) {

        if (operationRegion == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        if (operationRegion.getId() != null) {
            findByIdOrThrowResourceNotFoundException(operationRegion.getId());
        }

        if (StringUtils.isBlank(operationRegion.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        operationRegion.setName(operationRegion.getName().trim());

        OperationRegion operationRegionByName = operationRegionRepository.findByName(operationRegion.getName());

        if (operationRegionByName != null) {
            if (operationRegion.getId() == null || !operationRegion.getId().equals(operationRegionByName.getId())) {
                throw new BadRequestException("The specified name is used by another operation region.");
            }
        }

        Set<OperationRegionTag> tags = operationRegion.getTags();
        Set<OperationRegionToPolygonRegion> operationRegionToPolygonRegions = operationRegion.getOperationRegionToPolygonRegions();
        Set<CollectionRegion> collectionRegions = operationRegion.getCollectionRegions();
        Set<DistributionRegion> distributionRegions = operationRegion.getDistributionRegions();

        operationRegion.setTags(null);
        operationRegion.setOperationRegionToPolygonRegions(null);
        operationRegion.setCollectionRegions(null);
        operationRegion.setDistributionRegions(null);

        OperationRegion persistedOperationRegion = operationRegionRepository.save(operationRegion);

        processTags(persistedOperationRegion, tags);
        processOperationRegionToPolygonRegions(persistedOperationRegion, operationRegionToPolygonRegions);
        processCollectionRegions(persistedOperationRegion, collectionRegions);
        processDistributionRegions(persistedOperationRegion, distributionRegions);

        // TODO: Bu kontrolü burada yapmak doğru mu?

        List<PolygonRegion> polygonRegions = null;
        List<Long> idsOfLowestLevelPolygonRegionsOfOperationRegion = null;
        List<Long> idsOfLowestLevelPolygonRegionsOfCollectionRegions = null;
        List<Long> idsOfLowestLevelPolygonRegionsOfDistributionRegions = null;

        polygonRegions = new ArrayList<>();
        for (OperationRegionToPolygonRegion elem : persistedOperationRegion.getOperationRegionToPolygonRegions()) {
            polygonRegions.add(elem.getPolygonRegion());
        }
        idsOfLowestLevelPolygonRegionsOfOperationRegion = findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegions(polygonRegions);

        polygonRegions = new ArrayList<>();
        for (CollectionRegion collectionRegion : persistedOperationRegion.getCollectionRegions()) {
            for (CollectionRegionToPolygonRegion elem : collectionRegion.getCollectionRegionToPolygonRegions()) {
                polygonRegions.add(elem.getPolygonRegion());
            }
        }
        idsOfLowestLevelPolygonRegionsOfCollectionRegions = findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegions(polygonRegions);

        polygonRegions = new ArrayList<>();
        for (DistributionRegion distributionRegion : persistedOperationRegion.getDistributionRegions()) {
            for (DistributionRegionToPolygonRegion elem : distributionRegion.getDistributionRegionToPolygonRegions()) {
                polygonRegions.add(elem.getPolygonRegion());
            }
        }
        idsOfLowestLevelPolygonRegionsOfDistributionRegions = findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegions(polygonRegions);

        if (!CollectionUtils.isEqualCollection(idsOfLowestLevelPolygonRegionsOfOperationRegion, idsOfLowestLevelPolygonRegionsOfCollectionRegions)) {
            // TODO: Hangilerinin eksik olduğunu nasıl ifade edelim?
            throw new BadRequestException("Polygon regions of operation region do not include all of the polygon regions of collection regions or vice versa.");
        }

        if (!CollectionUtils.isEqualCollection(idsOfLowestLevelPolygonRegionsOfOperationRegion, idsOfLowestLevelPolygonRegionsOfDistributionRegions)) {
            // TODO: Hangilerinin eksik olduğunu nasıl ifade edelim?
            throw new BadRequestException("Polygon regions of operation region do not include all of the polygon regions of distribution regions or vice versa.");
        }

        return persistedOperationRegion;
    }

    @Transactional
    public void processTags(OperationRegion operationRegion, Set<OperationRegionTag> tags) {

        if (operationRegion == null || operationRegion.getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        operationRegion = findByIdOrThrowResourceNotFoundException(operationRegion.getId());

        Map<Long, OperationRegionTag> existingRecordsMap = new HashMap<>();

        for (OperationRegionTag elem : operationRegionTagRepository.findAllByOperationRegionId(operationRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<OperationRegionTag> recordsToBeCreated = new HashSet<>();
        Set<OperationRegionTag> recordsToBeUpdated = new HashSet<>();
        Set<OperationRegionTag> recordsToBeDeleted = new HashSet<>();

        if (tags != null) {
            for (OperationRegionTag elem : tags) {
                if (elem == null) {
                    throw new BadRequestException("A tag must be specified.");
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

        Set<OperationRegionTag> finalRecords = new HashSet<>();

        for (OperationRegionTag elem : recordsToBeDeleted) {
            operationRegionTagService.softDelete(elem.getId());
        }

        for (OperationRegionTag elem : recordsToBeUpdated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(operationRegionTagService.createOrUpdate(elem));
        }

        for (OperationRegionTag elem : recordsToBeCreated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(operationRegionTagService.createOrUpdate(elem));
        }

        operationRegion.setTags(finalRecords);
    }

    @Transactional
    public void processOperationRegionToPolygonRegions(OperationRegion operationRegion, Set<OperationRegionToPolygonRegion> operationRegionToPolygonRegions) {

        if (operationRegion == null || operationRegion.getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        operationRegion = findByIdOrThrowResourceNotFoundException(operationRegion.getId());

        Map<Long, OperationRegionToPolygonRegion> existingRecordsMap = new HashMap<>();

        for (OperationRegionToPolygonRegion elem : operationRegionToPolygonRegionRepository.findAllByOperationRegionId(operationRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<OperationRegionToPolygonRegion> recordsToBeCreated = new HashSet<>();
        Set<OperationRegionToPolygonRegion> recordsToBeUpdated = new HashSet<>();
        Set<OperationRegionToPolygonRegion> recordsToBeDeleted = new HashSet<>();

        if (operationRegionToPolygonRegions != null) {
            for (OperationRegionToPolygonRegion elem : operationRegionToPolygonRegions) {
                if (elem == null) {
                    throw new BadRequestException("An operation region to polygon region must be specified.");
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

        if (recordsToBeCreated.size() + recordsToBeUpdated.size() == 0) {
            throw new BadRequestException("There must be at least one operation region to polygon region.");
        }

        Set<OperationRegionToPolygonRegion> finalRecords = new HashSet<>();

        for (OperationRegionToPolygonRegion elem : recordsToBeDeleted) {
            operationRegionToPolygonRegionService.softDelete(elem.getId());
        }

        for (OperationRegionToPolygonRegion elem : recordsToBeUpdated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(operationRegionToPolygonRegionService.createOrUpdate(elem));
        }

        for (OperationRegionToPolygonRegion elem : recordsToBeCreated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(operationRegionToPolygonRegionService.createOrUpdate(elem));
        }

        operationRegion.setOperationRegionToPolygonRegions(finalRecords);
    }

    @Transactional
    public void processCollectionRegions(OperationRegion operationRegion, Set<CollectionRegion> collectionRegions) {

        if (operationRegion == null || operationRegion.getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        operationRegion = findByIdOrThrowResourceNotFoundException(operationRegion.getId());

        Map<Long, CollectionRegion> existingRecordsMap = new HashMap<>();

        for (CollectionRegion elem : collectionRegionRepository.findAllByOperationRegionId(operationRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<CollectionRegion> recordsToBeCreated = new HashSet<>();
        Set<CollectionRegion> recordsToBeUpdated = new HashSet<>();
        Set<CollectionRegion> recordsToBeDeleted = new HashSet<>();

        if (collectionRegions != null) {
            for (CollectionRegion elem : collectionRegions) {
                if (elem == null) {
                    throw new BadRequestException("A collection region must be specified.");
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

        if (recordsToBeCreated.size() + recordsToBeUpdated.size() == 0) {
            throw new BadRequestException("There must be at least one collection region.");
        }

        Set<CollectionRegion> finalRecords = new HashSet<>();

        for (CollectionRegion elem : recordsToBeDeleted) {
            collectionRegionService.softDelete(elem.getId());
        }

        for (CollectionRegion elem : recordsToBeUpdated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(collectionRegionService.createOrUpdate(elem));
        }

        for (CollectionRegion elem : recordsToBeCreated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(collectionRegionService.createOrUpdate(elem));
        }

        operationRegion.setCollectionRegions(finalRecords);
    }

    @Transactional
    public void processDistributionRegions(OperationRegion operationRegion, Set<DistributionRegion> distributionRegions) {

        if (operationRegion == null || operationRegion.getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        operationRegion = findByIdOrThrowResourceNotFoundException(operationRegion.getId());

        Map<Long, DistributionRegion> existingRecordsMap = new HashMap<>();

        for (DistributionRegion elem : distributionRegionRepository.findAllByOperationRegionId(operationRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<DistributionRegion> recordsToBeCreated = new HashSet<>();
        Set<DistributionRegion> recordsToBeUpdated = new HashSet<>();
        Set<DistributionRegion> recordsToBeDeleted = new HashSet<>();

        if (distributionRegions != null) {
            for (DistributionRegion elem : distributionRegions) {
                if (elem == null) {
                    throw new BadRequestException("A distribution region must be specified.");
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

        if (recordsToBeCreated.size() + recordsToBeUpdated.size() == 0) {
            throw new BadRequestException("There must be at least one distribution region.");
        }

        Set<DistributionRegion> finalRecords = new HashSet<>();

        for (DistributionRegion elem : recordsToBeDeleted) {
            distributionRegionService.softDelete(elem.getId());
        }

        for (DistributionRegion elem : recordsToBeUpdated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(distributionRegionService.createOrUpdate(elem));
        }

        for (DistributionRegion elem : recordsToBeCreated) {
            elem.setOperationRegion(operationRegion);
            finalRecords.add(distributionRegionService.createOrUpdate(elem));
        }

        operationRegion.setDistributionRegions(finalRecords);
    }

    @Transactional
    public void softDelete(Long id) {

        OperationRegion persistedEntity = findByIdOrThrowResourceNotFoundException(id);

        if (persistedEntity.getTags() != null) {
            for (OperationRegionTag elem : persistedEntity.getTags()) {
                operationRegionTagService.softDelete(elem.getId());
            }
        }

        if (persistedEntity.getOperationRegionToPolygonRegions() != null) {
            for (OperationRegionToPolygonRegion elem : persistedEntity.getOperationRegionToPolygonRegions()) {
                operationRegionToPolygonRegionService.softDelete(elem.getId());
            }
        }

        if (persistedEntity.getCollectionRegions() != null) {
            for (CollectionRegion elem : persistedEntity.getCollectionRegions()) {
                collectionRegionService.softDelete(elem.getId());
            }
        }

        if (persistedEntity.getDistributionRegions() != null) {
            for (DistributionRegion elem : persistedEntity.getDistributionRegions()) {
                distributionRegionService.softDelete(elem.getId());
            }
        }

        persistedEntity.setDeleted(true);

        operationRegionRepository.save(persistedEntity);
    }

    /**
     * Verilen PolygonRegion'ı oluşturan en küçük PolygonRegion'ları bulan metodtur.
     * Örneğin; şuan kullandığımız verilere göre Türkiye'deki en alt birimler ilçeler olduğu için
     * parametre olarak Türkiye verildiğinde Türkiye'deki tüm ilçeler bulunacaktır.
     */
    private List<PolygonRegion> findLowestLevelPolygonRegionsThatComposeGivenPolygonRegion(PolygonRegion polygonRegion) {

        List<PolygonRegion> lowestLevelPolygonRegions = new ArrayList<>();

        List<PolygonRegion> polygonRegions = new ArrayList<>();
        polygonRegions.add(polygonRegion);

        int index = 0;

        while (index <= polygonRegions.size() - 1) {

            PolygonRegion pr = polygonRegions.get(index);
            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(pr);

            if (!pr.getHasChildren()) {
                lowestLevelPolygonRegions.add(pr);
            } else {
                polygonRegions.addAll(polygonRegionRepository.findAllByParent(absoluteName));
            }

            index++;
        }

        return lowestLevelPolygonRegions;
    }

    private List<Long> findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegion(PolygonRegion polygonRegion) {

        List<Long> idsOfLowestLevelPolygonRegions = new ArrayList<>();

        for (PolygonRegion pr : findLowestLevelPolygonRegionsThatComposeGivenPolygonRegion(polygonRegion)) {
            idsOfLowestLevelPolygonRegions.add(pr.getId());
        }

        return idsOfLowestLevelPolygonRegions;
    }

    private List<PolygonRegion> findLowestLevelPolygonRegionsThatComposeGivenPolygonRegions(List<PolygonRegion> polygonRegions) {

        List<PolygonRegion> lowestLevelPolygonRegions = new ArrayList<>();

        for (PolygonRegion pr : polygonRegions) {
            lowestLevelPolygonRegions.addAll(findLowestLevelPolygonRegionsThatComposeGivenPolygonRegion(pr));
        }

        return lowestLevelPolygonRegions;
    }

    private List<Long> findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegions(List<PolygonRegion> polygonRegions) {

        List<Long> idsOfLowestLevelPolygonRegions = new ArrayList<>();

        for (PolygonRegion pr : polygonRegions) {
            idsOfLowestLevelPolygonRegions.addAll(findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegion(pr));
        }

        return idsOfLowestLevelPolygonRegions;
    }

    /**
     * @param basePolygonRegions Bu alan OperationRegion için işlem yapılırken boş oluyor, CollectionRegion ve DistributionRegion için işlem yapılırken
     *                           ise OperationRegion'da eklenmiş kayıtları içeriyor.
     */
    private DataForRegionMap findDataForRegionMap(
            List<PolygonRegion> basePolygonRegions, List<PolygonRegion> polygonRegionsAddedByOtherRecords, List<PolygonRegion> polygonRegionsAddedBySelectedRecord) {

        Map<Long, PolygonRegion> mapOfPolygonRegionsToBeShown = new HashMap<>();
        Map<Long, PolygonRegion> mapOfPolygonRegionsNotToBeShown = new HashMap<>();
        Map<Long, PolygonRegion> mapOfPolygonRegionsThatCanBeAdded = new HashMap<>();
        Map<Long, PolygonRegion> mapOfPolygonRegionsThatCannotBeAdded = new HashMap<>();
        Map<Long, PolygonRegion> mapOfPolygonRegionsThatAreAdded = new HashMap<>();

        List<PolygonRegion> polygonRegions = new ArrayList<>();
        polygonRegions.addAll(polygonRegionsAddedByOtherRecords);
        polygonRegions.addAll(polygonRegionsAddedBySelectedRecord);

        if (basePolygonRegions.size() > 0) {

            Map<Long, PolygonRegion> mapOfBasePolygonRegions = new HashMap<>();

            for (PolygonRegion polygonRegion : basePolygonRegions) {
                mapOfBasePolygonRegions.put(polygonRegion.getId(), polygonRegion);
            }

            for (PolygonRegion polygonRegion : polygonRegions) {

                String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);

                for (PolygonRegion basePolygonRegion : new ArrayList<>(mapOfBasePolygonRegions.values())) {

                    String absoluteNameOfBasePolygonRegion = polygonRegionService.getPolygonRegionAbsoluteName(basePolygonRegion);

                    if (absoluteName.equals(absoluteNameOfBasePolygonRegion) || absoluteName.startsWith(absoluteNameOfBasePolygonRegion + "/")
                            || absoluteNameOfBasePolygonRegion.startsWith(absoluteName + "/")) {

                        mapOfBasePolygonRegions.remove(basePolygonRegion.getId());
                    }
                }
            }

            polygonRegions.addAll(mapOfBasePolygonRegions.values());
        }

        for (PolygonRegion polygonRegion : polygonRegions) {

            if (polygonRegion.getLevel() == 0) {
                mapOfPolygonRegionsToBeShown.put(polygonRegion.getId(), polygonRegion);
            } else {

                String absoluteNameOfPolygonRegion = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
                String[] names = StringUtils.split(absoluteNameOfPolygonRegion, "/");
                List<String> absoluteNames = new ArrayList<>();

                for (int j = 0; j < names.length; j++) {
                    if (j == 0) {
                        absoluteNames.add("/" + names[j]);
                    } else {
                        absoluteNames.add(absoluteNames.get(j - 1) + "/" + names[j]);
                    }
                }

                absoluteNames.remove(absoluteNames.size() - 1);

                for (String absoluteName : absoluteNames) {

                    for (PolygonRegion pr : mapOfPolygonRegionsToBeShown.values()) {
                        if (polygonRegionService.getPolygonRegionAbsoluteName(pr).equals(absoluteName)) {
                            mapOfPolygonRegionsNotToBeShown.put(pr.getId(), pr);
                            break;
                        }
                    }

                    for (PolygonRegion pr : polygonRegionRepository.findAllByParent(absoluteName)) {
                        mapOfPolygonRegionsToBeShown.put(pr.getId(), pr);
                    }
                }
            }
        }

        for (PolygonRegion polygonRegion : mapOfPolygonRegionsNotToBeShown.values()) {
            mapOfPolygonRegionsToBeShown.remove(polygonRegion.getId());
        }

        for (PolygonRegion polygonRegion : polygonRegionsAddedByOtherRecords) {
            mapOfPolygonRegionsThatCannotBeAdded.put(polygonRegion.getId(), polygonRegion);
        }

        for (PolygonRegion polygonRegion : polygonRegionsAddedBySelectedRecord) {
            mapOfPolygonRegionsThatAreAdded.put(polygonRegion.getId(), polygonRegion);
        }

        List<Long> idsOfLowestLevelPolygonRegionsThatComposeBasePolygonRegions = null;

        if (basePolygonRegions.size() > 0) {
            idsOfLowestLevelPolygonRegionsThatComposeBasePolygonRegions =
                    findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegions(basePolygonRegions);
        }

        for (PolygonRegion polygonRegion : mapOfPolygonRegionsToBeShown.values()) {

            if (!mapOfPolygonRegionsThatAreAdded.containsKey(polygonRegion.getId()) && !mapOfPolygonRegionsThatCannotBeAdded.containsKey(polygonRegion.getId())) {

                if (basePolygonRegions.size() == 0) {
                    mapOfPolygonRegionsThatCanBeAdded.put(polygonRegion.getId(), polygonRegion);
                } else {

                    List<Long> idsOfLowestLevelPolygonRegionsThatComposePolygonRegion = findIdsOfLowestLevelPolygonRegionsThatComposeGivenPolygonRegion(polygonRegion);

                    if (idsOfLowestLevelPolygonRegionsThatComposeBasePolygonRegions.containsAll(idsOfLowestLevelPolygonRegionsThatComposePolygonRegion)) {
                        mapOfPolygonRegionsThatCanBeAdded.put(polygonRegion.getId(), polygonRegion);
                    } else {
                        mapOfPolygonRegionsThatCannotBeAdded.put(polygonRegion.getId(), polygonRegion);
                    }
                }
            }
        }

        // Aşağısı debugging için
//        System.out.println();
//        System.out.println();
//
//        System.out.println("polygonRegionsToBeShown:");
//        sortAndPrint(mapOfPolygonRegionsToBeShown.values());
//        System.out.println();
//
//        System.out.println("that can be added:");
//        sortAndPrint(mapOfPolygonRegionsThatCanBeAdded.values());
//        System.out.println();
//
//        System.out.println("that cannot be added:");
//        sortAndPrint(mapOfPolygonRegionsThatCannotBeAdded.values());
//        System.out.println();
//
//        System.out.println("that are added:");
//        sortAndPrint(mapOfPolygonRegionsThatAreAdded.values());
//        System.out.println();
//
//        System.out.println();
//        System.out.println();

        DataForRegionMap result = new DataForRegionMap();

        if (mapOfPolygonRegionsThatCanBeAdded.size() > 0) {
            result.getPolygonRegionsThatCanBeAdded().addAll(
                    polygonRegionRepository.findAllForDrawingDistinctByIdIn(new ArrayList<>(mapOfPolygonRegionsThatCanBeAdded.keySet())));
        }

        if (mapOfPolygonRegionsThatCannotBeAdded.size() > 0) {
            result.getPolygonRegionsThatCannotBeAdded().addAll(
                    polygonRegionRepository.findAllForDrawingDistinctByIdIn(new ArrayList<>(mapOfPolygonRegionsThatCannotBeAdded.keySet())));
        }

        if (mapOfPolygonRegionsThatAreAdded.size() > 0) {
            result.getPolygonRegionsThatAreAdded().addAll(
                    polygonRegionRepository.findAllForDrawingDistinctByIdIn(new ArrayList<>(mapOfPolygonRegionsThatAreAdded.keySet())));
        }

        return result;
    }

    private void sortAndPrint(Collection<PolygonRegion> polygonRegions) {

        List<PolygonRegion> list = new ArrayList<>(polygonRegions);

        Comparator<PolygonRegion> comparator = new Comparator<PolygonRegion>() {
            @Override
            public int compare(PolygonRegion pr1, PolygonRegion pr2) {
                return polygonRegionService.getPolygonRegionAbsoluteName(pr1).compareTo(polygonRegionService.getPolygonRegionAbsoluteName(pr2));
            }
        };

        Collections.sort(list, comparator);

        for (PolygonRegion pr : list) {
            System.out.println(pr.getId() + "\t\t" + polygonRegionService.getPolygonRegionAbsoluteName(pr));
        }
    }

    public DataForRegionMap findDataForOperationRegionMap(Long selectedOperationRegionId) {

        List<PolygonRegion> basePolygonRegions = Collections.EMPTY_LIST;
        List<PolygonRegion> polygonRegionsAddedByOtherRecords = new ArrayList<>();
        List<PolygonRegion> polygonRegionsAddedBySelectedRecord = new ArrayList<>();

        if (selectedOperationRegionId != null) {

            for (OperationRegion operationRegion : operationRegionRepository.findAllByIdNot(selectedOperationRegionId)) {
                for (OperationRegionToPolygonRegion elem : operationRegion.getOperationRegionToPolygonRegions()) {
                    polygonRegionsAddedByOtherRecords.add(elem.getPolygonRegion());
                }
            }

            for (OperationRegionToPolygonRegion elem : operationRegionRepository.findOne(selectedOperationRegionId).getOperationRegionToPolygonRegions()) {
                polygonRegionsAddedBySelectedRecord.add(elem.getPolygonRegion());
            }

        } else {
            for (OperationRegion operationRegion : operationRegionRepository.findAll()) {
                for (OperationRegionToPolygonRegion elem : operationRegion.getOperationRegionToPolygonRegions()) {
                    polygonRegionsAddedByOtherRecords.add(elem.getPolygonRegion());
                }
            }
        }

        return findDataForRegionMap(basePolygonRegions, polygonRegionsAddedByOtherRecords, polygonRegionsAddedBySelectedRecord);
    }

    public DataForRegionMap findDataForCollectionRegionMap(
            List<Long> polygonRegionIdsOfOperationRegion, List<Long> polygonRegionIdsOfOtherCollectionRegions, List<Long> polygonRegionIdsOfSelectedCollectionRegion) {

        List<PolygonRegion> polygonRegionsOfOperationRegion = new ArrayList<>(polygonRegionRepository.findAllByIdIn(polygonRegionIdsOfOperationRegion));
        List<PolygonRegion> polygonRegionsOfOtherCollectionRegions = new ArrayList<>(polygonRegionRepository.findAllByIdIn(polygonRegionIdsOfOtherCollectionRegions));
        List<PolygonRegion> polygonRegionsOfSelectedCollectionRegion = new ArrayList<>(polygonRegionRepository.findAllByIdIn(polygonRegionIdsOfSelectedCollectionRegion));

        return findDataForRegionMap(polygonRegionsOfOperationRegion, polygonRegionsOfOtherCollectionRegions, polygonRegionsOfSelectedCollectionRegion);
    }

    public DataForRegionMap findDataForDistributionRegionMap(
            List<Long> polygonRegionIdsOfOperationRegion, List<Long> polygonRegionIdsOfOtherDistributionRegions, List<Long> polygonRegionIdsOfSelectedDistributionRegion) {

        List<PolygonRegion> polygonRegionsOfOperationRegion = new ArrayList<>(polygonRegionRepository.findAllByIdIn(polygonRegionIdsOfOperationRegion));
        List<PolygonRegion> polygonRegionsOfOtherDistributionRegions = new ArrayList<>(polygonRegionRepository.findAllByIdIn(polygonRegionIdsOfOtherDistributionRegions));
        List<PolygonRegion> polygonRegionsOfSelectedDistributionRegion = new ArrayList<>(polygonRegionRepository.findAllByIdIn(polygonRegionIdsOfSelectedDistributionRegion));

        return findDataForRegionMap(polygonRegionsOfOperationRegion, polygonRegionsOfOtherDistributionRegions, polygonRegionsOfSelectedDistributionRegion);
    }

    /**
     * Bu metod, kayıt bulunamadığında hata atmıyor; bilinçli olarak böyle yapıldı. Sorgulayan taraf sonuca göre hatayı kendi versin diye...
     */
    public OperationRegion findOperationRegionThatContainsPoint(BigDecimal latitude, BigDecimal longitude) {

        OperationRegion operationRegion = null;
        PolygonRegion polygonRegion = polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(latitude, longitude);

        if (polygonRegion != null) {

            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);

            for (OperationRegion or : operationRegionRepository.findAllAccordingToQueryFourDistinctByOrderByName()) {
                for (OperationRegionToPolygonRegion ortpr : or.getOperationRegionToPolygonRegions()) {
                    String absoluteNameInner = polygonRegionService.getPolygonRegionAbsoluteName(ortpr.getPolygonRegion());
                    if (absoluteName.equals(absoluteNameInner) || absoluteName.startsWith(absoluteNameInner + "/")) {
                        operationRegion = or;
                        break;
                    }
                }
            }
        }

        return operationRegion;
    }

    public OperationRegion findOperationRegionThatContainsWarehouse(Long warehouseId) {

        Warehouse warehouse = warehouseService.findByIdOrThrowResourceNotFoundException(warehouseId);

        BigDecimal lat = warehouse.getLocation().getPointOnMap().getLat();
        BigDecimal lng = warehouse.getLocation().getPointOnMap().getLng();

        return findOperationRegionThatContainsPoint(lat, lng);
    }

    /**
     * Dikkat: Bu metod direkt olarak bir Controller tarafından çağırılmıyorsa performans sorunları oluşabilir. Çünkü
     * son sorgudan önce entityManager.clear() ile tüm cache boşaltılıyor. Cache boşaltılmaz ise
     * son sorgu cache'ten geliyor ve json istediğimiz gibi (yani EntityGraph'ta belirttiğimiz gibi) dönmüyor.
     */
    public OperationRegion findOperationRegionThatContainsWarehouseAccordingToQueryThree(Long warehouseId) {

        OperationRegion operationRegion = findOperationRegionThatContainsWarehouse(warehouseId);

        // TODO: EntityManager'a dokunmadan bir çözüm bulabilir miyiz?
        entityManager.clear();

        if (operationRegion != null) {
            return operationRegionRepository.findAccordingToQueryThreeById(operationRegion.getId());
        } else {
            return null;
        }
    }
}
