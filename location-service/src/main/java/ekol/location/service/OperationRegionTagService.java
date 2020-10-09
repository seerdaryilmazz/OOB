package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.OperationRegion;
import ekol.location.domain.OperationRegionTag;
import ekol.location.repository.OperationRegionTagRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationRegionTagService {

    @Autowired
    private OperationRegionTagRepository operationRegionTagRepository;

    @Autowired
    private OperationRegionService operationRegionService;

    public OperationRegionTag findByIdOrThrowResourceNotFoundException(Long id) {

        OperationRegionTag persistedEntity = operationRegionTagRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Operation region tag with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public OperationRegionTag createOrUpdate(OperationRegionTag tag) {

        if (tag == null) {
            throw new BadRequestException("A tag must be specified.");
        }

        if (tag.getId() != null) {
            findByIdOrThrowResourceNotFoundException(tag.getId());
        }

        if (tag.getOperationRegion() == null || tag.getOperationRegion().getId() == null) {
            throw new BadRequestException("An operation region must be specified.");
        }

        OperationRegion operationRegion = operationRegionService.findByIdOrThrowResourceNotFoundException(tag.getOperationRegion().getId());
        tag.setOperationRegion(operationRegion);

        if (StringUtils.isBlank(tag.getValue())) {
            throw new BadRequestException("A value must be specified.");
        }

        tag.setValue(tag.getValue().trim());

        OperationRegionTag tagByOperationRegionIdAndValue =
                operationRegionTagRepository.findByOperationRegionIdAndValue(tag.getOperationRegion().getId(), tag.getValue());

        if (tagByOperationRegionIdAndValue != null) {
            if (tag.getId() == null || !tag.getId().equals(tagByOperationRegionIdAndValue.getId())) {
                throw new BadRequestException("The specified value is already assigned to the specified operation region.");
            }
        }

        return operationRegionTagRepository.save(tag);
    }

    @Transactional
    public void softDelete(Long id) {
        OperationRegionTag persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        operationRegionTagRepository.save(persistedEntity);
    }
}
