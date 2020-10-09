package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.lookup.repository.IncotermRepository;
import ekol.orders.testdata.SomeData;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class TransportOrderRequestRepositoryIntegrationTest {

    @Autowired
    private IncotermRepository incotermRepository;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private TransportOrderRequestDocumentRepository transportOrderRequestDocumentRepository;

    private static boolean isInList(TransportOrderRequest request, List<TransportOrderRequest> list) {

        boolean isInList = false;

        for (TransportOrderRequest r : list) {
            if (r.getId().equals(request.getId())) {
                isInList = true;
                break;
            }
        }

        return isInList;
    }

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                transportOrderRequestDocumentRepository,
                documentTypeRepository,
                transportOrderRequestRepository,
                transportOrderRepository,
                incotermRepository);
    }

    @Before
    public void runBeforeEveryTest() {
        cleanup();
    }

    @After
    public void runAfterEveryTest() {
        cleanup();
    }

    @Test
    public void saveMustWorkCorrectly() {

        TransportOrderRequest persistedEntity = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }

    @Test
    public void findWithDetailsByIdMustWorkCorrectly() {

        TransportOrderRequest persistedRequest = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());

        DocumentType persistedDocumentType = documentTypeRepository.save(SomeData.someDocumentType().build());

        transportOrderRequestDocumentRepository.save(SomeData.someTransportOrderRequestDocument(persistedRequest, persistedDocumentType).build());

        Assert.assertTrue("'find with documents by id' does not work correctly.",
                transportOrderRequestRepository.findWithDetailsById(persistedRequest.getId()).getDocuments().size() > 0);
    }

    @Test
    public void findByOrderIsNullMustWorkCorrectly() {

        Incoterm persistedIncoterm = incotermRepository.save(SomeData.someIncoterm().build());

        TransportOrder persistedTransportOrder = transportOrderRepository.save(SomeData.someTransportOrder(null, persistedIncoterm).build());

        TransportOrderRequest persistedRequest1 = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());
        TransportOrderRequest persistedRequest2 = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(persistedTransportOrder).build());
        TransportOrderRequest persistedRequest3 = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());

        List<TransportOrderRequest> list = transportOrderRequestRepository.findByOrderIsNull();

        Assert.assertNotNull("'find by order is null' does not work correctly.", list);
        Assert.assertTrue("'find by order is null' does not work correctly.", list.size() == 2);
        Assert.assertTrue("'find by order is null' does not work correctly.", isInList(persistedRequest1, list));
        Assert.assertTrue("'find by order is null' does not work correctly.", isInList(persistedRequest2, list) == false);
        Assert.assertTrue("'find by order is null' does not work correctly.", isInList(persistedRequest3, list));
    }
}
