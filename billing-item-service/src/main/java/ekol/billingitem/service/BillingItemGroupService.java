package ekol.billingitem.service;

import ekol.billingitem.domain.BillingItemGroup;
import ekol.billingitem.domain.MoveBillingItemGroupRequest;
import ekol.billingitem.repository.BillingItemGroupRepository;
import ekol.billingitem.repository.BillingItemRepository;
import ekol.billingitem.util.Util;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@SuppressWarnings("SpringJavaAutowiringInspection")
public class BillingItemGroupService {

    @Autowired
    private BillingItemGroupRepository billingItemGroupRepository;

    @Autowired
    private BillingItemRepository billingItemRepository;

    public BillingItemGroup findByIdOrThrowResourceNotFoundException(Long id, boolean findAllParents) {

        BillingItemGroup persistedEntity = billingItemGroupRepository.findOne(id);

        if (persistedEntity != null) {
            if (findAllParents && persistedEntity.getParent() != null) {
                persistedEntity.setParent(findByIdOrThrowResourceNotFoundException(persistedEntity.getParent().getId(), true));
            }
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Billing item group cannot be found. id: {0} ", id);
        }
    }

    public void checkActiveness(BillingItemGroup group) {
        if (!group.getActive()) {
            throw new BadRequestException("Billing item group is not active. id: {0} ", group.getId());
        }
    }

    private void checkName(BillingItemGroup parent, String name) {

        long countByName;

        if (parent != null) {
            countByName = billingItemGroupRepository.countByParentIdAndNameAndActiveTrue(parent.getId(), name);
        } else {
            countByName = billingItemGroupRepository.countByParentIdIsNullAndNameAndActiveTrue(name);
        }

        if (countByName > 0) {
            throw new BadRequestException("Name is already being used.");
        }
    }

    private void checkName(Long idToBeExcluded, BillingItemGroup parent, String name) {

        long countByName;

        if (parent != null) {
            countByName = billingItemGroupRepository.countByIdNotAndParentIdAndNameAndActiveTrue(idToBeExcluded, parent.getId(), name);
        } else {
            countByName = billingItemGroupRepository.countByIdNotAndParentIdIsNullAndNameAndActiveTrue(idToBeExcluded, name);
        }

        if (countByName > 0) {
            throw new BadRequestException("Name is already being used.");
        }
    }

    private List<BillingItemGroup> findHierarchicPath(Long groupId) {

        List<BillingItemGroup> hierarchicPath = new ArrayList<>();
        BillingItemGroup group = billingItemGroupRepository.findOne(groupId);
        BillingItemGroup currentParent = group.getParent();

        while (currentParent != null) {
            hierarchicPath.add(currentParent);
            currentParent = currentParent.getParent();
        }

        Collections.reverse(hierarchicPath);
        hierarchicPath.add(group);

        return hierarchicPath;
    }

    private String convertHierarchicPathToString(List<BillingItemGroup> path) {
        StringBuilder sb = new StringBuilder();
        for (BillingItemGroup g : path) {
            sb.append("/id:" + g.getId());
        }
        return sb.toString();
    }

    private boolean isFirstGroupParentOfSecondGroup(BillingItemGroup group1, BillingItemGroup group2) {

        List<BillingItemGroup> hierarchicPath1 = findHierarchicPath(group1.getId());
        String hierarchicPath1AsString = convertHierarchicPathToString(hierarchicPath1);

        List<BillingItemGroup> hierarchicPath2 = findHierarchicPath(group2.getId());
        String hierarchicPath2AsString = convertHierarchicPathToString(hierarchicPath2);

        if (hierarchicPath2AsString.startsWith(hierarchicPath1AsString)) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public BillingItemGroup create(BillingItemGroup group) {

        if (group == null) {
            throw new BadRequestException("A billing item group must be specified.");
        }

        // parent
        BillingItemGroup parent = null;

        if (group.getParent() != null) {
            if (group.getParent().getId() == null) {
                throw new BadRequestException("A parent id must be specified.");
            }
            parent = findByIdOrThrowResourceNotFoundException(group.getParent().getId(), false);
            checkActiveness(parent);
        }

        // name
        if (StringUtils.isBlank(group.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        if (group.getName().trim().length() > 100) {
            throw new BadRequestException("Name can have at most 100 characters.");
        }

        String name = group.getName().trim().toUpperCase();

        checkName(parent, name);

        BillingItemGroup newGroup = new BillingItemGroup();
        newGroup.setParent(parent);
        newGroup.setName(name);
        newGroup.setActive(Boolean.TRUE);

        return billingItemGroupRepository.save(newGroup);
    }

    @Transactional
    public BillingItemGroup update(Long id, BillingItemGroup group) {

        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }

        BillingItemGroup originalGroup = findByIdOrThrowResourceNotFoundException(id, false);

        checkActiveness(originalGroup);

        if (group == null) {
            throw new BadRequestException("A billing item group must be specified.");
        }

        // name
        if (StringUtils.isBlank(group.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        if (group.getName().trim().length() > 100) {
            throw new BadRequestException("Name can have at most 100 characters.");
        }

        String name = group.getName().trim().toUpperCase();

        checkName(originalGroup.getId(), originalGroup.getParent(), name);

        originalGroup.setName(name);

        return billingItemGroupRepository.save(originalGroup);
    }

    @Transactional
    public BillingItemGroup move(Long id, MoveBillingItemGroupRequest request) {

        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }

        BillingItemGroup originalGroup = findByIdOrThrowResourceNotFoundException(id, false);

        checkActiveness(originalGroup);

        if (request == null) {
            throw new BadRequestException("Request details must be specified.");
        }

        BillingItemGroup newParent = null;

        if (request.getParent() != null && request.getParent().getId() != null) {
            newParent = findByIdOrThrowResourceNotFoundException(request.getParent().getId(), false);
            checkActiveness(newParent);
        }

        if (Util.isDifferent(originalGroup.getParent(), newParent)) {
            if (newParent != null && isFirstGroupParentOfSecondGroup(originalGroup, newParent)) {
                throw new BadRequestException("A group cannot be moved to a subgroup.");
            }
            checkName(newParent, originalGroup.getName());
            originalGroup.setParent(newParent);
            return billingItemGroupRepository.save(originalGroup);
        } else {
            return originalGroup;
        }
    }

    @Transactional
    public BillingItemGroup deactivate(Long id) {

        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }

        BillingItemGroup originalGroup = findByIdOrThrowResourceNotFoundException(id, false);

        checkActiveness(originalGroup);

        if (hasActiveChildren(originalGroup.getId())) {
            throw new BadRequestException("Billing item group cannot be deactivated because it has active subgroups and items.");
        }

        originalGroup.setActive(Boolean.FALSE);

        return billingItemGroupRepository.save(originalGroup);
    }

    public boolean hasActiveChildren(Long groupId) {

        long numberOfChildGroups = billingItemGroupRepository.countByParentIdAndActiveTrue(groupId);
        long numberOfChildItems = billingItemRepository.countByParentIdAndActiveTrue(groupId);

        return (numberOfChildGroups + numberOfChildItems > 0);
    }
}
