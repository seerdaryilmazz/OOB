package ekol.orders.transportOrder.controller;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.model.User;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.repository.TransportOrderRequestRepository;
import ekol.orders.transportOrder.service.TransportOrderRequestService;
import ekol.orders.transportOrder.service.TransportOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transport-order-req")
public class TransportOrderRequestApiController {

    private static final String ERROR_MESSAGE = "Resource with id {0} not found";

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Value("${oneorder.user-service}")
    private String userServiceName;

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private TransportOrderRequestService transportOrderRequestService;

    @Autowired
    private TransportOrderService transportOrderService;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public TransportOrderRequest addRequest(@RequestBody TransportOrderRequest request) {

        transportOrderRequestService.retrieveExternalFields(request, true, true, false);

        User currentUser = getCurrentUser();
        request.setCreatedById(currentUser.getId());
        request.setCreatedBy(currentUser);

        return transportOrderRequestService.createNewRequest(request);
    }

    @RequestMapping(value = "/{requestId}", method = RequestMethod.GET)
    public TransportOrderRequest findRequest(@PathVariable Long requestId) {
        return transportOrderRequestService.findRequestWithDetailsById(requestId);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<TransportOrderRequest> getAllOrderRequests() {
        return transportOrderRequestService.getAllOrderRequests(true, true, true);
    }

    @RequestMapping(value = "/by-order-id", method = RequestMethod.GET)
    public TransportOrderRequest findRequestByOrderId(@RequestParam("orderId") Long orderId) {
        return transportOrderRequestService.findRequestWithDetailsByOrderId(orderId);
    }

    @RequestMapping(value = "/current-user-last-10", method = RequestMethod.GET)
    public List<TransportOrderRequest> getLast10OrderRequestsCreatedByCurrentUser() {
        User user = getCurrentUser();
        return transportOrderRequestService.getLast10OrderRequestsCreatedBy(user.getId(), true, true, true);
    }

    @RequestMapping(value = "/{requestId}/confirm", method = RequestMethod.POST)
    public void confirmOrderRequest(@PathVariable Long requestId, @RequestParam String readyAtDate) {
        TransportOrderRequest request = transportOrderRequestRepository.findOne(requestId);
        if(request == null){
            throw new ResourceNotFoundException("Order request not found");
        }
        transportOrderService.confirm(request.getOrder(), new FixedZoneDateTime(readyAtDate));
    }

    @RequestMapping(value = "/{id}/assignTask", method = RequestMethod.POST)
    public void assignTask(@PathVariable Long id, @RequestParam String taskId) {
        TransportOrderRequest request = transportOrderRequestService.findRequestById(id);
        if (request == null) {
            throw new ResourceNotFoundException(ERROR_MESSAGE, id);
        }
        request.setTaskId(taskId);
        transportOrderRequestService.updateRequest(request);
    }

    @RequestMapping(value = "/{requestId}/get-just-order-id", method = RequestMethod.GET)
    public Long getJustOrderId(@PathVariable Long requestId) {

        TransportOrderRequest request = transportOrderRequestRepository.findOne(requestId);

        if (request == null) {
            throw new ResourceNotFoundException("TransportOrderRequest with specified id is not found: " + requestId);
        }

        if (request.getOrder() == null) {
            throw new BadRequestException("TransportOrderRequest with specified id does not have an order: " + requestId);
        }

        return request.getOrder().getId();
    }

    private User getCurrentUser() {
        return oAuth2RestTemplate.getForObject(userServiceName + "/users/current", User.class);
    }
}
