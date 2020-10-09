package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.DistributionRegion;
import ekol.location.domain.DistributionRegionToPolygonRegion;
import ekol.location.domain.PolygonRegion;
import ekol.location.repository.DistributionRegionToPolygonRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DistributionRegionToPolygonRegionService {

    @Autowired
    private DistributionRegionToPolygonRegionRepository distributionRegionToPolygonRegionRepository;

    @Autowired
    private PolygonRegionService polygonRegionService;

    @Autowired
    private DistributionRegionService distributionRegionService;

    public DistributionRegionToPolygonRegion findByIdOrThrowResourceNotFoundException(Long id) {

        DistributionRegionToPolygonRegion persistedEntity = distributionRegionToPolygonRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Distribution region to polygon region with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public DistributionRegionToPolygonRegion createOrUpdate(DistributionRegionToPolygonRegion distributionRegionToPolygonRegion) {

        if (distributionRegionToPolygonRegion == null) {
            throw new BadRequestException("A distribution region to polygon region must be specified.");
        }

        if (distributionRegionToPolygonRegion.getId() != null) {
            findByIdOrThrowResourceNotFoundException(distributionRegionToPolygonRegion.getId());
        }

        if (distributionRegionToPolygonRegion.getDistributionRegion() == null || distributionRegionToPolygonRegion.getDistributionRegion().getId() == null) {
            throw new BadRequestException("A distribution region must be specified.");
        }

        DistributionRegion distributionRegion = distributionRegionService.
                findByIdOrThrowResourceNotFoundException(distributionRegionToPolygonRegion.getDistributionRegion().getId());
        distributionRegionToPolygonRegion.setDistributionRegion(distributionRegion);

        if (distributionRegionToPolygonRegion.getPolygonRegion() == null || distributionRegionToPolygonRegion.getPolygonRegion().getId() == null) {
            throw new BadRequestException("A polygon region must be specified.");
        }

        PolygonRegion polygonRegion = polygonRegionService.
                findByIdOrThrowResourceNotFoundException(distributionRegionToPolygonRegion.getPolygonRegion().getId());
        distributionRegionToPolygonRegion.setPolygonRegion(polygonRegion);

        if (distributionRegionToPolygonRegion.getCategory() == null) {
            throw new BadRequestException("A category must be specified.");
        }

        Iterable<DistributionRegionToPolygonRegion> list = distributionRegionToPolygonRegionRepository.
                findAllByPolygonRegion_CountryIsoAlpha3Code(polygonRegion.getCountryIsoAlpha3Code());

        for (DistributionRegionToPolygonRegion elem : list) {

            if (distributionRegionToPolygonRegion.getId() == null || !distributionRegionToPolygonRegion.getId().equals(elem.getId())) {

                PolygonRegion otherPolygonRegion = elem.getPolygonRegion();
                String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
                String otherAbsoluteName = polygonRegionService.getPolygonRegionAbsoluteName(otherPolygonRegion);

                if (absoluteName.equals(otherAbsoluteName)) {
                    throw new BadRequestException("Specified polygon region is already used in a distribution region. Check " + absoluteName);
                } else if (absoluteName.startsWith(otherAbsoluteName + "/")) {
                    throw new BadRequestException("One of the parents of the specified polygon region is already used in a distribution region. Check " + absoluteName);
                } else if (otherAbsoluteName.startsWith(absoluteName + "/")) {
                    throw new BadRequestException("One of the children of the specified polygon region is already used in a distribution region. Check " + absoluteName);
                }
            }
        }

        return distributionRegionToPolygonRegionRepository.save(distributionRegionToPolygonRegion);
    }

    @Transactional
    public void softDelete(Long id) {
        DistributionRegionToPolygonRegion persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        distributionRegionToPolygonRegionRepository.save(persistedEntity);
    }
}
