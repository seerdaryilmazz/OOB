package ekol.orders.order.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import ekol.orders.lookup.builder.DocumentTypeBuilder;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.order.builder.DocumentBuilder;
import ekol.orders.order.builder.OrderBuilder;
import ekol.orders.order.builder.OrderDocumentBuilder;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.OrderDocument;
import ekol.orders.order.repository.OrderDocumentRepository;
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
public class OrderDocumentServiceUnitTest {

    @MockBean
    private OrderDocumentRepository orderDocumentRepository;
    @MockBean
    private DocumentTypeRepository documentTypeRepository;
    @MockBean
    private DocumentFileService documentFileService;
    @MockBean
    private ListOrderService listOrderService;
    @MockBean
    private DocumentValidator documentValidator;

    @Captor
    private ArgumentCaptor<OrderDocument> orderDocumentCaptor;


    private OrderDocumentService orderDocumentService;

    private Document getDocument1(){
        return DocumentBuilder.aDocument().withSavedFileName("saved-file-1").withOriginalFileName("org-file-1")
                .withType(DocumentTypeBuilder.aDocumentType().withId(10L).build())
                .build();
    }
    private Document getDocument2(){
        return DocumentBuilder.aDocument().withSavedFileName("saved-file-2").withOriginalFileName("org-file-2")
                .withType(DocumentTypeBuilder.aDocumentType().withId(20L).build())
                .build();
    }


    @Before
    public void init() {
        this.orderDocumentService = new OrderDocumentService(orderDocumentRepository,
                documentTypeRepository,
                documentFileService,
                listOrderService,
                documentValidator);
    }

    @Test
    public void givenOrderId_whenGetDocuments_thenListDocumentsOfOrder() {
        long orderId = 1L;

        given(orderDocumentRepository.findByOrderId(orderId)).willReturn(Arrays.asList(
                OrderDocumentBuilder.anOrderDocument().withDocument(getDocument1()).withId(1L).withOrder(OrderBuilder.anOrder().build()).build(),
                OrderDocumentBuilder.anOrderDocument().withDocument(getDocument2()).withId(1L).withOrder(OrderBuilder.anOrder().build()).build()
        ));

        List<OrderDocument> response = orderDocumentService.getDocuments(orderId);

        assertEquals(2, response.size());
        assertEquals("saved-file-1", response.get(0).getDocument().getSavedFileName());
        assertEquals("saved-file-2", response.get(1).getDocument().getSavedFileName());
    }

    @Test
    public void givenValidDocumentId_whenGetDocument_thenGetDocument() {
        Long documentId = 1L;

        given(orderDocumentRepository.findById(documentId)).willReturn(
                Optional.of(OrderDocumentBuilder.anOrderDocument().withId(1L).withDocument(getDocument1()).build()));

        OrderDocument response = orderDocumentService.getByIdOrThrowException(documentId);

        assertEquals(documentId, response.getId());
        assertEquals("saved-file-1", response.getDocument().getSavedFileName());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenInvalidDocumentId_whenGetDocument_thenThrowException() {
        Long documentId = 1L;

        given(orderDocumentRepository.findById(documentId)).willReturn(Optional.empty());

        orderDocumentService.getByIdOrThrowException(documentId);

    }

    public void givenValidDocument_whenDeleteDocument_thenSetDocumentDeletedAndRemoveFile() {
        OrderDocument document = OrderDocumentBuilder.anOrderDocument().withId(1L)
                .withDocument(getDocument1()).withOrder(OrderBuilder.anOrder().build())
                .build();

        given(orderDocumentRepository.findById(1L)).willReturn(Optional.of(document));

        orderDocumentService.deleteDocument(1L);

        then(orderDocumentRepository).should(times(1)).save(orderDocumentCaptor.capture());
        then(documentFileService).should(times(1)).removeDocument(document.getDocument());

        assertTrue(orderDocumentCaptor.getValue().isDeleted());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenInvalidDocument_whenDeleteDocument_thenThrowException() {

        given(orderDocumentRepository.findById(1L)).willReturn(Optional.empty());

        orderDocumentService.deleteDocument(1L);

        then(orderDocumentRepository).should(never()).save(any(OrderDocument.class));
        then(documentFileService).should(never()).removeDocument(any(Document.class));
    }

    @Test
    public void givenValidDocument_whenSaveDocument_thenSaveRepositoryAndMoveFileToPermSpace() {
        Document document1 = getDocument1();
        Document document2 = getDocument2();

        Long orderId = 1L;
        List<OrderDocument> documents = Arrays.asList(
                OrderDocumentBuilder.anOrderDocument().withDocument(document1).build(),
                OrderDocumentBuilder.anOrderDocument().withDocument(document2).build()
        );

        given(listOrderService.findByIdOrThrowException(orderId)).willReturn(OrderBuilder.anOrder().build());
        given(documentTypeRepository.findById(document1.getType().getId())).willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().build()));
        given(documentTypeRepository.findById(document2.getType().getId())).willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().build()));

        given(orderDocumentRepository.save(documents)).willAnswer((m) -> {
            List<OrderDocument> input = (List<OrderDocument>)m.getArguments()[0];
            input.forEach(orderDocument -> orderDocument.setId(new Random().nextLong()));
            return input;
        });

        orderDocumentService.saveDocument(orderId, documents);

        then(documentFileService).should(times(1)).moveDocumentsToPermanentSpace(Arrays.asList(document1, document2));
        then(documentValidator).should(times(1)).validate(document1);
        then(documentValidator).should(times(1)).validate(document2);
        then(orderDocumentRepository).should(times(1)).save(documents);

    }

    @Test(expected = ValidationException.class)
    public void givenDocumentValidatorFails_whenSaveDocument_thenThrowException() {
        Document document1 = getDocument1();
        Document document2 = getDocument2();

        Long orderId = 1L;
        List<OrderDocument> documents = Arrays.asList(
                OrderDocumentBuilder.anOrderDocument().withDocument(document1).build(),
                OrderDocumentBuilder.anOrderDocument().withDocument(document2).build()
        );

        willThrow(new ValidationException("eori exception")).given(documentValidator).validate(document2);
        given(documentTypeRepository.findById(document1.getType().getId())).willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().build()));
        given(documentTypeRepository.findById(document2.getType().getId())).willReturn(Optional.of(DocumentTypeBuilder.aDocumentType().build()));


        orderDocumentService.saveDocument(orderId, documents);

        then(orderDocumentRepository).should(never()).save(any(OrderDocument.class));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void givenOrderNotFound_whenSaveDocument_thenThrowException() {
        Document document1 = getDocument1();
        Document document2 = getDocument2();

        Long orderId = 1L;
        List<OrderDocument> documents = Arrays.asList(
                OrderDocumentBuilder.anOrderDocument().withDocument(document1).build(),
                OrderDocumentBuilder.anOrderDocument().withDocument(document2).build()
        );

        willThrow(new ResourceNotFoundException("not found")).given(listOrderService).findByIdOrThrowException(orderId);

        orderDocumentService.saveDocument(orderId, documents);

        then(orderDocumentRepository).should(never()).save(any(OrderDocument.class));

    }


}
