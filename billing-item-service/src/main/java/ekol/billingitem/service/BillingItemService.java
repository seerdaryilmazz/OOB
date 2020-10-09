package ekol.billingitem.service;

import ekol.billingitem.domain.BillingItem;
import ekol.billingitem.domain.BillingItemGroup;
import ekol.billingitem.domain.BillingItemType;
import ekol.billingitem.domain.MoveBillingItemRequest;
import ekol.billingitem.repository.BillingItemRepository;
import ekol.billingitem.util.Util;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("SpringJavaAutowiringInspection")
public class BillingItemService {

    @Autowired
    private BillingItemRepository billingItemRepository;

    @Autowired
    private BillingItemGroupService billingItemGroupService;

    public BillingItem findByIdOrThrowResourceNotFoundException(Long id, boolean findAllParents) {

        BillingItem persistedEntity = billingItemRepository.findOne(id);

        if (persistedEntity != null) {
            if (findAllParents && persistedEntity.getParent() != null) {
                persistedEntity.setParent(billingItemGroupService.findByIdOrThrowResourceNotFoundException(persistedEntity.getParent().getId(), true));
            }
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Billing item cannot be found. id: {0}", id);
        }
    }

    public BillingItem findActiveByCodeOrThrowResourceNotFoundException(String code, boolean findAllParents) {

        BillingItem persistedEntity = billingItemRepository.findByCodeAndActiveTrue(code);

        if (persistedEntity != null) {
            if (findAllParents && persistedEntity.getParent() != null) {
                persistedEntity.setParent(billingItemGroupService.findByIdOrThrowResourceNotFoundException(persistedEntity.getParent().getId(), true));
            }
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Billing item cannot be found. code: {0}", code);
        }
    }

    public void checkActiveness(BillingItem billingItem) {
        if (!billingItem.getActive()) {
            throw new BadRequestException("Billing item is not active. id: {0}", billingItem.getId());
        }
    }

    private void checkCode(String code) {
        if (billingItemRepository.countByCodeAndActiveTrue(code) > 0) {
            throw new BadRequestException("Code is already being used.");
        }
    }

    private void checkCode(Long idToBeExcluded, String code) {
        if (billingItemRepository.countByIdNotAndCodeAndActiveTrue(idToBeExcluded, code) > 0) {
            throw new BadRequestException("Code is already being used.");
        }
    }

    private void checkName(BillingItemGroup parent, String name) {

        long countByName;

        if (parent != null) {
            countByName = billingItemRepository.countByParentIdAndNameAndActiveTrue(parent.getId(), name);
        } else {
            countByName = billingItemRepository.countByParentIdIsNullAndNameAndActiveTrue(name);
        }

        if (countByName > 0) {
            throw new BadRequestException("Name is already being used.");
        }
    }

    private void checkName(Long idToBeExcluded, BillingItemGroup parent, String name) {

        long countByName;

        if (parent != null) {
            countByName = billingItemRepository.countByIdNotAndParentIdAndNameAndActiveTrue(idToBeExcluded, parent.getId(), name);
        } else {
            countByName = billingItemRepository.countByIdNotAndParentIdIsNullAndNameAndActiveTrue(idToBeExcluded, name);
        }

        if (countByName > 0) {
            throw new BadRequestException("Name is already being used.");
        }
    }

