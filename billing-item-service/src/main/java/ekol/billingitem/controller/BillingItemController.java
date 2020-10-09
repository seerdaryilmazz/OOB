package ekol.billingitem.controller;

import ekol.billingitem.domain.BillingItem;
import ekol.billingitem.domain.MoveBillingItemRequest;
import ekol.billingitem.repository.BillingItemRepository;
import ekol.billingitem.service.BillingItemService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing-item")
@SuppressWarnings("SpringJavaAutowiringInspection")
public class BillingItemController {

    @Autowired
    private BillingItemRepository billingItemRepository;

    @Autowired
    private BillingItemService billingItemService;

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public List<BillingItem> findAllActiveByParent(@RequestParam(required = false) Long parentId) {

        List<BillingItem> list;

        if (parentId == null) {
            list = IterableUtils.toList(billingItemRepository.findAllByParentIdNullAndActiveTrue());
        } else {
            list = IterableUtils.toList(billingItemRepository.findAllByParentIdAndActiveTrue(parentId));
        }

        return list;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BillingItem find(@PathVariable Long id, @RequestParam(required = false) boolean findAllParents) {
        return billingItemService.findByIdOrThrowResourceNotFoundException(id, findAllParents);
    }

    @RequestMapping(value = "/active-by-code", method = RequestMethod.GET)
    public BillingItem find(@RequestParam String code, @RequestParam(required = false) boolean findAllParents) {
        return billingItemService.findActiveByCodeOrThrowResourceNotFoundException(code, findAllParents);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public BillingItem create(@RequestBody BillingItem billingItem) {
        return billingItemService.create(billingItem);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public BillingItem update(@PathVariable Long id, @RequestBody BillingItem billingItem) {
        return billingItemService.update(id, billingItem);
    }

    @RequestMapping(value = {"/{id}/move", "/{id}/move"}, method = RequestMethod.PUT)
    public BillingItem move(@PathVariable Long id, @RequestBody MoveBillingItemRequest request) {
        return billingItemService.move(id, request);
    }

    @RequestMapping(value = {"/{id}/deactivate", "/{id}/deactivate"}, method = RequestMethod.PUT)
    public BillingItem deactivate(@PathVariable Long id) {
        return billingItemService.deactivate(id);
    }
}
