package ekol.billingitem.controller;

import ekol.billingitem.domain.BillingItemGroup;
import ekol.billingitem.domain.MoveBillingItemGroupRequest;
import ekol.billingitem.repository.BillingItemGroupRepository;
import ekol.billingitem.service.BillingItemGroupService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing-item-group")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class BillingItemGroupController {

    @Autowired
    private BillingItemGroupRepository billingItemGroupRepository;

    @Autowired
    private BillingItemGroupService billingItemGroupService;

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public List<BillingItemGroup> findAllActiveByParent(@RequestParam(required = false) Long parentId) {

        List<BillingItemGroup> list;

        if (parentId == null) {
            list = IterableUtils.toList(billingItemGroupRepository.findAllByParentIdNullAndActiveTrue());
        } else {
            list = IterableUtils.toList(billingItemGroupRepository.findAllByParentIdAndActiveTrue(parentId));
        }

        for (BillingItemGroup group : list) {
            group.setHasChildren(billingItemGroupService.hasActiveChildren(group.getId()));
        }

        return list;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BillingItemGroup find(@PathVariable Long id, @RequestParam boolean findAllParents) {
        return billingItemGroupService.findByIdOrThrowResourceNotFoundException(id, findAllParents);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public BillingItemGroup create(@RequestBody BillingItemGroup group) {
        return billingItemGroupService.create(group);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public BillingItemGroup update(@PathVariable Long id, @RequestBody BillingItemGroup group) {
        return billingItemGroupService.update(id, group);
    }

    @RequestMapping(value = {"/{id}/move", "/{id}/move"}, method = RequestMethod.PUT)
    public BillingItemGroup move(@PathVariable Long id, @RequestBody MoveBillingItemGroupRequest request) {
        return billingItemGroupService.move(id, request);
    }

    @RequestMapping(value = {"/{id}/deactivate", "/{id}/deactivate"}, method = RequestMethod.PUT)
    public BillingItemGroup deactivate(@PathVariable Long id) {
        return billingItemGroupService.deactivate(id);
    }
}
