package ekol.orders.transportOrder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;
import ekol.exceptions.*;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.orders.transportOrder.domain.validator.TransportOrderValidator;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.repository.DocumentTypeRepository;
import ekol.orders.lookup.repository.PackageTypeRepository;
import ekol.orders.order.domain.Status;
import ekol.orders.order.domain.dto.response.location.WarehouseResponse;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.*;
import ekol.orders.transportOrder.dto.Region;
import ekol.orders.transportOrder.dto.rule.ApprovalWorkflow;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentIndexService;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentSearchService;
import ekol.orders.transportOrder.event.OrderCreatedEventProducer;
import ekol.orders.transportOrder.event.OrderRulesExecutedEventMessage;
import ekol.orders.transportOrder.event.ShipmentStatusChangeMessage;
import ekol.orders.transportOrder.event.TripPlanCreatedEventMessage;
import ekol.orders.transportOrder.repository.*;
import ekol.resource.oauth2.SessionOwner;
import ekol.resource.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransportOrderService {

    private static final Comparator<Shipment> SHIPMENT_COMPARATOR = new Comparator<Shipment>() {
        @Override
        public int compare(Shipment shipment1, Shipment shipment2) {
            return shipment1.getCode().compareTo(shipment2.getCode());
        }
    };

    @Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

    @Value("${oneorder.order-template-service}")
    private String orderTemplateServiceName;

    @Value("${oneorder.location-service}")
    private String locationService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private TransportOrderDocumentRepository transportOrderDocumentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentUnitRepository shipmentUnitRepository;

    @Autowired
    private ShipmentUnitPackageRepository shipmentUnitPackageRepository;

    @Autowired
    private RouteRequirementRepository routeRequirementRepository;

    @Autowired
    private RouteRequirementRouteRepository routeRequirementRouteRepository;

    @Autowired
    private VehicleRequirementRepository vehicleRequirementRepository;

    @Autowired
    private EquipmentRequirementRepository equipmentRequirementRepository;

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private ShipmentIndexService shipmentIndexService;

    @Autowired
    private CreateTaskService createTaskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionOwner sessionOwner;

    @Autowired
    private OrderCreatedEventProducer orderCreatedProducer;

    @Autowired
    private ShipmentSearchService shipmentSearchService;

    @Autowired
    private TransportOrderValidator transportOrderValidator;


    /*public TransportOrder handleCreateTransportOrder(TransportOrder transportOrder) {
        TransportOrder order = this.createTransportOrder(transportOrder);

        //shipmentIndexService.produceOrderCreatedEvent(order.getId());
        //shipmentIndexService.produceOrderCreatedExchangeEvent(order.getId());

        return order;
    }*/

    @Transactional
    public TransportOrder createTransportOrder(TransportOrder transportOrder) {

        if (transportOrder.getId() != null) {
            throw new BadRequestException("TransportOrder.id must be null.");
        }

        if (transportOrder.getRequest() == null || transportOrder.getRequest().getId() == null) {
            throw new BadRequestException("TransportOrder.request.id cannot be null.");
        }

        TransportOrderRequest request = transportOrderRequestRepository.findOne(transportOrder.getRequest().getId());

        if (request == null) {
            throw new ResourceNotFoundException("TransportOrderRequest with specified id cannot be found: " + transportOrder.getRequest().getId());
        }

        transportOrderValidator.validate(transportOrder);

        transportOrder.setCustomerId(request.getCustomerId());
        Status status = request.isConfirmationRequired() ? Status.CREATED : Status.CONFIRMED;
        transportOrder.setStatus(status);
        transportOrder.getShipments().forEach(shipment -> shipment.setStatus(status));
        transportOrder.setSubsidiary(request.getSubsidiary());
        //transportOrder.setReadyAtDate(); // TODO: request'teki readyAtDate order'a kopyalanmalı.

        findAndSetShipmentsRegions(transportOrder);

        TransportOrder persistedTransportOrder = transportOrderRepository.save(transportOrder);

        createDocuments(persistedTransportOrder, transportOrder.getDocuments());

        createOrUpdateShipments(persistedTransportOrder, transportOrder.getShipments());

        createOrUpdateRouteRequirements(persistedTransportOrder, transportOrder.getRouteRequirements());

        createOrUpdateVehicleRequirements(persistedTransportOrder, transportOrder.getVehicleRequirements());

        createOrUpdateEquipmentRequirements(persistedTransportOrder, transportOrder.getEquipmentRequirements());

        request.setOrder(persistedTransportOrder);
        transportOrderRequestRepository.save(request);

        // TODO: EntityManager'a dokunmadan bir çözüm bulabilir miyiz? Bu satırlar olmazsa findWithDetailsById istediğimiz tüm alanları getirmiyor.
        entityManager.flush();
        entityManager.clear();

        persistedTransportOrder = findWithDetailsById(persistedTransportOrder.getId());

        orderCreatedProducer.produce(persistedTransportOrder);

        return persistedTransportOrder;
    }

    @Transactional
    public TransportOrder updateTransportOrder(TransportOrder transportOrder) {

        if (transportOrder.getId() == null) {
            throw new BadRequestException("TransportOrder.id cannot be null.");
        }

        transportOrderValidator.validate(transportOrder);

        TransportOrder transportOrderFromDB = transportOrderRepository.findOne(transportOrder.getId());

        Map<String, TransportOrderDocument> existingDocumentMap = new HashMap<>();
        Map<String, TransportOrderDocument> newDocumentMap = new HashMap<>();

        if (transportOrderFromDB.getDocuments() != null && transportOrderFromDB.getDocuments().size() > 0) {
            for (TransportOrderDocument document : transportOrderFromDB.getDocuments()) {
                existingDocumentMap.put(document.getFileName(), document);
            }
        }

        if (transportOrder.getDocuments() != null && transportOrder.getDocuments().size() > 0) {
            for (TransportOrderDocument document : transportOrder.getDocuments()) {
                newDocumentMap.put(document.getFileName(), document);
            }
        }

        TransportOrder persistedTransportOrder = transportOrderRepository.save(transportOrder);

        for (String fileName : existingDocumentMap.keySet()) {
            if (!newDocumentMap.containsKey(fileName)) {
                softDeleteDocument(existingDocumentMap.get(fileName));
            }
        }

        for (String fileName : newDocumentMap.keySet()) {
            if (!existingDocumentMap.containsKey(fileName)) {
                createDocument(persistedTransportOrder, newDocumentMap.get(fileName));
            } else {
                updateDocument(newDocumentMap.get(fileName));
            }
        }

        createOrUpdateShipments(persistedTransportOrder, transportOrder.getShipments());

        createOrUpdateRouteRequirements(persistedTransportOrder, transportOrder.getRouteRequirements());

        createOrUpdateVehicleRequirements(persistedTransportOrder, transportOrder.getVehicleRequirements());

        createOrUpdateEquipmentRequirements(persistedTransportOrder, transportOrder.getEquipmentRequirements());

        return persistedTransportOrder;
    }

    @Transactional
    public TransportOrder patchTransportOrder(Long id, TransportOrder transportOrderPatch) {

        // TODO: Bu method 'globally synchronized' olmalı.

//        JsonNode patchNode = objectMapper.valueToTree(transportOrderPatch);
//        JsonPatch patch = null;
//
//        try {
//            patch = JsonPatch.fromJson(patchNode);
//        } catch (IOException e) {
//            throw new ApplicationException("Could not construct patch", e);
//        }


        TransportOrder transportOrder = transportOrderRepository.findWithDetailsById(id);

        if (transportOrder == null) {
            throw new ResourceNotFoundException("TransportOrder with id " + id + " is not found");
        }

        JsonNode originalNode = objectMapper.valueToTree(transportOrder);
        JsonNode patchNode = objectMapper.valueToTree(transportOrderPatch);
        JsonPatch patch = JsonDiff.asJsonPatch(originalNode, patchNode);

        System.out.println();
        System.out.println();
        System.out.println(patch.toString());
        System.out.println();
        System.out.println();

        JsonNode patchedNode = null;

        try {
            patchedNode = patch.apply(originalNode);
        } catch (JsonPatchException e) {
            throw new ApplicationException("Could not apply patch", e);
        }

        TransportOrder transportOrderPatched = null;

        try {
            transportOrderPatched = objectMapper.treeToValue(patchedNode, TransportOrder.class);
        } catch (JsonProcessingException e) {
            throw new ApplicationException("Could not construct object from node", e);
        }

        return updateTransportOrder(transportOrderPatched);
    }

    @Transactional
    public TransportOrder patchTransportOrder(Long id, JsonPatch patch) {

        // TODO: Bu method 'globally synchronized' olmalı.

        System.out.println();
        System.out.println();
        System.out.println(patch.toString());
        System.out.println();
        System.out.println();

        TransportOrder transportOrder = transportOrderRepository.findWithDetailsById(id);

        if (transportOrder == null) {
            throw new ResourceNotFoundException("TransportOrder with id " + id + " is not found");
        }

        JsonNode originalNode = objectMapper.valueToTree(transportOrder);
        JsonNode patchedNode = null;

        try {
            patchedNode = patch.apply(originalNode);
        } catch (JsonPatchException e) {
            throw new ApplicationException("Could not apply patch", e);
        }

        TransportOrder transportOrderPatched = null;

        try {
            transportOrderPatched = objectMapper.treeToValue(patchedNode, TransportOrder.class);
        } catch (JsonProcessingException e) {
            throw new ApplicationException("Could not construct object from node", e);
        }

        return updateTransportOrder(transportOrderPatched);
    }

    @Transactional
    public TransportOrder confirm(TransportOrder transportOrder, FixedZoneDateTime readyAtDate) {
        transportOrder.setStatus(Status.CONFIRMED);
        transportOrder.getShipments().forEach(shipment -> {
            shipment.setStatus(Status.CONFIRMED);
            shipmentRepository.save(shipment);
        });
        transportOrder.setReadyAtDate(readyAtDate);
        return transportOrderRepository.save(transportOrder);
    }

    @Transactional
    public TransportOrder approveTransportOrder(Long transportOrderId) {

        TransportOrder transportOrder = transportOrderRepository.findOne(transportOrderId);

        if (transportOrder == null) {
            throw new ResourceNotFoundException("TransportOrder not found: " + transportOrderId);
        }

        transportOrder.setStatus(Status.APPROVED);

        return transportOrderRepository.save(transportOrder);
    }

    @Transactional
    public TransportOrder rejectTransportOrder(Long transportOrderId) {

        TransportOrder transportOrder = transportOrderRepository.findOne(transportOrderId);

        if (transportOrder == null) {
            throw new ResourceNotFoundException("TransportOrder not found: " + transportOrderId);
        }

        transportOrder.setStatus(Status.REJECTED);

        return transportOrderRepository.save(transportOrder);
    }

    @Transactional
    public void softDeleteTransportOrder(Long id) {

        TransportOrder transportOrder = transportOrderRepository.findOne(id);

        if (transportOrder != null) {

            softDeleteDocuments(transportOrder.getDocuments());

            softDeleteShipments(transportOrder.getShipments());

            softDeleteRouteRequirements(transportOrder.getRouteRequirements());

            softDeleteVehicleRequirements(transportOrder.getVehicleRequirements());

            softDeleteEquipmentRequirements(transportOrder.getEquipmentRequirements());

            transportOrder.setDeleted(true);
            transportOrderRepository.save(transportOrder);
        }
    }

    private void createDocuments(TransportOrder persistedTransportOrder, Set<TransportOrderDocument> documents) {

        if (documents != null) {
            for (TransportOrderDocument document : documents) {
                createDocument(persistedTransportOrder, document);
            }
        }
    }

    private void checkDocumentOriginalName(TransportOrderDocument document) {
        if (document.getOriginalName() == null || document.getOriginalName().trim().length() == 0) {
            throw new BadRequestException("TransportOrderDocument.originalName cannot be null or empty.");
        } else {
            document.setOriginalName(document.getOriginalName().trim());
        }
    }

    private void checkDocumentDisplayName(TransportOrderDocument document) {
        if (document.getDisplayName() == null || document.getDisplayName().trim().length() == 0) {
            throw new BadRequestException("TransportOrderDocument.displayName cannot be null or empty.");
        } else {
            document.setDisplayName(document.getDisplayName().trim());
        }
    }

    private void checkDocumentFileName(TransportOrderDocument document) {
        if (document.getFileName() == null || document.getFileName().trim().length() == 0) {
            throw new BadRequestException("TransportOrderDocument.fileName cannot be null or empty.");
        } else {
            document.setFileName(document.getFileName().trim());
        }
    }

    private void checkDocumentType(TransportOrderDocument document) {
        if (document.getType() == null || document.getType().getId() == null) {
            throw new BadRequestException("TransportOrderDocument.type.id cannot be null.");
        } else {
            DocumentType type = documentTypeRepository.findOne(document.getType().getId());
            if (type == null) {
                throw new ResourceNotFoundException("TransportOrderDocument with specified id cannot be found: " + document.getType().getId());
            } else {
                document.setType(type);
            }
        }
    }

    private TransportOrderDocument createDocument(TransportOrder persistedTransportOrder, TransportOrderDocument document) {

        document.setId(null);
        document.setTransportOrder(persistedTransportOrder);
        document.setDirectoryPath(fileService.getPermanentDirectory().toString());

        checkDocumentOriginalName(document);
        checkDocumentDisplayName(document);
        checkDocumentFileName(document);
        checkDocumentType(document);

        TransportOrderDocument persistedDocument = transportOrderDocumentRepository.save(document);

        // order request'ten gelen dökümanlar permanent directory'de, yeni eklenen döümanlar temporary directory'de olduğu için...
        if (fileService.fileExistsInTempDir(document.getFileName())) {
            fileService.moveFileToPermDir(document.getFileName());
        }

        return persistedDocument;
    }

    private TransportOrderDocument updateDocument(TransportOrderDocument document) {

        if (document.getId() == null) {
            throw new BadRequestException("TransportOrderDocument.id cannot be null.");
        }

        if (document.getTransportOrder() == null || document.getTransportOrder().getId() == null) {
            throw new BadRequestException("TransportOrderDocument.transportOrder.id cannot be null.");
        }

        document.setDirectoryPath(fileService.getPermanentDirectory().toString());

        checkDocumentOriginalName(document);
        checkDocumentDisplayName(document);
        checkDocumentFileName(document);
        checkDocumentType(document);

        return transportOrderDocumentRepository.save(document);
    }

    private void softDeleteDocuments(Set<TransportOrderDocument> documents) {

        if (documents != null) {
            for (TransportOrderDocument document : documents) {
                softDeleteDocument(document);
            }
        }
    }

    private void softDeleteDocument(TransportOrderDocument document) {
        document.setDeleted(true);
        transportOrderDocumentRepository.save(document);
    }

    private void softDeleteShipments(Set<Shipment> shipments) {

        if (shipments != null) {
            for (Shipment shipment : shipments) {
                softDeleteShipment(shipment);
            }
        }
    }

    private void softDeleteShipment(Shipment shipment) {

        if (shipment.getShipmentUnits() != null) {
            for (ShipmentUnit shipmentUnit : shipment.getShipmentUnits()) {
                softDeleteShipmentUnit(shipmentUnit);
            }
        }

        shipment.setDeleted(true);
        shipmentRepository.save(shipment);
    }

    private void softDeleteShipmentUnit(ShipmentUnit shipmentUnit) {

        if (shipmentUnit.getShipmentUnitPackages() != null) {
            for (ShipmentUnitPackage shipmentUnitPackage : shipmentUnit.getShipmentUnitPackages()) {
                softDeleteShipmentUnitPackage(shipmentUnitPackage);
            }
        }

        shipmentUnit.setDeleted(true);
        shipmentUnitRepository.save(shipmentUnit);
    }

    private void softDeleteShipmentUnitPackage(ShipmentUnitPackage shipmentUnitPackage) {
        shipmentUnitPackage.setDeleted(true);
        shipmentUnitPackageRepository.save(shipmentUnitPackage);
    }

    private void softDeleteRouteRequirements(Set<RouteRequirement> routeRequirements) {

        if (routeRequirements != null) {
            for (RouteRequirement routeRequirement : routeRequirements) {
                softDeleteRouteRequirement(routeRequirement);
            }
        }
    }

    private void softDeleteRouteRequirement(RouteRequirement routeRequirement) {

        if (routeRequirement.getRoutes() != null) {
            for (RouteRequirementRoute route : routeRequirement.getRoutes()) {
                softDeleteRouteRequirementRoute(route);
            }
        }

        routeRequirement.setDeleted(true);
        routeRequirementRepository.save(routeRequirement);
    }

    private void softDeleteRouteRequirementRoute(RouteRequirementRoute route) {
        route.setDeleted(true);
        routeRequirementRouteRepository.save(route);
    }

    private void softDeleteVehicleRequirements(Set<VehicleRequirement> vehicleRequirements) {

        if (vehicleRequirements != null) {
            for (VehicleRequirement vehicleRequirement : vehicleRequirements) {
                softDeleteVehicleRequirement(vehicleRequirement);
            }
        }
    }

    private void softDeleteVehicleRequirement(VehicleRequirement vehicleRequirement) {
        vehicleRequirement.setDeleted(true);
        vehicleRequirementRepository.save(vehicleRequirement);
    }

    private void softDeleteEquipmentRequirements(Set<EquipmentRequirement> equipmentRequirements) {

        if (equipmentRequirements != null) {
            for (EquipmentRequirement equipmentRequirement : equipmentRequirements) {
                softDeleteEquipmentRequirement(equipmentRequirement);
            }
        }
    }

    private void softDeleteEquipmentRequirement(EquipmentRequirement equipmentRequirement) {
        equipmentRequirement.setDeleted(true);
        equipmentRequirementRepository.save(equipmentRequirement);
    }

    private void createOrUpdateShipments(TransportOrder persistedTransportOrder, Set<Shipment> shipments) {

        if (shipments != null) {
            for (Shipment shipment : shipments) {
                shipment.setTransportOrder(persistedTransportOrder);
                saveShipment(shipment);
            }
        }
    }

    private Shipment saveShipment(Shipment shipment) {

        // TODO: Arayüzde kuralı bulup gerekli kontrolleri yapıyoruz, burada da aynı kontrolleri yapmamız gerekiyor ancak
        // bu kural yapısı büyük ihtimalle değişecek. Bu kısmı daha sonra yapmalı.

//        Long senderId = shipment.getSender().getCompanyId();
//        Long loadingLocationOwnerId = shipment.getSender().getLocationOwnerCompanyId();
//        Long loadingLocationId = shipment.getSender().getLocationId();
//        Long consigneeId = shipment.getReceiver().getCompanyId();
//
//        oAuth2RestTemplate.getForObject(orderTemplateServiceName + "/rule/sender/rule-that-matches" +
//                "?senderId=" + senderId + "&loadingLocationOwnerId=" + loadingLocationOwnerId + "&loadingLocationId=" + loadingLocationId + "&consigneeId=" + consigneeId,
//                Object.class);

        WarehouseResponse warehouse = getWarehouseByCompanyLocation(shipment.getSender().getLocationId());

        if (warehouse != null) {
            shipment.getSender().setLocationIsAWarehouse(Boolean.TRUE);
            shipment.getSender().setWarehouse(new IdNamePair(warehouse.getId(), warehouse.getName()));
        } else {
            shipment.getSender().setLocationIsAWarehouse(Boolean.FALSE);
            shipment.getSender().setWarehouse(null);
        }

        warehouse = getWarehouseByCompanyLocation(shipment.getReceiver().getLocationId());

        if (warehouse != null) {
            shipment.getReceiver().setLocationIsAWarehouse(Boolean.TRUE);
            shipment.getReceiver().setWarehouse(new IdNamePair(warehouse.getId(), warehouse.getName()));
        } else {
            shipment.getReceiver().setLocationIsAWarehouse(Boolean.FALSE);
            shipment.getReceiver().setWarehouse(null);
        }

        Shipment persistedShipment = shipmentRepository.save(shipment);

        if (shipment.getShipmentUnits() != null) {
            for (ShipmentUnit shipmentUnit : shipment.getShipmentUnits()) {
                shipmentUnit.setShipment(persistedShipment);
                saveShipmentUnit(shipmentUnit);
            }
        }

        return persistedShipment;
    }

    private ShipmentUnit saveShipmentUnit(ShipmentUnit shipmentUnit) {

        shipmentUnit.setType(packageTypeRepository.findOne(shipmentUnit.getType().getId()));
        ShipmentUnit persistedShipmentUnit = shipmentUnitRepository.save(shipmentUnit);

        if (shipmentUnit.getShipmentUnitPackages() != null) {
            for (ShipmentUnitPackage shipmentUnitPackage : shipmentUnit.getShipmentUnitPackages()) {
                shipmentUnitPackage.setShipmentUnit(persistedShipmentUnit);
                saveShipmentUnitPackage(shipmentUnitPackage);
            }
        }

        return persistedShipmentUnit;
    }

    private ShipmentUnitPackage saveShipmentUnitPackage(ShipmentUnitPackage shipmentUnitPackage) {

        return shipmentUnitPackageRepository.save(shipmentUnitPackage);
    }

    private void createOrUpdateRouteRequirements(TransportOrder persistedTransportOrder, Set<RouteRequirement> routeRequirements) {

        if (routeRequirements != null) {
            for (RouteRequirement routeRequirement : routeRequirements) {
                routeRequirement.setTransportOrder(persistedTransportOrder);
                saveRouteRequirement(routeRequirement);
            }
        }
    }

    private RouteRequirement saveRouteRequirement(RouteRequirement routeRequirement) {

        RouteRequirement persistedRouteRequirement = routeRequirementRepository.save(routeRequirement);

        if (routeRequirement.getRoutes() != null) {
            for (RouteRequirementRoute route : routeRequirement.getRoutes()) {
                route.setRequirement(persistedRouteRequirement);
                saveRouteRequirementRoute(route);
            }
        }

        return persistedRouteRequirement;
    }

    private RouteRequirementRoute saveRouteRequirementRoute(RouteRequirementRoute route) {

        return routeRequirementRouteRepository.save(route);
    }

    private void createOrUpdateVehicleRequirements(TransportOrder persistedTransportOrder, Set<VehicleRequirement> vehicleRequirements) {

        if (vehicleRequirements != null) {
            for (VehicleRequirement vehicleRequirement : vehicleRequirements) {
                vehicleRequirement.setTransportOrder(persistedTransportOrder);
                vehicleRequirementRepository.save(vehicleRequirement);
            }
        }
    }

    private void createOrUpdateEquipmentRequirements(TransportOrder persistedTransportOrder, Set<EquipmentRequirement> equipmentRequirements) {

        if (equipmentRequirements != null) {
            for (EquipmentRequirement equipmentRequirement : equipmentRequirements) {
                equipmentRequirement.setTransportOrder(persistedTransportOrder);
                saveEquipmentRequirement(equipmentRequirement);
            }
        }
    }

    private EquipmentRequirement saveEquipmentRequirement(EquipmentRequirement equipmentRequirement) {

        return equipmentRequirementRepository.save(equipmentRequirement);
    }

    private void fillCustomer(TransportOrder transportOrder) {
        transportOrder.setCustomer(oAuth2RestTemplate.getForObject(kartoteksServiceName + "/company/" + transportOrder.getCustomerId(), Company.class));
    }

    private void fillSenderOrReceiver(SenderOrReceiver senderOrReceiver) {

        if (senderOrReceiver != null) {

            Company company = null;
            Company locationOwnerCompany = null;

            if (senderOrReceiver.getCompanyId() != null) {
                company = oAuth2RestTemplate.getForObject(kartoteksServiceName + "/company/" + senderOrReceiver.getCompanyId(), Company.class);
                senderOrReceiver.setCompany(company);
            }

            if (senderOrReceiver.getLocationId() != null) {
                senderOrReceiver.setLocation(oAuth2RestTemplate.getForObject(kartoteksServiceName + "/location/" + senderOrReceiver.getLocationId(), Location.class));
            }

            if (senderOrReceiver.getCompanyContactId() != null) {
                senderOrReceiver.setCompanyContact(oAuth2RestTemplate.getForObject(kartoteksServiceName + "/contact/" + senderOrReceiver.getCompanyContactId(), Contact.class));
            }

            if (senderOrReceiver.getLocationOwnerCompanyId() != null) {
                // Genelde locationOwnerCompany ve company aynı olduğundan fazladan sorgu yapmamak için...
                if (senderOrReceiver.getLocationOwnerCompanyId().equals(senderOrReceiver.getCompanyId())) {
                    locationOwnerCompany = company;
                } else {
                    locationOwnerCompany = oAuth2RestTemplate.getForObject(kartoteksServiceName + "/company/" + senderOrReceiver.getLocationOwnerCompanyId(), Company.class);
                }
                senderOrReceiver.setLocationOwnerCompany(locationOwnerCompany);
            }

            if (senderOrReceiver.getLocationContactId() != null) {
                senderOrReceiver.setLocationContact(oAuth2RestTemplate.getForObject(kartoteksServiceName + "/contact/" + senderOrReceiver.getLocationContactId(), Contact.class));
            }
        }
    }

    private void sortShipments(TransportOrder transportOrder) {
        if (transportOrder.getShipments() != null) {
            List<Shipment> list = new ArrayList<>(transportOrder.getShipments());
            Collections.sort(list, SHIPMENT_COMPARATOR);
            transportOrder.setShipments(new LinkedHashSet<>(list));
        }
    }

    public TransportOrder findWithDetailsById(long transportOrderId) {

        TransportOrder transportOrder = transportOrderRepository.findWithDetailsById(transportOrderId);

        if (transportOrder == null) {
            throw new ResourceNotFoundException("TransportOrder with id " + transportOrderId + " is not found");
        }

        fillCustomer(transportOrder);

        if (transportOrder.getShipments() != null && !transportOrder.getShipments().isEmpty()) {
            transportOrder.getShipments().forEach(shipment -> {
                fillSenderOrReceiver(shipment.getSender());
                fillSenderOrReceiver(shipment.getReceiver());
            });
            sortShipments(transportOrder);
        }

        return transportOrder;
    }

    public TransportOrder findWithRuleSetDetailsById(long transportOrderId) {

        TransportOrder transportOrder = transportOrderRepository.findWithRuleSetDetailsById(transportOrderId);

        if (transportOrder == null) {
            throw new ResourceNotFoundException("TransportOrder with id " + transportOrderId + " is not found");
        }

        return transportOrder;
    }

    @Transactional
    public void processRuleExecutions(OrderRulesExecutedEventMessage orderRulesExecutedEventMessage) {
        TransportOrder transportOrder = transportOrderRepository.findWithDetailsById(orderRulesExecutedEventMessage.getOrderResult().getOrderId());
        boolean supervisorApprovalNeeded = false;
        boolean managerApprovalNeeded = false;

        if (orderRulesExecutedEventMessage.getOrderResult().getRequiredApprovals() != null) {
            for (OrderRulesExecutedEventMessage.ApprovalResult approval : orderRulesExecutedEventMessage.getOrderResult().getRequiredApprovals()) {
                if (approval.getLevel().equals(ApprovalWorkflow.SUPERVISOR.name())) {
                    supervisorApprovalNeeded = true;
                } else if (approval.getLevel().equals(ApprovalWorkflow.MANAGER.name())) {
                    managerApprovalNeeded = true;
                }
                if (supervisorApprovalNeeded && managerApprovalNeeded) {
                    break;
                }
            }
        }
        // Hiç approval workflow yoksa 'APPROVED' olarak kabul et.
        if (!supervisorApprovalNeeded && !managerApprovalNeeded) {
            transportOrder.setStatus(Status.APPROVED);
            transportOrder.getShipments().forEach(shipment -> {
                shipment.setStatus(Status.APPROVED);
                shipmentRepository.save(shipment);
            });
        }

        TransportOrder persistedTransportOrder = transportOrderRepository.save(transportOrder);

        if (managerApprovalNeeded) {
            supervisorApprovalNeeded = true;
        }

        if (supervisorApprovalNeeded || managerApprovalNeeded) {
            createTaskService.createTaskForOrderApproval(persistedTransportOrder.getId(), supervisorApprovalNeeded, managerApprovalNeeded);
        }


        applyRuleResultForShipments(transportOrder, orderRulesExecutedEventMessage);

    }

    private void applyRuleResultForShipments(TransportOrder persistedTransportOrder, OrderRulesExecutedEventMessage orderRulesExecutedEventMessage) {
        orderRulesExecutedEventMessage.getOrderResult().getShipmentResults().entrySet().forEach(entry -> {
            Long shipmentId = entry.getKey();
            OrderRulesExecutedEventMessage.ShipmentRuleResult ruleResult = entry.getValue();
            Shipment shipment = persistedTransportOrder.getShipments().stream().filter(each -> each.getId().equals(shipmentId)).findFirst().get();
            shipment.setCollectionWarehouse(ruleResult.getCollectionWarehouse());
            shipment.setDistributionWarehouse(ruleResult.getDistributionWarehouse());
            shipment.setCollectionArrivalDate(ruleResult.getCollectionArrivalShouldBeBefore());
            shipment.setLinehaulArrivalDate(ruleResult.getLinehaulArrivalShouldBeBefore());
            shipment.setExecutedRuleResult(ruleResult.toJSON());
            shipmentRepository.save(shipment);
        });
    }

    public List<ShipmentDocument> searchShipmentsOfMySubsidiary() {

        Long subsidiaryId;
        if(sessionOwner.getCurrentUser().getSubsidiaries() != null && sessionOwner.getCurrentUser().getSubsidiaries().size() > 0) {
            subsidiaryId = sessionOwner.getCurrentUser().getSubsidiaries().get(0).getId();
        } else {
            throw new UnauthorizedAccessException("User does not have any subsidiary.");
        }
        return shipmentSearchService.searchShipmentsBySubsidiaryId(subsidiaryId);
    }

    private void findAndSetShipmentsRegions(TransportOrder transportOrder) {
        transportOrder.getShipments().forEach(shipment-> {
            Region collectionRegion = retrieveCollectionRegionOfLocation(shipment.getSender().getLocationId());
            Region distributionRegion = retrieveDistributionRegionOfLocation(shipment.getReceiver().getLocationId());
            if(collectionRegion == null) {
                throw new ValidationException("Loading Location does not belong to a Collection Region");
            }
            if(distributionRegion == null) {
                throw new ValidationException("Unloading Location does not belong to a Collection Region");
            }

            shipment.getSender().setLocationRegionId(collectionRegion.getId());
            shipment.getSender().setLocationRegionCategoryId(collectionRegion.getCategory().getId());
            shipment.getSender().setLocationOperationRegionId(collectionRegion.getOperationRegion().getId());

            shipment.getReceiver().setLocationRegionId(distributionRegion.getId());
            shipment.getReceiver().setLocationRegionCategoryId(distributionRegion.getCategory().getId());
            shipment.getReceiver().setLocationOperationRegionId(distributionRegion.getOperationRegion().getId());

        });
    }

    private Region retrieveCollectionRegionOfLocation(Long locationId) {
        Region collectionRegion = oAuth2RestTemplate.getForObject(
                locationService + "/collection-region/query-two/by-company-location?companyLocationId=" + locationId, Region.class);

        return collectionRegion;
    }

    private Region retrieveDistributionRegionOfLocation(Long locationId) {
        Region collectionRegion = oAuth2RestTemplate.getForObject(
                locationService + "/distribution-region/query-two/by-company-location?companyLocationId=" + locationId, Region.class);

        return collectionRegion;
    }

    private WarehouseResponse getWarehouseByCompanyLocation(Long companyLocationId) {
        return oAuth2RestTemplate.getForObject(locationService + "/location/warehouse/bycompanylocation/" + companyLocationId, WarehouseResponse.class);
    }

    @Transactional
    public void processTripPlanCreated(TripPlanCreatedEventMessage eventMessage){
        eventMessage.getTrips().stream()
                .map(TripPlanCreatedEventMessage.Trip::getFromTripStop)
                .forEach(tripStop -> {
                    Long locationId = tripStop.getLocation().getId();
                    Set<Long> shipmentIds = tripStop.getTripOperations().stream()
                            .map(TripPlanCreatedEventMessage.TripOperation::getTransport)
                            .map(TripPlanCreatedEventMessage.Transport::getShipmentId)
                            .collect(Collectors.toSet());
                    shipmentIds.forEach(id -> {
                        Shipment shipment = shipmentRepository.findOne(id);
                        if(shipment != null && shipment.getSender().getLocationId().equals(locationId)){
                            updateShipmentStatus(shipment, Status.PLANNED);
                        }

                    });
                });

    }

    private void updateShipmentStatus(Shipment shipment, Status status){
        shipment.setStatus(status);
        shipmentRepository.save(shipment);
        shipmentIndexService.indexTransportOrder(shipment.getTransportOrder().getId());
    }

    public void handleShipmentCollected(ShipmentStatusChangeMessage message) {
        Shipment shipment = shipmentRepository.findWithTransportOrderById(message.getShipmentId());
        if(shipment != null){
            this.updateShipmentStatus(shipment, Status.COLLECTED);
        }
    }
    public void handleShipmentDelivered(ShipmentStatusChangeMessage message) {
        Shipment shipment = shipmentRepository.findWithTransportOrderById(message.getShipmentId());
        if(shipment != null){
            this.updateShipmentStatus(shipment, Status.DELIVERED);
        }
    }
}