    @Transactional
    public BillingItem create(BillingItem billingItem) {

        if (billingItem == null) {
            throw new BadRequestException("A billing item must be specified.");
        }

        BillingItemGroup parent = null;

        if (billingItem.getParent() != null) {
            if (billingItem.getParent().getId() == null) {
                throw new BadRequestException("A parent id must be specified.");
            }
            parent = billingItemGroupService.findByIdOrThrowResourceNotFoundException(billingItem.getParent().getId(), false);
            billingItemGroupService.checkActiveness(parent);
        }

        // code
        if (StringUtils.isBlank(billingItem.getCode())) {
            throw new BadRequestException("A code must be specified.");
        }

        if (!billingItem.getCode().matches("[0-9]{4}")) {
            throw new BadRequestException("Code must consist of 4 digits.");
        }

        String code = billingItem.getCode();

        // officialCode'un formatını henüz tam olarak bilmiyoruz, şimdilik aşağıdaki gibi bir varsayım yaptık.
        if (StringUtils.isNotEmpty(billingItem.getOfficialCode()) && !billingItem.getOfficialCode().matches("[0-9]{2,6}")) {
            throw new BadRequestException("Official code must consist of minimum 2, maximum 6 digits.");
        }

        String officialCode = billingItem.getOfficialCode();

        // name
        if (StringUtils.isBlank(billingItem.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        if (billingItem.getName().trim().length() > 100) {
            throw new BadRequestException("Name can have at most 100 characters.");
        }

        String name = billingItem.getName().trim().toUpperCase();

        // type
        if (billingItem.getType() == null) {
            throw new BadRequestException("A type must be specified.");
        }

        BillingItemType type = billingItem.getType();

        checkCode(code);
        checkName(parent, name);

        BillingItem newBillingItem = new BillingItem();
        newBillingItem.setParent(parent);
        newBillingItem.setCode(code);
        newBillingItem.setOfficialCode(officialCode);
        newBillingItem.setName(name);
        newBillingItem.setType(type);
        newBillingItem.setActive(Boolean.TRUE);

        return billingItemRepository.save(newBillingItem);
    }

    @Transactional
    public BillingItem update(Long id, BillingItem billingItem) {

        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }

        BillingItem originalBillingItem = findByIdOrThrowResourceNotFoundException(id, false);

        checkActiveness(originalBillingItem);

        if (billingItem == null) {
            throw new BadRequestException("A billing item must be specified.");
        }

        // code
        if (StringUtils.isBlank(billingItem.getCode())) {
            throw new BadRequestException("A code must be specified.");
        }

        if (!billingItem.getCode().matches("[0-9]{4}")) {
            throw new BadRequestException("Code must consist of 4 digits.");
        }

        String code = billingItem.getCode();

        // officialCode'un formatını henüz tam olarak bilmiyoruz, şimdilik aşağıdaki gibi bir varsayım yaptık.
        if (StringUtils.isNotEmpty(billingItem.getOfficialCode()) && !billingItem.getOfficialCode().matches("[0-9]{2,6}")) {
            throw new BadRequestException("Official code must consist of minimum 2, maximum 6 digits.");
        }

        String officialCode = billingItem.getOfficialCode();

        // name
        if (StringUtils.isBlank(billingItem.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        if (billingItem.getName().trim().length() > 100) {
            throw new BadRequestException("Name can have at most 100 characters.");
        }

        String name = billingItem.getName().trim().toUpperCase();

        // type
        if (billingItem.getType() == null) {
            throw new BadRequestException("A type must be specified.");
        }

        BillingItemType type = billingItem.getType();

        checkCode(originalBillingItem.getId(), code);
        checkName(originalBillingItem.getId(), originalBillingItem.getParent(), name);

        originalBillingItem.setCode(code);
        originalBillingItem.setOfficialCode(officialCode);
        originalBillingItem.setName(name);
        originalBillingItem.setType(type);

        return billingItemRepository.save(originalBillingItem);
    }

    @Transactional
    public BillingItem move(Long id, MoveBillingItemRequest request) {

        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }

        BillingItem originalBillingItem = findByIdOrThrowResourceNotFoundException(id, false);

        checkActiveness(originalBillingItem);

        if (request == null) {
            throw new BadRequestException("Request details must be specified.");
        }

        BillingItemGroup newParent = null;

        if (request.getParent() != null && request.getParent().getId() != null) {
            newParent = billingItemGroupService.findByIdOrThrowResourceNotFoundException(request.getParent().getId(), false);
            billingItemGroupService.checkActiveness(newParent);
        }

        if (Util.isDifferent(originalBillingItem.getParent(), newParent)) {
            checkName(newParent, originalBillingItem.getName());
            originalBillingItem.setParent(newParent);
            return billingItemRepository.save(originalBillingItem);
        } else {
            return originalBillingItem;
        }
    }

    @Transactional
    public BillingItem deactivate(Long id) {

        if (id == null) {
            throw new BadRequestException("An id must be specified.");
        }

        BillingItem originalBillingItem = findByIdOrThrowResourceNotFoundException(id, false);

        checkActiveness(originalBillingItem);

        originalBillingItem.setActive(Boolean.FALSE);

        return billingItemRepository.save(originalBillingItem);
    }
}
