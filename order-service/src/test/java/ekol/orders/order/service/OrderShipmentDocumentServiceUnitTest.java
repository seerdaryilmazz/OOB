package ekol.orders.order.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import ekol.orders.lookup.builder.DocumentTypeBuilder;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.order.builder.DocumentBuilder;
import ekol.orders.order.builder.OrderShipmentBuilder;
import ekol.orders.order.builder.OrderShipmentDocumentBuilder;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.OrderShipmentDocument;
import ekol.orders.order.repository.OrderShipmentDocumentRepository;
import ekol.orders.order.validator.DocumentValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class OrderShipmentDocumentServiceUnitTest {

    @MockBean
    private OrderShipmentDocumentRepository orderShipmentDocumentRepository;
    @MockBean
    private DocumentTypeRepository documentTypeRepository;
    @MockBean
    private DocumentFileService documentFileService;
    @MockBean
    private ListOrderService listOrderService;
    @MockBean
    private DocumentValidator documentValidator;

    @Captor
    private ArgumentCaptor<OrderShipmentDocument> orderShipmentDocumentCaptor;


    private OrderShipmentDocumentService orderShipmentDocumentService;

    private Document getDocument1(){
        return DocumentBuilder.aDocument().withSavedFileName("saved-file-1").withOriginalFileName("org-file-1")
                .withType(DocumentTypeBuilder.aDocumentType().withDocumentGroup(DocumentTypeGroup.ORDER).withId(10L).build())
                .build();
    }
    private Document getDocument2(){
        return DocumentBuilder.aDocument().withSavedFileName("saved-file-2").withOriginalFileName("org-file-2")
                .withType(DocumentTypeBuilder.aDocumentType().withDocumentGroup(DocumentTypeGroup.ORDER).withId(20L).build())
                .build();
    }


    @Before
    public void init() {
        this.orderShipmentDocumentService = new OrderShipmentDocumentService(orderShipmentDocumentRepository,
                documentTypeRepository,
                documentFileService,
                listOrderService,
                documentValidator);
    }

    @Test
    public void givenShipmentId_whenGetDocuments_thenListDocumentsOfShipment() {
        long shipmentId = 1L;

        given(orderShipmentDocumentRepository.findByShipmentId(shipmentId)).willReturn(Arrays.asList(
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(getDocument1()).withId(1L)
                        .withShipment(OrderShipmentBuilder.anOrderShipment().build()).build(),
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(getDocument2()).withId(1L)
                        .withShipment(OrderShipmentBuilder.anOrderShipment().build()).build()
        ));

        List<OrderShipmentDocument> response = orderShipmentDocumentService.getDocuments(shipmentId);

        assertEquals(2, response.size());
        assertEquals("saved-file-1", response.get(0).getDocument().getSavedFileName());
        assertEquals("saved-file-2", response.get(1).getDocument().getSavedFileName());
    }

    @Test
    public void givenValidDocumentId_whenGetDocument_thenGetDocument() {
        Long documentId = 1L;

        given(orderShipmentDocumentRepository.findById(documentId)).willReturn(
                Optional.of(OrderShipmentDocumentBuilder.anOrderShipmentDocument().withId(1L).withDocument(getDocument1()).build()));

        OrderShipmentDocument response = orderShipmentDocumentService.getByIdOrThrowException(documentId);

        assertEquals(documentId, response.getId());
        assertEquals("saved-file-1", response.getDocument().getSavedFileName());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenInvalidDocumentId_whenGetDocument_thenThrowException() {
        Long documentId = 1L;

        given(orderShipmentDocumentRepository.findById(documentId)).willReturn(Optional.empty());

        orderShipmentDocumentService.getByIdOrThrowException(documentId);

    }

    public void givenValidDocument_whenDeleteDocument_thenSetDocumentDeletedAndRemoveFile() {
        OrderShipmentDocument document = OrderShipmentDocumentBuilder.anOrderShipmentDocument().withId(1L)
                .withDocument(getDocument1()).withShipment(OrderShipmentBuilder.anOrderShipment().build())
                .build();

        given(orderShipmentDocumentRepository.findById(1L)).willReturn(Optional.of(document));

        orderShipmentDocumentService.deleteDocument(1L);

        then(orderShipmentDocumentRepository).should(times(1)).save(orderShipmentDocumentCaptor.capture());
        then(documentFileService).should(times(1)).removeDocument(document.getDocument());

        assertTrue(orderShipmentDocumentCaptor.getValue().isDeleted());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenInvalidDocument_whenDeleteDocument_thenThrowException() {

        given(orderShipmentDocumentRepository.findById(1L)).willReturn(Optional.empty());

        orderShipmentDocumentService.deleteDocument(1L);

        then(orderShipmentDocumentRepository).should(never()).save(any(OrderShipmentDocument.class));
        then(documentFileService).should(never()).removeDocument(any(Document.class));
    }

    @Test
    public void givenValidDocument_whenSaveDocument_thenSaveRepositoryAndMoveFileToPermSpace() {
        Document document1 = getDocument1();
        Document document2 = getDocument2();

        Long shipmentId = 1L;
        List<OrderShipmentDocument> documents = Arrays.asList(
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(document1).build(),
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(document2).build()
        );

        given(listOrderService.findShipmentByIdOrThrowException(shipmentId)).willReturn(OrderShipmentBuilder.anOrderShipment().build());
        given(documentTypeRepository.findById(document1.getType().getId()))
                .willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().withDocumentGroup(DocumentTypeGroup.ORDER).build()));
        given(documentTypeRepository.findById(document2.getType().getId()))
                .willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().withDocumentGroup(DocumentTypeGroup.ORDER).build()));

        given(orderShipmentDocumentRepository.save(documents)).willAnswer((m) -> {
            List<OrderShipmentDocument> input = (List<OrderShipmentDocument>)m.getArguments()[0];
            input.forEach(orderDocument -> orderDocument.setId(new Random().nextLong()));
            return input;
        });

        orderShipmentDocumentService.saveDocument(shipmentId, documents);

        then(documentFileService).should(times(1)).moveDocumentsToPermanentSpace(Arrays.asList(document1, document2));
        then(documentValidator).should(times(1)).validate(document1);
        then(documentValidator).should(times(1)).validate(document2);
        then(orderShipmentDocumentRepository).should(times(1)).save(documents);

    }

    @Test(expected = ValidationException.class)
    public void givenDocumentValidatorFails_whenSaveDocument_thenThrowException() {
        Document document1 = getDocument1();
        Document document2 = getDocument2();

        Long orderId = 1L;
        List<OrderShipmentDocument> documents = Arrays.asList(
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(document1).build(),
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(document2).build()
        );

        willThrow(new ValidationException("eori exception")).given(documentValidator).validate(document2);
        given(documentTypeRepository.findById(document1.getType().getId()))
                .willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().withDocumentGroup(DocumentTypeGroup.ORDER).build()));
        given(documentTypeRepository.findById(document2.getType().getId()))
                .willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().withDocumentGroup(DocumentTypeGroup.ORDER).build()));


        orderShipmentDocumentService.saveDocument(orderId, documents);

        then(orderShipmentDocumentRepository).should(never()).save(any(OrderShipmentDocument.class));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenShipmentNotFound_whenSaveDocument_thenThrowException() {
        Document document1 = getDocument1();
        Document document2 = getDocument2();

        Long shipmentId = 1L;
        List<OrderShipmentDocument> documents = Arrays.asList(
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(document1).build(),
                OrderShipmentDocumentBuilder.anOrderShipmentDocument().withDocument(document2).build()
        );

        willThrow(new ResourceNotFoundException("not found")).given(listOrderService).findShipmentByIdOrThrowException(shipmentId);

        orderShipmentDocumentService.saveDocument(shipmentId, documents);

        then(orderShipmentDocumentRepository).should(never()).save(any(OrderShipmentDocument.class));

    }
}
