package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.OperationRegion;
import ekol.location.domain.OperationRegionToPolygonRegion;
import ekol.location.domain.PolygonRegion;
import ekol.location.repository.OperationRegionToPolygonRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationRegionToPolygonRegionService {

    @Autowired
    private OperationRegionToPolygonRegionRepository operationRegionToPolygonRegionRepository;

    @Autowired
    private PolygonRegionService polygonRegionService;

    @Autowired
    private OperationRegionService operationRegionService;

    public OperationRegionToPolygonRegion findByIdOrThrowResourceNotFoundException(Long id) {

        OperationRegionToPolygonRegion persistedEntity = operationRegionToPolygonRegionRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Operation region to polygon region with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public OperationRegionToPolygonRegion createOrUpdate(OperationRegionToPolygonRegion operationRegionToPolygonRegion) {

        if (operationRegionToPolygonRegion == null) {
            throw new BadRequestException("An operation region to polygon region must be specified.");
        }

        if (operationRegionToPolygonRegion.getId() != null) {
            findByIdOrThrowResourceNotFoundException(operationRegionToPolygonRegion.getId());
        }

        if (operationRegionToPolygonRegion.getOperationRegion() == null || operationRegionToPolygonRegion.getOperationRegion().getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        OperationRegion operationRegion = operationRegionService.
                findByIdOrThrowResourceNotFoundException(operationRegionToPolygonRegion.getOperationRegion().getId());
        operationRegionToPolygonRegion.setOperationRegion(operationRegion);

        if (operationRegionToPolygonRegion.getPolygonRegion() == null || operationRegionToPolygonRegion.getPolygonRegion().getId() == null) {
            throw new BadRequestException("A polygon region must be specified.");
        }

        PolygonRegion polygonRegion = polygonRegionService.
                findByIdOrThrowResourceNotFoundException(operationRegionToPolygonRegion.getPolygonRegion().getId());
        operationRegionToPolygonRegion.setPolygonRegion(polygonRegion);

        Iterable<OperationRegionToPolygonRegion> list = operationRegionToPolygonRegionRepository.
                findAllByPolygonRegion_CountryIsoAlpha3Code(polygonRegion.getCountryIsoAlpha3Code());

        for (OperationRegionToPolygonRegion elem : list) {

            if (operationRegionToPolygonRegion.getId() == null || !operationRegionToPolygonRegion.getId().equals(elem.getId())) {

                PolygonRegion otherPolygonRegion = elem.getPolygonRegion();
                String absoluteName = polygonRegionService.getPolygonRegionAbsoluteName(polygonRegion);
                String otherAbsoluteName = polygonRegionService.getPolygonRegionAbsoluteName(otherPolygonRegion);

                if (absoluteName.equals(otherAbsoluteName)) {
                    throw new BadRequestException("Specified polygon region is already used in an operation region. Check " + absoluteName);
                } else if (absoluteName.startsWith(otherAbsoluteName + "/")) {
                    throw new BadRequestException("One of the parents of the specified polygon region is already used in an operation region. Check " + absoluteName);
                } else if (otherAbsoluteName.startsWith(absoluteName + "/")) {
                    throw new BadRequestException("One of the children of the specified polygon region is already used in an operation region. Check " + absoluteName);
                }
            }
        }

        return operationRegionToPolygonRegionRepository.save(operationRegionToPolygonRegion);
    }

    @Transactional
    public void softDelete(Long id) {
        OperationRegionToPolygonRegion persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        // TODO: Silmeden önce yapılması gereken kontroller var mı?
        persistedEntity.setDeleted(true);
        operationRegionToPolygonRegionRepository.save(persistedEntity);
    }
}
