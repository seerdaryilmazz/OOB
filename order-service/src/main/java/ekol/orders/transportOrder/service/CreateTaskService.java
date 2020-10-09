package ekol.orders.transportOrder.service;

import ekol.exceptions.BadRequestException;
import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.dto.*;
import ekol.orders.transportOrder.repository.TransportOrderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by kilimci on 09/09/16.
 */
@Service
public class CreateTaskService {

    @Value("${oneorder.task-service}")
    private String taskServiceName;

    @Value("${oneorder.order-service}")
    private String orderServiceName;

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Value("${oneorder.user-service}")
    private String userServiceName;

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2RestTemplate resourceRestTemplate;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    /**
     * @param orderRequest
     * @return taskId
     */
    public String createTaskForOrderEntry(TransportOrderRequest orderRequest) {
        CreateOrderTaskRequest taskRequest = createOrderTaskRequest(orderRequest);
        return resourceRestTemplate.postForObject(
                taskServiceName + "/process/OrderEntryProcess/start", taskRequest, String.class);
    }

    public void updateTaskForOrderEntry(TransportOrderRequest orderRequest) {
        CreateOrderTaskRequest taskRequest = createOrderTaskRequest(orderRequest);
        taskRequest.setTaskId(orderRequest.getTaskId());

        resourceRestTemplate.postForLocation(
                taskServiceName + "/process/OrderUpdateProcess/start", taskRequest);
    }

    private CreateOrderTaskRequest createOrderTaskRequest(TransportOrderRequest orderRequest) {
        CreateOrderTaskRequest taskRequest = CreateOrderTaskRequest.withOrderRequest(orderRequest);
        Company customer = resourceRestTemplate.getForObject(
                kartoteksServiceName + "/company/" + orderRequest.getCustomerId(), Company.class);
        taskRequest.setCustomer(customer);

        String sessionUser = userService.getSessionUsername();

        ResponseEntity<CustomerRepTeam[]> customerRepTeams = resourceRestTemplate.getForEntity(userServiceName + "/customer-rep/" + sessionUser + "/teams", CustomerRepTeam[].class);
        taskRequest.setCustomerRepTeams(Arrays.asList(customerRepTeams.getBody()));

        return taskRequest;
    }

    private String findAccountNameForNodeAndLevel(boolean needed, Long nodeId, Long level) {
        if (needed) {
            Node[] users = resourceRestTemplate.getForObject("http://authorization-service/auth/user/byNodeIdAndLevel/" + nodeId + "/" + level, Node[].class);
            if (users == null || users.length == 0 || StringUtils.isBlank(users[0].getName())) {
                throw new BadRequestException("Can not find level " + level + " for " + nodeId + " for approval");
            } else {
                return users[0].getName();
            }
        } else {
            return null;
        }
    }

    public String createTaskForOrderApproval(Long transportOrderId, boolean supervisorApprovalNeeded, boolean managerApprovalNeeded) {
        TaskTemplate taskTemplate = resourceRestTemplate.getForObject(taskServiceName + "/taskTemplate/findByName/OrderApprovalTask", TaskTemplate.class);
        if (taskTemplate == null) {
            throw new BadRequestException("Can not find task template");
        }

        Long nodeId = null;
        if (taskTemplate.getParams() == null || !taskTemplate.getParams().containsKey("nodeId") || taskTemplate.getParams().get("nodeId") == null) {
            throw new BadRequestException("Can not find user node");
        }

        nodeId = Long.parseLong(taskTemplate.getParams().get("nodeId").toString());

        TransportOrder transportOrder = transportOrderRepository.findWithDetailsById(transportOrderId);
        if (transportOrder == null) {
            throw new BadRequestException("Can not find transport order");
        }

        OrderApprovalTaskRequest orderApprovalTaskRequest =
                new OrderApprovalTaskRequest(
                        transportOrder,
                        supervisorApprovalNeeded,
                        managerApprovalNeeded,
                        findAccountNameForNodeAndLevel(supervisorApprovalNeeded, nodeId, 200l),
                        findAccountNameForNodeAndLevel(managerApprovalNeeded, nodeId, 300l));

        return resourceRestTemplate.postForObject(taskServiceName + "/process/OrderApprovalProcess/start", orderApprovalTaskRequest, String.class);
    }
}
