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

@Service
public class DistributionRegionService {
	
	@Autowired
    private DistributionRegionRepository distributionRegionRepository;
	
	@Autowired
    private DistributionRegionToPolygonRegionService distributionRegionToPolygonRegionService;
	
	@Autowired
    private DistributionRegionToPolygonRegionRepository distributionRegionToPolygonRegionRepository;
	
	@Autowired
    private DistributionRegionTagService distributionRegionTagService;
	
	@Autowired
    private DistributionRegionTagRepository distributionRegionTagRepository;
	
	@Autowired
    private OperationRegionService operationRegionService;
	
	@Autowired
    private PolygonRegionService polygonRegionService;
	
	@Autowired
    private CustomsOfficeLocationRepository customsOfficeLocationRepository;

	@Autowired
    private KartoteksServiceClient kartoteksServiceClient;

    public DistributionRegion findByIdOrThrowResourceNotFoundException(Long id) {

        DistributionRegion persistedEntity = distributionRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Distribution region with specified id cannot be found: " + id);
        }
    }

    public DistributionRegion findAccordingToQueryTwoByIdOrThrowResourceNotFoundException(Long id) {

        DistributionRegion persistedEntity = distributionRegionRepository.findAccordingToQueryTwoById(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Distribution region with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public DistributionRegion createOrUpdate(DistributionRegion distributionRegion) {

        if (distributionRegion == null) {
            throw new BadRequestException("A distribution region must be specified.");
        }

        if (distributionRegion.getId() != null) {
            findByIdOrThrowResourceNotFoundException(distributionRegion.getId());
        }

        if (distributionRegion.getOperationRegion() == null || distributionRegion.getOperationRegion().getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        OperationRegion operationRegion = operationRegionService.findByIdOrThrowResourceNotFoundException(distributionRegion.getOperationRegion().getId());
        distributionRegion.setOperationRegion(operationRegion);

        if (StringUtils.isBlank(distributionRegion.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        distributionRegion.setName(distributionRegion.getName().trim());

        DistributionRegion distributionRegionByName = distributionRegionRepository.findByName(distributionRegion.getName());

        if (distributionRegionByName != null) {
            if (distributionRegion.getId() == null || !distributionRegion.getId().equals(distributionRegionByName.getId())) {
                throw new BadRequestException("The specified name is used by another distribution region.");
            }
        }

        Set<DistributionRegionTag> tags = distributionRegion.getTags();
        Set<DistributionRegionToPolygonRegion> distributionRegionToPolygonRegions = distributionRegion.getDistributionRegionToPolygonRegions();

        distributionRegion.setTags(null);
        distributionRegion.setDistributionRegionToPolygonRegions(null);

        DistributionRegion persistedDistributionRegion = distributionRegionRepository.save(distributionRegion);

        processTags(persistedDistributionRegion, tags);
        processDistributionRegionToPolygonRegions(persistedDistributionRegion, distributionRegionToPolygonRegions);

        return persistedDistributionRegion;
    }

    @Transactional
    public void processTags(DistributionRegion distributionRegion, Set<DistributionRegionTag> tags) {

        if (distributionRegion == null || distributionRegion.getId() == null) {
            throw new BadRequestException("A distribution region must be specified.");
        }

        distributionRegion = findByIdOrThrowResourceNotFoundException(distributionRegion.getId());

        Map<Long, DistributionRegionTag> existingRecordsMap = new HashMap<>();

        for (DistributionRegionTag elem : distributionRegionTagRepository.findAllByDistributionRegionId(distributionRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<DistributionRegionTag> recordsToBeCreated = new HashSet<>();
        Set<DistributionRegionTag> recordsToBeUpdated = new HashSet<>();
        Set<DistributionRegionTag> recordsToBeDeleted = new HashSet<>();

        if (tags != null) {
            for (DistributionRegionTag elem : tags) {
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

        Set<DistributionRegionTag> finalRecords = new HashSet<>();

        for (DistributionRegionTag elem : recordsToBeDeleted) {
            distributionRegionTagService.softDelete(elem.getId());
        }

        for (DistributionRegionTag elem : recordsToBeUpdated) {
            elem.setDistributionRegion(distributionRegion);
            finalRecords.add(distributionRegionTagService.createOrUpdate(elem));
        }

        for (DistributionRegionTag elem : recordsToBeCreated) {
            elem.setDistributionRegion(distributionRegion);
            finalRecords.add(distributionRegionTagService.createOrUpdate(elem));
        }

        distributionRegion.setTags(finalRecords);
    }

    @Transactional
    public void processDistributionRegionToPolygonRegions(DistributionRegion distributionRegion, Set<DistributionRegionToPolygonRegion> distributionRegionToPolygonRegions) {

        if (distributionRegion == null || distributionRegion.getId() == null) {
            throw new BadRequestException("A distribution region must be specified.");
        }

        distributionRegion = findByIdOrThrowResourceNotFoundException(distributionRegion.getId());

        Map<Long, DistributionRegionToPolygonRegion> existingRecordsMap = new HashMap<>();

        for (DistributionRegionToPolygonRegion elem : distributionRegionToPolygonRegionRepository.findAllByDistributionRegionId(distributionRegion.getId())) {
            existingRecordsMap.put(elem.getId(), elem);
        }

        Set<DistributionRegionToPolygonRegion> recordsToBeCreated = new HashSet<>();
        Set<DistributionRegionToPolygonRegion> recordsToBeUpdated = new HashSet<>();
        Set<DistributionRegionToPolygonRegion> recordsToBeDeleted = new HashSet<>();

        if (distributionRegionToPolygonRegions != null) {
            for (DistributionRegionToPolygonRegion elem : distributionRegionToPolygonRegions) {
                if (elem == null) {
                    throw new BadRequestException("A distribution region to polygon region must be specified.");
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
            throw new BadRequestException("There must be at least one distribution region to polygon region.");
        }

        Set<DistributionRegionToPolygonRegion> finalRecords = new HashSet<>();

        for (DistributionRegionToPolygonRegion elem : recordsToBeDeleted) {
            distributionRegionToPolygonRegionService.softDelete(elem.getId());
        }

        for (DistributionRegionToPolygonRegion elem : recordsToBeUpdated) {
            elem.setDistributionRegion(distributionRegion);
            finalRecords.add(distributionRegionToPolygonRegionService.createOrUpdate(elem));
        }

        for (DistributionRegionToPolygonRegion elem : recordsToBeCreated) {
            elem.setDistributionRegion(distributionRegion);
            finalRecords.add(distributionRegionToPolygonRegionService.createOrUpdate(elem));
        }

        distributionRegion.setDistributionRegionToPolygonRegions(finalRecords);
    }

    @Transactional
    public void softDelete(Long id) {

        DistributionRegion persistedEntity = findByIdOrThrowResourceNotFoundException(id);

        if (persistedEntity.getTags() != null) {
            for (DistributionRegionTag elem : persistedEntity.getTags()) {
                distributionRegionTagService.softDelete(elem.getId());
            }
        }

        if (persistedEntity.getDistributionRegionToPolygonRegions() != null) {
            for (DistributionRegionToPolygonRegion elem : persistedEntity.getDistributionRegionToPolygonRegions()) {
                distributionRegionToPolygonRegionService.softDelete(elem.getId());
            }
        }

        persistedEntity.setDeleted(true);

        distributionRegionRepository.save(persistedEntity);
    }


    public DistributionRegion findThatContainsPoint(BigDecimal latitude, BigDecimal longitude) {

        DistributionRegion distributionRegion = null;
        PolygonRegion polygonRegion = polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(latitude, longitude);

        if (polygonRegion != null) {

            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);

            for (DistributionRegion cr : distributionRegionRepository.findAllAccordingToQueryOneDistinctBy()) {
                for (DistributionRegionToPolygonRegion drtpr : cr.getDistributionRegionToPolygonRegions()) {
                    String absoluteNameInner = polygonRegionService.getPolygonRegionAbsoluteName(drtpr.getPolygonRegion());
                    if (absoluteName.equals(absoluteNameInner) || absoluteName.startsWith(absoluteNameInner + "/")) {
                        distributionRegion = cr;
                        break;
                    }
                }
            }
        }

        return distributionRegion;
    }

    public DistributionRegion findThatContainsCompanyLocation(Long companyLocationId) {
        CompanyLocation companyLocation = kartoteksServiceClient.getCompanyLocation(companyLocationId);
        BigDecimal lat = companyLocation.getPostaladdress().getPointOnMap().getLat();
        BigDecimal lng = companyLocation.getPostaladdress().getPointOnMap().getLng();

        return findThatContainsPoint(lat, lng);
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
    public RegionOfLocationResponse findDistributionAndOperationRegionOfLocationId(Long companyLocationId) {
    	CompanyLocation companyLocation = kartoteksServiceClient.getCompanyLocation(companyLocationId);
        BigDecimal latitude = companyLocation.getPostaladdress().getPointOnMap().getLat();
        BigDecimal longitude = companyLocation.getPostaladdress().getPointOnMap().getLng();

        PolygonRegion polygonRegion = polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(latitude, longitude);

        /**
         * Bu metod, kayıt bulunamadığında hata atmıyor; bilinçli olarak böyle yapıldı. Sorgulayan taraf sonuca göre hatayı kendi versin diye...
         */
        if (polygonRegion != null) {
            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
            for (DistributionRegion dr : distributionRegionRepository.findAllAccordingToQueryOneDistinctBy()) {
                for (DistributionRegionToPolygonRegion drtpr : dr.getDistributionRegionToPolygonRegions()) {
                    String absoluteNameInner = polygonRegionService.getPolygonRegionAbsoluteName(drtpr.getPolygonRegion());
                    if (absoluteName.equals(absoluteNameInner) || absoluteName.startsWith(absoluteNameInner + "/")) {
                        return new RegionOfLocationResponse(drtpr.getCategory(), dr, dr.getOperationRegion());

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
    public RegionOfLocationResponse findDistributionAndOperationRegionByPoint(Point point) {
    	
    	PolygonRegion polygonRegion = polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(point.getLat(), point.getLng());

        /**
         * Bu metod, kayıt bulunamadığında hata atmıyor; bilinçli olarak böyle yapıldı. Sorgulayan taraf sonuca göre hatayı kendi versin diye...
         */
        if (polygonRegion != null) {
            String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
            for (DistributionRegion dr : distributionRegionRepository.findAllAccordingToQueryOneDistinctBy()) {
                for (DistributionRegionToPolygonRegion drtpr : dr.getDistributionRegionToPolygonRegions()) {
                    String absoluteNameInner = polygonRegionService.getPolygonRegionAbsoluteName(drtpr.getPolygonRegion());
                    if (absoluteName.equals(absoluteNameInner) || absoluteName.startsWith(absoluteNameInner + "/")) {
                        return new RegionOfLocationResponse(drtpr.getCategory(), dr, dr.getOperationRegion());

                    }
                }
            }
        }
        return null;
    }

       
}
