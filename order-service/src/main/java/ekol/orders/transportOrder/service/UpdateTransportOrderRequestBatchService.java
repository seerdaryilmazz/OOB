package ekol.orders.transportOrder.service;


import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.repository.TransportOrderRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kilimci on 07/10/16.
 */
@Service
public class UpdateTransportOrderRequestBatchService {

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private CreateTaskService createTaskService;

    @Scheduled(cron = "0 0 * * * *")
    public void processPendingTransportOrderRequests(){
        List<TransportOrderRequest> pendingRequests = transportOrderRequestRepository.findByOrderIsNull();
        pendingRequests.forEach(request ->
            createTaskService.updateTaskForOrderEntry(request)
        );
    }
}
