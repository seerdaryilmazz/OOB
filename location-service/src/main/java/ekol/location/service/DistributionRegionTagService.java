package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.DistributionRegion;
import ekol.location.domain.DistributionRegionTag;
import ekol.location.repository.DistributionRegionTagRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DistributionRegionTagService {

    @Autowired
    private DistributionRegionTagRepository distributionRegionTagRepository;

    @Autowired
    private DistributionRegionService distributionRegionService;

    public DistributionRegionTag findByIdOrThrowResourceNotFoundException(Long id) {

        DistributionRegionTag persistedEntity = distributionRegionTagRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Distribution region tag with specified id cannot be found: " + id);
        }
    }

    @Transactional
    public DistributionRegionTag createOrUpdate(DistributionRegionTag tag) {

        if (tag == null) {
            throw new BadRequestException("A tag must be specified.");
        }

        if (tag.getId() != null) {
            findByIdOrThrowResourceNotFoundException(tag.getId());
        }

        if (tag.getDistributionRegion() == null || tag.getDistributionRegion().getId() == null) {
            throw new BadRequestException("A distribution region must be specified.");
        }

        DistributionRegion distributionRegion = distributionRegionService.findByIdOrThrowResourceNotFoundException(tag.getDistributionRegion().getId());
        tag.setDistributionRegion(distributionRegion);

        if (StringUtils.isBlank(tag.getValue())) {
            throw new BadRequestException("A value must be specified.");
        }

        tag.setValue(tag.getValue().trim());

        DistributionRegionTag tagByDistributionRegionIdAndValue =
                distributionRegionTagRepository.findByDistributionRegionIdAndValue(tag.getDistributionRegion().getId(), tag.getValue());

        if (tagByDistributionRegionIdAndValue != null) {
            if (tag.getId() == null || !tag.getId().equals(tagByDistributionRegionIdAndValue.getId())) {
                throw new BadRequestException("The specified value is already assigned to the specified distribution region.");
            }
        }

        return distributionRegionTagRepository.save(tag);
    }

    @Transactional
    public void softDelete(Long id) {
        DistributionRegionTag persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        distributionRegionTagRepository.save(persistedEntity);
    }
}
