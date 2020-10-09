package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.CollectionRegion;
import ekol.location.domain.CollectionRegionTag;
import ekol.location.repository.CollectionRegionTagRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CollectionRegionTagService {

    @Autowired
    private CollectionRegionTagRepository collectionRegionTagRepository;

    @Autowired
    private CollectionRegionService collectionRegionService;

    public CollectionRegionTag findByIdOrThrowResourceNotFoundException(Long id) {

        CollectionRegionTag persistedEntity = collectionRegionTagRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Collection region tag with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public CollectionRegionTag createOrUpdate(CollectionRegionTag tag) {

        if (tag == null) {
            throw new BadRequestException("A tag must be specified.");
        }

        if (tag.getId() != null) {
            findByIdOrThrowResourceNotFoundException(tag.getId());
        }

        if (tag.getCollectionRegion() == null || tag.getCollectionRegion().getId() == null) {
            throw new BadRequestException("A collection region must be specified.");
        }

        CollectionRegion collectionRegion = collectionRegionService.findByIdOrThrowResourceNotFoundException(tag.getCollectionRegion().getId());
        tag.setCollectionRegion(collectionRegion);

        if (StringUtils.isBlank(tag.getValue())) {
            throw new BadRequestException("A value must be specified.");
        }

        tag.setValue(tag.getValue().trim());

        CollectionRegionTag tagByCollectionRegionIdAndValue =
                collectionRegionTagRepository.findByCollectionRegionIdAndValue(tag.getCollectionRegion().getId(), tag.getValue());

        if (tagByCollectionRegionIdAndValue != null) {
            if (tag.getId() == null || !tag.getId().equals(tagByCollectionRegionIdAndValue.getId())) {
                throw new BadRequestException("The specified value is already assigned to the specified collection region.");
            }
        }

        return collectionRegionTagRepository.save(tag);
    }

    @Transactional
    public void softDelete(Long id) {
        CollectionRegionTag persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        collectionRegionTagRepository.save(persistedEntity);
    }
}
