package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.CollectionRegion;
import ekol.location.domain.CollectionRegionToPolygonRegion;
import ekol.location.domain.PolygonRegion;
import ekol.location.repository.CollectionRegionToPolygonRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionRegionToPolygonRegionService {

    @Autowired
    private CollectionRegionToPolygonRegionRepository collectionRegionToPolygonRegionRepository;

    @Autowired
    private PolygonRegionService polygonRegionService;

    @Autowired
    private CollectionRegionService collectionRegionService;

    public CollectionRegionToPolygonRegion findByIdOrThrowResourceNotFoundException(Long id) {

        CollectionRegionToPolygonRegion persistedEntity = collectionRegionToPolygonRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Collection region to polygon region with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public CollectionRegionToPolygonRegion createOrUpdate(CollectionRegionToPolygonRegion collectionRegionToPolygonRegion) {

        if (collectionRegionToPolygonRegion == null) {
            throw new BadRequestException("A collection region to polygon region must be specified.");
        }

        if (collectionRegionToPolygonRegion.getId() != null) {
            findByIdOrThrowResourceNotFoundException(collectionRegionToPolygonRegion.getId());
        }

        if (collectionRegionToPolygonRegion.getCollectionRegion() == null || collectionRegionToPolygonRegion.getCollectionRegion().getId() == null) {
            throw new BadRequestException("A collection region must be specified.");
        }

        CollectionRegion collectionRegion = collectionRegionService.
                findByIdOrThrowResourceNotFoundException(collectionRegionToPolygonRegion.getCollectionRegion().getId());
        collectionRegionToPolygonRegion.setCollectionRegion(collectionRegion);

        if (collectionRegionToPolygonRegion.getPolygonRegion() == null || collectionRegionToPolygonRegion.getPolygonRegion().getId() == null) {
            throw new BadRequestException("A polygon region must be specified.");
        }

        PolygonRegion polygonRegion = polygonRegionService.
                findByIdOrThrowResourceNotFoundException(collectionRegionToPolygonRegion.getPolygonRegion().getId());
        collectionRegionToPolygonRegion.setPolygonRegion(polygonRegion);

        if (collectionRegionToPolygonRegion.getCategory() == null) {
            throw new BadRequestException("A category must be specified.");
        }

        Iterable<CollectionRegionToPolygonRegion> list = collectionRegionToPolygonRegionRepository.
                findAllByPolygonRegion_CountryIsoAlpha3Code(polygonRegion.getCountryIsoAlpha3Code());

        for (CollectionRegionToPolygonRegion elem : list) {

            if (collectionRegionToPolygonRegion.getId() == null || !collectionRegionToPolygonRegion.getId().equals(elem.getId())) {

                PolygonRegion otherPolygonRegion = elem.getPolygonRegion();
                String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
                String otherAbsoluteName = polygonRegionService.getPolygonRegionAbsoluteName(otherPolygonRegion);

                if (absoluteName.equals(otherAbsoluteName)) {
                    throw new BadRequestException("Specified polygon region is already used in a collection region. Check " + absoluteName);
                } else if (absoluteName.startsWith(otherAbsoluteName + "/")) {
                    throw new BadRequestException("One of the parents of the specified polygon region is already used in a collection region. Check " + absoluteName);
                } else if (otherAbsoluteName.startsWith(absoluteName + "/")) {
                    throw new BadRequestException("One of the children of the specified polygon region is already used in a collection region. Check " + absoluteName);
                }
            }
        }

        return collectionRegionToPolygonRegionRepository.save(collectionRegionToPolygonRegion);
    }

    @Transactional
    public void softDelete(Long id) {
        CollectionRegionToPolygonRegion persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        collectionRegionToPolygonRegionRepository.save(persistedEntity);
    }
}
