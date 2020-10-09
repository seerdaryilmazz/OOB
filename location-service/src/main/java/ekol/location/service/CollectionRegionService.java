package ekol.location.service;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.*;
import ekol.location.client.KartoteksServiceClient;
import ekol.location.client.dto.CompanyLocation;
import ekol.location.domain.*;
import ekol.location.domain.dto.RegionOfLocationResponse;
import ekol.location.domain.location.comnon.Point;
import ekol.location.repository.*;
import lombok.AllArgsConstructor;

@Service
public class CollectionRegionService {

	@Autowired
    private CollectionRegionRepository collectionRegionRepository;
	
	@Autowired
    private CollectionRegionToPolygonRegionService collectionRegionToPolygonRegionService;
	
	@Autowired
    private CollectionRegionToPolygonRegionRepository collectionRegionToPolygonRegionRepository;
	
	@Autowired
    private CollectionRegionTagService collectionRegionTagService;
	
	@Autowired
    private CollectionRegionTagRepository collectionRegionTagRepository;
	
	@Autowired
    private OperationRegionService operationRegionService;
	
	@Autowired
    private PolygonRegionService polygonRegionService;
	
	@Autowired
    private CustomsOfficeLocationRepository customsOfficeLocationRepository;
	
	@Autowired
    private KartoteksServiceClient kartoteksServiceClient;

