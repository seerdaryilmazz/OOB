package ekol.orders;


import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.repository.TransportOrderRequestDocumentRepository;
import ekol.orders.transportOrder.repository.TransportOrderRequestRepository;
import ekol.orders.transportOrder.service.CreateTaskService;
import ekol.orders.transportOrder.service.TransportOrderRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

/**
 * Created by kilimci on 25/07/16.
 */
@RunWith(SpringRunner.class)
public class TransportOrderRequestServiceTest {

    @InjectMocks
    private TransportOrderRequestService transportOrderRequestService;

    @Mock
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Mock
    private TransportOrderRequestDocumentRepository transportOrderRequestDocumentRepository;

    @Mock
    private OAuth2RestTemplate resourceRestTemplate;

    @Mock
    private CreateTaskService createTaskService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveTransportOrderRequest() {
        TransportOrderRequest request = new TransportOrderRequest();
        request.setId(1L);
        Company c = new Company();
        c.setName("asdasd");
        request.setCustomer(c);
        when(transportOrderRequestRepository.save(request)).thenReturn(request);
        transportOrderRequestService.createNewRequest(request);
        verify(transportOrderRequestRepository, times(2)).save(request);
    }
}
