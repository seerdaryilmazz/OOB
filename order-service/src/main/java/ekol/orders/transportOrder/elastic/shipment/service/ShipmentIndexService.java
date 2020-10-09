package ekol.orders.transportOrder.elastic.shipment.service;


import ekol.orders.order.domain.dto.response.location.WarehouseResponse;
import ekol.orders.order.service.LocationServiceClient;
import ekol.orders.transportOrder.domain.Company;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.domain.TransportOrder;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.repository.ShipmentRepository;
import ekol.orders.transportOrder.repository.TransportOrderRepository;
import ekol.orders.transportOrder.service.KartoteksClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozer on 03/10/16.
 */
@Service
public class ShipmentIndexService {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentIndexService.class);

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private KartoteksClient kartoteksClient;

    @Autowired
    private LocationServiceClient locationServiceClient;

    public void setTransportOrderRepository(TransportOrderRepository transportOrderRepository) {
        this.transportOrderRepository = transportOrderRepository;
    }

    @Transactional
    public void deleteAndBuildIndex(){
        deleteAllIndexedTransportOrders();
        transportOrderRepository.findStreamingByIdIsNotNull().forEach(transportOrder -> indexTransportOrder(transportOrder.getId()));
    }

    public List<ShipmentDocument> indexTransportOrder(Long id) {
        List<ShipmentDocument> documents = convertTransportOrderForIndex(id);
        documents.forEach(shipmentDocument -> indexShipmentDocument(shipmentDocument));
        return documents;
    }

    public List<ShipmentDocument> convertTransportOrderForIndex(Long id){
        List<ShipmentDocument> result = new ArrayList<>();
        TransportOrder transportOrder = transportOrderRepository.findWithDetailsById(id);
        if(transportOrder == null || transportOrder.getShipments() == null){
            return result;
        }
        fillTransportOrder(transportOrder);
        transportOrder.getShipments().forEach(shipment -> {
            fillShipment(shipment);
            result.add(ShipmentDocument.fromShipment(shipment));
        });
        return result;
    }

    public void deleteAllIndexedTransportOrders() {
        elasticsearchTemplate.deleteIndex(ShipmentDocument.class);
    }

    private void checkIndexAndCreate(){
        if (!elasticsearchTemplate.indexExists(ShipmentDocument.class)) {
            elasticsearchTemplate.createIndex(ShipmentDocument.class);
            elasticsearchTemplate.putMapping(ShipmentDocument.class);
            elasticsearchTemplate.refresh(ShipmentDocument.class);
        }
    }

    private void indexShipmentDocument(ShipmentDocument document) {
        checkIndexAndCreate();
        elasticsearchTemplate.index(new IndexQueryBuilder().withId(document.getId()).withObject(document).build());
    }

    public ShipmentDocument convertShipmentForIndex(Long id) {
        Shipment shipment = shipmentRepository.findWithDetailsById(id);
        fillTransportOrder(shipment.getTransportOrder());
        fillShipment(shipment);
        return ShipmentDocument.fromShipment(shipment);
    }

    private void fillTransportOrder(TransportOrder transportOrder){
        transportOrder.setCustomer(kartoteksClient.findCompanyById(transportOrder.getCustomerId()));
    }

    private void fillShipment(Shipment shipment){
        fillSenderForShipment(shipment);
        fillReceiverForShipment(shipment);
        fillWarehousesForShipment(shipment);
    }
    private void fillSenderForShipment(Shipment shipment){
        Company sender = kartoteksClient.findCompanyById(shipment.getSender().getCompanyId());
        shipment.getSender().setCompany(sender);
        if(shipment.isSenderCompanyEqualsLoadingLocationOwner()){
            shipment.getSender().setLocationOwnerCompany(sender);
        }else{
            shipment.getSender().setLocationOwnerCompany(kartoteksClient.findCompanyById(shipment.getSender().getLocationOwnerCompanyId()));
        }

        shipment.getSender().setLocation(kartoteksClient.findLocationById(shipment.getSender().getLocationId()));
    }

    private void fillReceiverForShipment(Shipment shipment){
        Company receiver = kartoteksClient.findCompanyById(shipment.getReceiver().getCompanyId());
        shipment.getReceiver().setCompany(receiver);
        if(shipment.isReceiverCompanyEqualsLoadingLocationOwner()){
            shipment.getReceiver().setLocationOwnerCompany(receiver);
        }else{
            shipment.getReceiver().setLocationOwnerCompany(kartoteksClient.findCompanyById(shipment.getReceiver().getLocationOwnerCompanyId()));
        }

        shipment.getReceiver().setLocation(kartoteksClient.findLocationById(shipment.getReceiver().getLocationId()));
    }

    private void fillWarehousesForShipment(Shipment shipment){
        if(shipment.getCollectionWarehouse() != null){
            WarehouseResponse collectionLocation = locationServiceClient.findWarehouseById(shipment.getCollectionWarehouse().getId());
            shipment.setCollectionWarehouseLocation(collectionLocation.getCompanyLocation());
        }

        if(shipment.getDistributionWarehouse() != null) {
            WarehouseResponse distributionLocation = locationServiceClient.findWarehouseById(shipment.getDistributionWarehouse().getId());
            shipment.setDistributionWarehouseLocation(distributionLocation.getCompanyLocation());
        }
    }

    public void fillRegionsForShipment(Shipment shipment) {

    }

  /*  public void updateShipmentIndicesSetRelatedSegmentsAsPlanned(TripPlanCreatedEventMessage message){

        Map<Long, List<Long>> map = message.retrievePlannedSegmentIdsInShipmentIds();

        List<ShipmentDocument> documents = shipmentSearchService.findByShipmentIds(map.keySet());

        documents.forEach(doc -> {
            doc.getSegments().forEach(segment -> {
                if(map.get(Long.parseLong(doc.getId())).contains(segment.getSegmentId())) {
                    segment.setPlanned(true);
                }
            });
        });

        bulkIndexShipmentDocument(documents);

    }*/

}
