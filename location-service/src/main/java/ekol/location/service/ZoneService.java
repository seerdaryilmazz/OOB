package ekol.location.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.ResourceNotFoundException;
import ekol.location.client.KartoteksServiceClient;
import ekol.location.client.dto.CompanyLocation;
import ekol.location.domain.*;
import ekol.location.repository.*;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 13/12/16.
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ZoneService {

    private ZoneRepository zoneRepository;
    private ZoneTagRepository zoneTagRepository;
    private ZoneTypeRepository zoneTypeRepository;
    private ZoneZipCodeRepository zoneZipCodeRepository;
    private ZonePolygonRegionRepository zonePolygonRegionRepository;
    private PolygonRegionRepository polygonRegionRepository;
    private PolygonRegionService polygonRegionService;
    private KartoteksServiceClient kartoteksServiceClient;
    
    public static final String ZONETYPE_DISTRIBUTION_CODE = "DISTRIBUTION";
    public static final String ZONETYPE_COLLECTION_CODE = "COLLECTION";

    public Zone findWithDetailsById(Long id) {
        return zoneRepository.findWithDetailsById(id);
    }

    public List<Zone> findAll() {
        return zoneRepository.findAllByOrderByNameAsc();
    }

    public List<Zone> findAll(String zoneTypeCode) {
        ZoneType type = zoneTypeRepository.findByCode(zoneTypeCode);
        if (type == null) {
            throw new ResourceNotFoundException("Zone type does not exist");
        }
        return findAll(type);
    }

    public List<Zone> findAll(ZoneType zoneType) {
        return zoneRepository.findByZoneTypeOrderByNameAsc(zoneType);
    }

    private void saveZoneTags(Zone zone, Set<ZoneTag> tags) {
        List<String> tagValues = new ArrayList<>();
        if (tags != null && !tags.isEmpty()) {
            tagValues.addAll(tags.stream().map(tag -> tag.getValue()).collect(Collectors.toList()));
        }

        List<ZoneTag> currentTags = new ArrayList<>();
        currentTags.addAll(zoneTagRepository.findByZoneId(zone.getId()));

        List<String> currentTagValues = new ArrayList<>();
        currentTagValues.addAll(currentTags.stream().map(tag -> tag.getValue()).collect(Collectors.toList()));

        // Add new ones
        tagValues.forEach(value -> {
            if (!currentTagValues.contains(value)) {
                ZoneTag zoneTag = new ZoneTag();
                zoneTag.setValue(value);
                zoneTag.setZone(zone);
                zoneTagRepository.save(zoneTag);
            }
        });

        // Delete deleted ones
        currentTags.forEach(tag -> {
            if (!tagValues.contains(tag.getValue())) {
                tag.setDeleted(true);
                zoneTagRepository.save(tag);
            }
        });
    }

    private void saveZoneZipCodes(Zone zone, Set<ZoneZipCode> zipCodes) {
        // Delete deleted ones
        List<ZoneZipCode> currentZipCodes = new ArrayList<>();
        currentZipCodes.addAll(zoneZipCodeRepository.findByZoneId(zone.getId()));
        currentZipCodes.forEach(currentZipCode -> {
            boolean exists = zipCodes.stream().anyMatch(zipCode -> currentZipCode.getId().equals(zipCode.getId()));
            if (!exists) {
                currentZipCode.setDeleted(true);
                zoneZipCodeRepository.save(currentZipCode);
            }
        });

        // Add / update
        if (zipCodes != null && !zipCodes.isEmpty()) {
            zipCodes.forEach(zipCode -> {
                zipCode.setZone(zone);
                zoneZipCodeRepository.save(zipCode);
            });
        }
    }

    private void saveZonePolygonRegions(Zone zone, Set<ZonePolygonRegion> polygonRegions, Set<ZoneZipCode> zipCodes) {
        List<ZonePolygonRegion> existingOnes = zonePolygonRegionRepository.findByZoneId(zone.getId());

        // Delete removed ones
        if (existingOnes != null && !existingOnes.isEmpty()) {
            existingOnes.forEach(existingOne -> {
                boolean[] deleted = new boolean[]{true};

                if (polygonRegions != null && !polygonRegions.isEmpty()) {
                    polygonRegions.forEach(polygonRegion -> {
                        if (polygonRegion.getPolygonRegion().getName().equals(existingOne.getPolygonRegion().getName()) &&
                                polygonRegion.getPolygonRegion().getParent().equals(existingOne.getPolygonRegion().getParent()) &&
                                ((polygonRegion.getZoneZipCodeRep() == null && existingOne.getZoneZipCode() == null) ||
                                        (polygonRegion.getZoneZipCodeRep() != null && polygonRegion.getZoneZipCodeRep().equals(existingOne.getZoneZipCode().getRep()))
                                )) {
                            deleted[0] = false;
                        }
                    });
                }

                if (deleted[0]) {
                    existingOne.setDeleted(true);
                    zonePolygonRegionRepository.save(existingOne);
                }
            });
        }

        // Update others
        if (polygonRegions != null && !polygonRegions.isEmpty()) {
            polygonRegions.forEach(polygonRegion -> {

                // Find the existing one
                ZonePolygonRegion[] existingOne = new ZonePolygonRegion[]{null};
                if (existingOnes != null && !existingOnes.isEmpty()) {
                    existingOnes.forEach(eo -> {
                        if (eo.getPolygonRegion().getName().equals(polygonRegion.getPolygonRegion().getName()) &&
                                eo.getPolygonRegion().getParent().equals(polygonRegion.getPolygonRegion().getParent()) &&
                                ((eo.getZoneZipCode() == null && polygonRegion.getZoneZipCodeRep() == null) ||
                                        (eo.getZoneZipCode() != null && eo.getZoneZipCode().getRep().equals(polygonRegion.getZoneZipCodeRep()))
                                )) {
                            existingOne[0] = eo;
                        }
                    });
                }

                if (existingOne[0] == null) {
                    // Add new
                    PolygonRegion selectedPolygonRegion = polygonRegionRepository.findByParentAndName(polygonRegion.getPolygonRegion().getParent(), polygonRegion.getPolygonRegion().getName());
                    polygonRegion.setPolygonRegion(selectedPolygonRegion);
                    polygonRegion.setZone(zone);
                    polygonRegion.setZoneZipCode(getZoneZipCodeFromRep(zipCodes, polygonRegion.getZoneZipCodeRep()));
                    zonePolygonRegionRepository.save(polygonRegion);
                } else {
                    // Update existing
                    existingOne[0].setSelected(polygonRegion.isSelected());
                    zonePolygonRegionRepository.save(existingOne[0]);
                }
            });
        }
    }

    private ZoneZipCode getZoneZipCodeFromRep(Set<ZoneZipCode> zipCodes, String rep) {
        ZoneZipCode[] foundOne = new ZoneZipCode[]{null};

        if (zipCodes != null && !zipCodes.isEmpty() && StringUtils.isNotBlank(rep)) {
            zipCodes.forEach(zipCode -> {
                if (rep.equals(zipCode.getRep())) {
                    foundOne[0] = zipCode;
                }
            });
        }

        return foundOne[0];
    }

    @Transactional
    public void save(Zone zone) {
        Set<ZoneTag> tags = zone.getTags();
        Set<ZoneZipCode> zipCodes = zone.getZipCodes();
        Set<ZonePolygonRegion> polygonRegions = zone.getPolygonRegions();

        zoneRepository.save(zone);
        saveZoneTags(zone, tags);
        saveZoneZipCodes(zone, zipCodes);
        saveZonePolygonRegions(zone, polygonRegions, zipCodes);
    }

    public void delete(Long zoneId) {
        if (zoneId != null) {
            Zone zoneToDelete = zoneRepository.findOne(zoneId);
            zoneToDelete.setDeleted(true);
            zoneRepository.save(zoneToDelete);
        }
    }

    public Zone findZoneByLocationId(Long locationId, String zoneTypeCode) {

        CompanyLocation sender = kartoteksServiceClient.getCompanyLocation(locationId);

        if(sender == null || sender.getPostaladdress() == null || sender.getPostaladdress().getPointOnMap() == null) {
            return null;
        }

        BigDecimal lat = sender.getPostaladdress().getPointOnMap().getLat();
        BigDecimal lng = sender.getPostaladdress().getPointOnMap().getLng();

        Zone zone = null;
        List<Zone> zones = zoneRepository.findAllForCoordinateSearchDistinctByZoneTypeCode(zoneTypeCode);

        for (Zone z : zones) {
            for (ZonePolygonRegion zpr : z.getPolygonRegions()) {
                if (zpr.isSelected() && polygonRegionService.findLowestLevelPolygonRegionThatContainsPoint(zpr.getPolygonRegion(), lat, lng) != null) {
                    zone = z;
                    break;
                }
            }
        }

        if (zone == null) {
           return null;
        } else {
            return zoneRepository.findWithDetailsById(zone.getId());
        }
    }
}
