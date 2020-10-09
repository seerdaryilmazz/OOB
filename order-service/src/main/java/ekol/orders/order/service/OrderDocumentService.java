package ekol.orders.order.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderDocument;
import ekol.orders.order.repository.OrderDocumentRepository;
import ekol.orders.order.validator.DocumentValidator;

@Service
public class OrderDocumentService {


    private OrderDocumentRepository orderDocumentRepository;
    private DocumentTypeRepository documentTypeRepository;
    private DocumentFileService documentFileService;
    private ListOrderService listOrderService;
    private DocumentValidator documentValidator;

    @Autowired
    public OrderDocumentService(OrderDocumentRepository orderDocumentRepository,
                                DocumentTypeRepository documentTypeRepository,
                                DocumentFileService documentFileService,
                                ListOrderService listOrderService,
                                DocumentValidator documentValidator){
        this.orderDocumentRepository = orderDocumentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentFileService = documentFileService;
        this.listOrderService = listOrderService;
        this.documentValidator = documentValidator;
    }

    public List<OrderDocument> getDocuments(Long orderId){
        return orderDocumentRepository.findByOrderId(orderId);
    }

    public OrderDocument getByIdOrThrowException(Long id){
        return orderDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document with id {0} not found", String.valueOf(id)));
    }

    public void deleteDocument(Long documentId){
        OrderDocument document = getByIdOrThrowException(documentId);
        document.setDeleted(true);

        orderDocumentRepository.save(document);
        if(getDocuments(document.getOrder().getId()).stream().noneMatch(t->!Objects.equals(t.getId(), documentId) &&  Objects.equals(document.getDocument().getSavedFileName(), t.getDocument().getSavedFileName() ))) {
        	documentFileService.removeDocument(document.getDocument());
        }

    }

    public List<OrderDocument> saveDocument(Long orderId, List<OrderDocument> orderDocuments) {
        Order order = listOrderService.findByIdOrThrowException(orderId);

        documentFileService.moveDocumentsToPermanentSpace(orderDocuments.stream().map(OrderDocument::getDocument).collect(toList()));
        orderDocuments.forEach(orderDocument -> {
            documentValidator.validate(orderDocument.getDocument());
            documentTypeRepository.findById(orderDocument.getDocument().getType().getId())
                    .ifPresent(documentType -> orderDocument.getDocument().setType(documentType));
            orderDocument.setOrder(order);
        });
        List<OrderDocument> result = new ArrayList<>();
        orderDocumentRepository.save(orderDocuments).forEach(result::add);
        return result;
    }
}
