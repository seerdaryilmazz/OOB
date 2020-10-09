package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.testdata.SomeData;
import ekol.orders.transportOrder.domain.TransportOrderRequest;
import ekol.orders.transportOrder.domain.TransportOrderRequestDocument;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class TransportOrderRequestDocumentRepositoryIntegrationTest {

    @Autowired
    private TransportOrderRequestDocumentRepository transportOrderRequestDocumentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                transportOrderRequestDocumentRepository,
                documentTypeRepository,
                transportOrderRequestRepository);
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

        TransportOrderRequest persistedRequest = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());

        DocumentType persistedDocumentType = documentTypeRepository.save(SomeData.someDocumentType().build());

        TransportOrderRequestDocument persistedEntity =
                transportOrderRequestDocumentRepository.save(
                        SomeData.someTransportOrderRequestDocument(persistedRequest, persistedDocumentType).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }
}
