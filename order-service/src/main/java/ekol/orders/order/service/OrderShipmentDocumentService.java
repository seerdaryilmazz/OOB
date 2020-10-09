package ekol.orders.order.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.ValidationException;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentDocument;
import ekol.orders.order.repository.OrderShipmentDocumentRepository;
import ekol.orders.order.validator.DocumentValidator;

@Service
public class OrderShipmentDocumentService {

    private OrderShipmentDocumentRepository orderShipmentDocumentRepository;
    private DocumentTypeRepository documentTypeRepository;
    private DocumentFileService documentFileService;
    private ListOrderService listOrderService;
    private DocumentValidator documentValidator;

    @Autowired
    public OrderShipmentDocumentService(OrderShipmentDocumentRepository orderShipmentDocumentRepository,
                                        DocumentTypeRepository documentTypeRepository,
                                        DocumentFileService documentFileService,
                                        ListOrderService listOrderService,
                                        DocumentValidator documentValidator){
        this.orderShipmentDocumentRepository = orderShipmentDocumentRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.documentFileService = documentFileService;
        this.listOrderService = listOrderService;
        this.documentValidator = documentValidator;
    }

    public OrderShipmentDocument getByIdOrThrowException(Long id){
        return orderShipmentDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document with id {0} not found", String.valueOf(id)));
    }

    public List<OrderShipmentDocument> getDocuments(Long shipmentId){
        return orderShipmentDocumentRepository.findByShipmentId(shipmentId);
    }

    public List<OrderShipmentDocument> getDocuments(Long shipmentId, DocumentTypeGroup group){
        return orderShipmentDocumentRepository.findByShipmentId(shipmentId).stream()
                .filter(document -> document.getDocument().getType().getDocumentGroup().equals(group))
                .collect(toList());
    }

    public void deleteDocument(Long documentId){
        OrderShipmentDocument document = getByIdOrThrowException(documentId);
        document.setDeleted(true);

        orderShipmentDocumentRepository.save(document);

        if(getDocuments(document.getShipment().getId()).stream().noneMatch(t->!Objects.equals(t.getId(), documentId) &&  Objects.equals(document.getDocument().getSavedFileName(), t.getDocument().getSavedFileName() ))) {
        	documentFileService.removeDocument(document.getDocument());
        }

    }

    public List<OrderShipmentDocument> saveDocument(Long shipmentId, List<OrderShipmentDocument> shipmentDocuments) {
        OrderShipment shipment = listOrderService.findShipmentByIdOrThrowException(shipmentId);

        shipmentDocuments.forEach(shipmentDocument -> {
            documentValidator.validate(shipmentDocument.getDocument());
            documentTypeRepository.findById(shipmentDocument.getDocument().getType().getId())
                    .ifPresent(documentType -> shipmentDocument.getDocument().setType(documentType));
            if(shipmentDocument.isHealthCertificateDocument() &&
                    shipment.getHealthCertificateTypes().stream()
                            .noneMatch(certificate -> certificate.getId().equals(shipmentDocument.getDocument().getType().getId()))){
                throw new ValidationException("Document type {0} needs to be added to the certificate information field. Then, document can be attached",
                        shipmentDocument.getDocument().getType().getName());
            }
            shipmentDocument.setShipment(shipment);
        });
        documentFileService.moveDocumentsToPermanentSpace(shipmentDocuments.stream().map(OrderShipmentDocument::getDocument).collect(toList()));
        List<OrderShipmentDocument> result = new ArrayList<>();
        orderShipmentDocumentRepository.save(shipmentDocuments).forEach(result::add);
        return result;
    }




}
