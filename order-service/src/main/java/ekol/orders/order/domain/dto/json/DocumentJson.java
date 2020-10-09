package ekol.orders.order.domain.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.DocumentTypeGroup;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.OrderDocument;
import ekol.orders.order.domain.OrderShipmentDocument;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentJson {

    private Long id;
    private IdCodeNameTrio type;
    private DocumentTypeGroup group;
    private String path;
    private String savedFileName;
    private String originalFileName;
    private String description;

    public static DocumentJson fromShipmentDocument(OrderShipmentDocument shipmentDocument){
        DocumentJson json = new DocumentJson();
        json.setId(shipmentDocument.getId());
        json.setSavedFileName(shipmentDocument.getDocument().getSavedFileName());
        json.setOriginalFileName(shipmentDocument.getDocument().getOriginalFileName());
        json.setPath(shipmentDocument.getDocument().getPath());
        json.setType(shipmentDocument.getDocument().getType().toIdCodeNameTrio());
        json.setGroup(shipmentDocument.getDocument().getType().getDocumentGroup());
        json.setDescription(shipmentDocument.getDocument().getDescription());
        return json;
    }

    public static DocumentJson fromOrderDocument(OrderDocument orderDocument){
        DocumentJson json = new DocumentJson();
        json.setId(orderDocument.getId());
        json.setType(orderDocument.getDocument().getType().toIdCodeNameTrio());
        json.setSavedFileName(orderDocument.getDocument().getSavedFileName());
        json.setOriginalFileName(orderDocument.getDocument().getOriginalFileName());
        json.setPath(orderDocument.getDocument().getPath());
        json.setGroup(orderDocument.getDocument().getType().getDocumentGroup());
        json.setDescription(orderDocument.getDocument().getDescription());
        return json;
    }

    public OrderShipmentDocument toShipmentDocument(){
        OrderShipmentDocument shipmentDocument = new OrderShipmentDocument();
        shipmentDocument.setId(getId());
        Document document = new Document();
        document.setSavedFileName(getSavedFileName());
        document.setOriginalFileName(getOriginalFileName());
        document.setPath(getPath());
        document.setType(DocumentType.with(getType()));
        document.setDescription(getDescription());
        shipmentDocument.setDocument(document);
        return shipmentDocument;
    }

    public OrderDocument toOrderDocument(){
        OrderDocument orderDocument = new OrderDocument();
        orderDocument.setId(getId());
        Document document = new Document();
        document.setSavedFileName(getSavedFileName());
        document.setOriginalFileName(getOriginalFileName());
        document.setPath(getPath());
        document.setType(DocumentType.with(getType()));
        document.setDescription(getDescription());
        orderDocument.setDocument(document);
        return orderDocument;
    }
}