    public CollectionRegion findByIdOrThrowResourceNotFoundException(Long id) {

        CollectionRegion persistedEntity = collectionRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Collection region with specified id cannot be found: " + id);
        }
    }

    public CollectionRegion findAccordingToQueryTwoByIdOrThrowResourceNotFoundException(Long id) {

        CollectionRegion persistedEntity = collectionRegionRepository.findAccordingToQueryTwoById(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Collection region with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public CollectionRegion createOrUpdate(CollectionRegion collectionRegion) {

        if (collectionRegion == null) {
            throw new BadRequestException("A collection region must be specified.");
        }

        if (collectionRegion.getId() != null) {
            findByIdOrThrowResourceNotFoundException(collectionRegion.getId());
        }

        if (collectionRegion.getOperationRegion() == null || collectionRegion.getOperationRegion().getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        OperationRegion operationRegion = operationRegionService.findByIdOrThrowResourceNotFoundException(collectionRegion.getOperationRegion().getId());
        collectionRegion.setOperationRegion(operationRegion);

        if (StringUtils.isBlank(collectionRegion.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        collectionRegion.setName(collectionRegion.getName().trim());

        CollectionRegion collectionRegionByName = collectionRegionRepository.findByName(collectionRegion.getName());

        if (collectionRegionByName != null) {
            if (collectionRegion.getId() == null || !collectionRegion.getId().equals(collectionRegionByName.getId())) {
                throw new BadRequestException("The specified name is used by another collection region.");
            }
        }

        Set<CollectionRegionTag> tags = collectionRegion.getTags();
        Set<CollectionRegionToPolygonRegion> collectionRegionToPolygonRegions = collectionRegion.getCollectionRegionToPolygonRegions();

        collectionRegion.setTags(null);
        collectionRegion.setCollectionRegionToPolygonRegions(null);

        CollectionRegion persistedCollectionRegion = collectionRegionRepository.save(collectionRegion);

        processTags(persistedCollectionRegion, tags);
        processCollectionRegionToPolygonRegions(persistedCollectionRegion, collectionRegionToPolygonRegions);

        return persistedCollectionRegion;
    }

    @Transactional
    public void processTags(CollectionRegion collectionRegion, Set<CollectionRegionTag> tags) {

        if (collectionRegion == null || collectionRegion.getId() == null) {
            throw new BadRequestException("A collection region must be specified.");
        }

        collectionRegion = findByIdOrThrowResourceNotFoundException(collectionRegion.getId());

        Map<Long, CollectionRegionTag> existingRecordsMap = new HashMap<>();

        for (CollectionRegionTag elem : collectionRegionTagRepository.findAllByCollectionRegionId(collectionRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<CollectionRegionTag> recordsToBeCreated = new HashSet<>();
        Set<CollectionRegionTag> recordsToBeUpdated = new HashSet<>();
        Set<CollectionRegionTag> recordsToBeDeleted = new HashSet<>();

        if (tags != null) {
            for (CollectionRegionTag elem : tags) {
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

        Set<CollectionRegionTag> finalRecords = new HashSet<>();

        for (CollectionRegionTag elem : recordsToBeDeleted) {
            collectionRegionTagService.softDelete(elem.getId());
        }

        for (CollectionRegionTag elem : recordsToBeUpdated) {
            elem.setCollectionRegion(collectionRegion);
            finalRecords.add(collectionRegionTagService.createOrUpdate(elem));
        }

        for (CollectionRegionTag elem : recordsToBeCreated) {
            elem.setCollectionRegion(collectionRegion);
            finalRecords.add(collectionRegionTagService.createOrUpdate(elem));
        }

        collectionRegion.setTags(finalRecords);
    }

    @Transactional
    public void processCollectionRegionToPolygonRegions(CollectionRegion collectionRegion, Set<CollectionRegionToPolygonRegion> collectionRegionToPolygonRegions) {

        if (collectionRegion == null || collectionRegion.getId() == null) {
            throw new BadRequestException("A collection region must be specified.");
        }

        collectionRegion = findByIdOrThrowResourceNotFoundException(collectionRegion.getId());

        Map<Long, CollectionRegionToPolygonRegion> existingRecordsMap = new HashMap<>();

        for (CollectionRegionToPolygonRegion elem : collectionRegionToPolygonRegionRepository.findAllByCollectionRegionId(collectionRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<CollectionRegionToPolygonRegion> recordsToBeCreated = new HashSet<>();
        Set<CollectionRegionToPolygonRegion> recordsToBeUpdated = new HashSet<>();
        Set<CollectionRegionToPolygonRegion> recordsToBeDeleted = new HashSet<>();

        if (collectionRegionToPolygonRegions != null) {
            for (CollectionRegionToPolygonRegion elem : collectionRegionToPolygonRegions) {
                if (elem == null) {
                    throw new BadRequestException("A collection region to polygon region must be specified.");
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
            throw new BadRequestException("There must be at least one collection region to polygon region.");
        }

        Set<CollectionRegionToPolygonRegion> finalRecords = new HashSet<>();

        for (CollectionRegionToPolygonRegion elem : recordsToBeDeleted) {
            collectionRegionToPolygonRegionService.softDelete(elem.getId());
        }

        for (CollectionRegionToPolygonRegion elem : recordsToBeUpdated) {
            elem.setCollectionRegion(collectionRegion);
            finalRecords.add(collectionRegionToPolygonRegionService.createOrUpdate(elem));
        }

        for (CollectionRegionToPolygonRegion elem : recordsToBeCreated) {
            elem.setCollectionRegion(collectionRegion);
            finalRecords.add(collectionRegionToPolygonRegionService.createOrUpdate(elem));
        }

        collectionRegion.setCollectionRegionToPolygonRegions(finalRecords);
    }

    @Transactional
    public void softDelete(Long id) {

        CollectionRegion persistedEntity = findByIdOrThrowResourceNotFoundException(id);

        if (persistedEntity.getTags() != null) {
            for (CollectionRegionTag elem : persistedEntity.getTags()) {
                collectionRegionTagService.softDelete(elem.getId());
            }
        }

        if (persistedEntity.getCollectionRegionToPolygonRegions() != null) {
            for (CollectionRegionToPolygonRegion elem : persistedEntity.getCollectionRegionToPolygonRegions()) {
                collectionRegionToPolygonRegionService.softDelete(elem.getId());
            }
        }

        persistedEntity.setDeleted(true);

        collectionRegionRepository.save(persistedEntity);
    }
    
    public Point findPointCustomsLocationId(Long customsLocationId) {
    	return customsOfficeLocationRepository.findOne(customsLocationId).getPointOnMap();
    }
    
    public Point findPointCompanyLocationId(Long companyLocationId) {
    	CompanyLocation companyLocation = kartoteksServiceClient.getCompanyLocation(companyLocationId);
        BigDecimal latitude = companyLocation.getPostaladdress().getPointOnMap().getLat();
        BigDecimal longitude = companyLocation.getPostaladdress().getPointOnMap().getLng();
        return new Point(latitude, longitude);
    }

    /**
     * Dikkat: Bu metod direkt olarak bir Controller tarafından çağırılmıyorsa performans sorunları oluşabilir. Çünkü
     * son sorgudan önce entityManager.clear() ile tüm cache boşaltılıyor. Cache boşaltılmaz ise
     * son sorgu cache'ten geliyor ve json istediğimiz gibi (yani EntityGraph'ta belirttiğimiz gibi) dönmüyor.
     */
    public RegionOfLocationResponse findCollectionAndOperationRegionOfLocationId(Long companyLocationId) {
    	CompanyLocation companyLocation = kartoteksServiceClient.getCompanyLocation(companyLocationId);
        BigDecimal latitude = companyLocation.getPostaladdress().getPointOnMap().getLat();
        BigDecimal longitude = companyLocation.getPostaladdress().getPointOnMap().getLng();

        /**
         * Bu döngü, kayıt bulunamadığında hata atmıyor; bilinçli olarak böyle yapıldı. Sorgulayan taraf sonuca göre hatayı kendi versin diye...
         */
        PolygonRegion polygonRegion = polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(latitude, longitude);
        if (polygonRegion != null) {
            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
            for (CollectionRegion cr : collectionRegionRepository.findAllAccordingToQueryOneDistinctBy()) {
                for (CollectionRegionToPolygonRegion crtpr : cr.getCollectionRegionToPolygonRegions()) {
                    String absoluteNameInner = polygonRegionService.getPolygonRegionAbsoluteName(crtpr.getPolygonRegion());
                    if (absoluteName.equals(absoluteNameInner) || absoluteName.startsWith(absoluteNameInner + "/")) {
                        return new RegionOfLocationResponse(crtpr.getCategory(), cr, cr.getOperationRegion());
                    }
                }
            }
        }

        return null;
    }
    
    /**
     * Dikkat: Bu metod direkt olarak bir Controller tarafından çağırılmıyorsa performans sorunları oluşabilir. Çünkü
     * son sorgudan önce entityManager.clear() ile tüm cache boşaltılıyor. Cache boşaltılmaz ise
     * son sorgu cache'ten geliyor ve json istediğimiz gibi (yani EntityGraph'ta belirttiğimiz gibi) dönmüyor.
     */
    public RegionOfLocationResponse findCollectionAndOperationRegionByPoint(Point point) {
    	
    	/**
         * Bu döngü, kayıt bulunamadığında hata atmıyor; bilinçli olarak böyle yapıldı. Sorgulayan taraf sonuca göre hatayı kendi versin diye...
         */
    	PolygonRegion polygonRegion = polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(point.getLat(), point.getLng());
        if (polygonRegion != null) {
            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
            for (CollectionRegion cr : collectionRegionRepository.findAllAccordingToQueryOneDistinctBy()) {
                for (CollectionRegionToPolygonRegion crtpr : cr.getCollectionRegionToPolygonRegions()) {
                    String absoluteNameInner = polygonRegionService.getPolygonRegionAbsoluteName(crtpr.getPolygonRegion());
                    if (absoluteName.equals(absoluteNameInner) || absoluteName.startsWith(absoluteNameInner + "/")) {
                        return new RegionOfLocationResponse(crtpr.getCategory(), cr, cr.getOperationRegion());
                    }
                }
            }
        }

        return null;
    }

}
