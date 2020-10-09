package ekol.orders.transportOrder.controller;

import ekol.orders.transportOrder.service.CreateTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 14/02/2017.
 */
@RestController
@RequestMapping("/orderApproval")
public class OrderApprovalController {

    @Autowired
    private CreateTaskService createTaskService;

    @RequestMapping("createTask/{transportOrderId}/{supervisorApprovalNeeded}/{managerApprovalNeeded}")
    public String createTask(@PathVariable Long transportOrderId, @PathVariable boolean supervisorApprovalNeeded, @PathVariable boolean managerApprovalNeeded) {
        return createTaskService.createTaskForOrderApproval(transportOrderId, supervisorApprovalNeeded, managerApprovalNeeded);
    }
}
