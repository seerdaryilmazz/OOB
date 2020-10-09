package ekol.orders.order.service;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.ApplicationException;
import ekol.orders.order.domain.*;
import ekol.orders.order.domain.dto.json.CreateOrderRequestJson;
import ekol.orders.order.domain.dto.response.SubsidiaryResponse;
import ekol.orders.order.domain.dto.response.kartoteks.CompanyResponse;
import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import ekol.orders.order.domain.dto.response.location.CustomsOfficeLocationResponse;
import ekol.orders.order.domain.dto.response.location.RegionResponse;
import ekol.orders.order.event.Operation;
import ekol.orders.order.event.OrderEvent;
import ekol.orders.order.repository.*;
import ekol.orders.order.validator.OrderValidator;
import ekol.resource.service.FileService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CreateOrderService {

    private OrderRepository orderRepository;
    private OrderShipmentRepository shipmentRepository;
    private OrderShipmentUnitRepository shipmentUnitRepository;
    private OrderShipmentAdrRepository shipmentAdrRepository;
    private OrderShipmentVehicleRequirementRepository shipmentVehicleRequirementRepository;
    private OrderShipmentEquipmentRequirementRepository shipmentEquipmentRequirementRepository;
    private OrderDocumentRepository orderDocumentRepository;
    private OrderShipmentDocumentRepository orderShipmentDocumentRepository;
    private OrderShipmentArrivalCustomsRepository orderShipmentArrivalCustomsRepository;
    private OrderShipmentDepartureCustomsRepository orderShipmentDepartureCustomsRepository;
    private OrderShipmentDefinitionOfGoodsRepository orderShipmentDefinitionOfGoodsRepository;
    private OrderValidator orderValidator;
    private KartoteksServiceClient kartoteksServiceClient;
    private OrderShipmentLoadSpecsService shipmentLoadSpecsService;
    private LocationServiceClient locationServiceClient;
    private AuthorizationServiceClient authorizationServiceClient;
    private LoadingMeterCalculator loadingMeterCalculator;
    private PayWeightCalculator payWeightCalculator;
    private FileService fileService;
    private ApplicationEventPublisher publisher;
    private CodeGenerator codeGenerator;



    private void setCustomerPartnership(Order order){
        if(order.getCustomer() != null){
            boolean isPartner = kartoteksServiceClient.isCompanyPartner(order.getCustomer().getId());
            order.setPartnerCustomer(isPartner);
        }
    }
    private void calculateVolumeForEachShipmentUnit(Order order){
        order.getShipments().forEach(shipment -> {
            shipment.getUnits().stream()
                    .filter(shipmentUnit -> shipmentUnit.getWidth() != null && shipmentUnit.getLength() != null &&
                            shipmentUnit.getHeight() != null && shipmentUnit.getQuantity() != null)
                    .forEach(shipmentUnit -> {
                        BigDecimal volume = loadingMeterCalculator.calculateVolume(
                                shipmentUnit.getLength(), shipmentUnit.getWidth(), shipmentUnit.getHeight(), shipmentUnit.getQuantity());
                        shipmentUnit.setVolume(volume);
                    });
        });
    }
    private void calculateLdmForEachShipmentUnit(Order order) {
        order.getShipments().forEach(shipment -> {
            shipment.getUnits().stream()
                    .filter(OrderShipmentUnit::canCalculateLdm)
                    .forEach(shipmentUnit -> {
                        BigDecimal ldm = loadingMeterCalculator.calculateLoadingMeter(
                                shipmentUnit.getLength(), shipmentUnit.getWidth(), shipmentUnit.calculateStackSize(), shipmentUnit.getQuantity());
                        shipmentUnit.setLdm(ldm);
                    });
        });
    }

    private void calculatePayWeightForEachShipment(Order order) {
        order.getShipments().stream()
                .filter(OrderShipment::canCalculatePayWeight)
                .forEach(shipment ->
                    shipment.setPayWeight(payWeightCalculator.calculatePayWeight(shipment.getGrossWeight(), shipment.getTotalVolume(), shipment.getTotalLdm()))
                );
    }

    private RegionResponse getCollectionRegion(ShipmentHandlingParty handlingParty) {
    	if(CompanyType.COMPANY == handlingParty.getHandlingCompanyType()) {
    		return locationServiceClient.getCollectionRegionOfCompanyLocation(handlingParty.getHandlingLocation().getId());
    	} else if (CompanyType.CUSTOMS == handlingParty.getHandlingCompanyType()) {
    		return locationServiceClient.getCollectionRegionOfCustomsLocation(handlingParty.getHandlingLocation().getId());
    	} 
    	return null;
    }
    private RegionResponse getDistributionRegion(ShipmentHandlingParty handlingParty) {
    	if(CompanyType.COMPANY == handlingParty.getHandlingCompanyType()) {
    		return locationServiceClient.getDistributionRegionOfCompanyLocation(handlingParty.getHandlingLocation().getId());
    	} else if (CompanyType.CUSTOMS == handlingParty.getHandlingCompanyType()) {
    		return locationServiceClient.getDistributionRegionOfCustomsLocation(handlingParty.getHandlingLocation().getId());
    	} 
    	return null;
    }

    private void setRegionsOfCollectionLocation(List<ShipmentHandlingParty> handlingParties){
        handlingParties.forEach(handlingParty -> {
            RegionResponse response = getCollectionRegion(handlingParty);
            setHandlingPartyRegions(handlingParty, response);
            setHandlingPartyCrossDockInfo(handlingParty);
        });
    }
    
    private void setRegionsOfDistributionLocation(List<ShipmentHandlingParty> handlingParties){
        handlingParties.forEach(handlingParty -> {
            RegionResponse response = getDistributionRegion(handlingParty);
            setHandlingPartyRegions(handlingParty, response);
            setHandlingPartyCrossDockInfo(handlingParty);
        });
    }
    private void setHandlingPartyRegions(ShipmentHandlingParty handlingParty, RegionResponse response){
        if(response != null){
            handlingParty.setHandlingOperationRegion(IdNameEmbeddable.with(response.getOperationRegion()));
            handlingParty.setHandlingRegion(IdNameEmbeddable.with(response.getId(), response.getName()));
            handlingParty.setHandlingRegionCategory(CodeNameEmbeddable.with(response.getCategory()));
        }
    }
    private void setHandlingPartyCrossDockInfo(ShipmentHandlingParty handlingParty){
        boolean handlingAtCrossDock = CompanyType.COMPANY == handlingParty.getHandlingCompanyType() 
                && locationServiceClient.isCrossDockWarehouse(handlingParty.getHandlingLocation().getId());
        handlingParty.setHandlingAtCrossDock(handlingAtCrossDock);
    }
    private void setRegionsOfSenderHandlingLocations(Order order){
        List<ShipmentHandlingParty> senders = order.getShipments().stream()
                .filter(orderShipment -> orderShipment.getSender() != null && orderShipment.getSender().getHandlingLocation() != null)
                .map(OrderShipment::getSender).collect(toList());
        setRegionsOfCollectionLocation(senders);
    }
    private void setRegionsOfConsigneeHandlingLocations(Order order){
        List<ShipmentHandlingParty> consignees = order.getShipments().stream()
                .filter(orderShipment -> orderShipment.getConsignee() != null && orderShipment.getConsignee().getHandlingLocation() != null)
                .map(OrderShipment::getConsignee).collect(toList());
        setRegionsOfDistributionLocation(consignees);
    }
    private void setVolumeAndLdmForShipmentUnits(Order order){
        calculateVolumeForEachShipmentUnit(order);
        calculateLdmForEachShipmentUnit(order);
        calculatePayWeightForEachShipment(order);
    }
    private void setRegionsOfHandlingLocations(Order order){
        setRegionsOfSenderHandlingLocations(order);
        setRegionsOfConsigneeHandlingLocations(order);
    }

    private void setHandlingLocationAddressDetails(Order order){
        order.getShipments().forEach(orderShipment -> {
        	Map<String, String> senderHandlingLocationDetails = fetchHandnlingLocatioDetails(orderShipment.getSender());
            orderShipment.getSender().setHandlingLocationCountryCode(senderHandlingLocationDetails.get("countryCode"));
            orderShipment.getSender().setHandlingLocationPostalCode(senderHandlingLocationDetails.get("postalCode"));
            orderShipment.getSender().setHandlingLocationTimezone(senderHandlingLocationDetails.get("timezone"));
            
            Map<String, String> consigneeHandlingLocationDetails = fetchHandnlingLocatioDetails(orderShipment.getConsignee());
            orderShipment.getConsignee().setHandlingLocationCountryCode(consigneeHandlingLocationDetails.get("countryCode"));
            orderShipment.getConsignee().setHandlingLocationPostalCode(consigneeHandlingLocationDetails.get("postalCode"));
            orderShipment.getConsignee().setHandlingLocationTimezone(consigneeHandlingLocationDetails.get("timezone"));
        });
    }
    
    private Map<String, String> fetchHandnlingLocatioDetails(ShipmentHandlingParty party) {
    	String countryCode = null;
    	String postalCode = null;
    	String timezone = null;
    	if(CompanyType.COMPANY == party.getHandlingCompanyType()) {
    		LocationResponse location = kartoteksServiceClient.getLocation(party.getHandlingLocation().getId());
    		countryCode = location.getPostaladdress().getCountry().getIso();
    		postalCode = location.getPostaladdress().getPostalCode();
    		timezone = location.getTimezone();
    	} else if(CompanyType.CUSTOMS == party.getHandlingCompanyType()) {
    		CustomsOfficeLocationResponse location = locationServiceClient.getCustomsOfficeLocation(party.getHandlingLocation().getId());
    		countryCode = location.getCountry().getIso();
    		postalCode = location.getPostalCode();
    		timezone = location.getTimezone();
    	}
    	Map<String,String> result = new HashMap<>();
    	result.put("countryCode", countryCode);
    	result.put("postalCode", postalCode);
    	result.put("timezone", timezone);
    	return result;
    }

    private void setSubsidiaryCountryCode(Order order){
        SubsidiaryResponse subsidiaryResponse = authorizationServiceClient.getSubsidiary(order.getSubsidiary().getId());
        CompanyResponse companyResponse = kartoteksServiceClient.getCompany(subsidiaryResponse.getDefaultInvoiceCompany().getId());
        order.setCountryCode(companyResponse.getCountry().getIso());
    }

    @Transactional
    public List<Order> create(CreateOrderRequestJson createOrderRequest){
        List<Order> orders = new ArrayList<>();
        for(int i = 0; i < createOrderRequest.getNumberOfReplications(); i++){
            Order newOrder = createOrderRequest.getOrder().toEntity();
            newOrder.setStatus(createOrderRequest.isConfirmed() ? Status.CONFIRMED : Status.CREATED);
            orders.add(create(newOrder));
        }
        return orders;
    }

    @Transactional
    public Order create(Order order){

        orderValidator.validateNew(order);

        setVolumeAndLdmForShipmentUnits(order);
        orderValidator.validateMeasurements(order);

        shipmentLoadSpecsService.setLoadSpecsForOrder(order);
        orderValidator.validateLoadSpecs(order);

        setHandlingLocationAddressDetails(order);
        orderValidator.validateCustoms(order);

        saveDocuments(order);
        orderValidator.validateDocuments(order);

        setCustomerPartnership(order);
        setRegionsOfHandlingLocations(order);
        setSubsidiaryCountryCode(order);

        order.setCode(codeGenerator.getNewOrderCode());
        order.getShipments().forEach(orderShipment -> orderShipment.setCode(codeGenerator.getNewShipmentCode()));

        Order savedOrder = save(order);
        publisher.publishEvent(OrderEvent.with(savedOrder, Operation.CREATE));
        return savedOrder;
    }

    private void saveDocuments(Order order){
        //same file may be used more than once for different document types
        //and moving file to permanent space removes the file from temporary space
        //so we need to group documents by filename, and move each file to permanent space once.
        order.getDocuments().stream()
                .collect(Collectors.groupingBy(document -> document.getDocument().getSavedFileName()))
                .entrySet().forEach(fileNameAndDocuments -> {
                    String permanentPath = moveFileToPermanentSpace(fileNameAndDocuments.getKey());
                    fileNameAndDocuments.getValue().forEach(orderDocument -> orderDocument.getDocument().setPath(permanentPath));
                });

        order.getShipments().forEach(orderShipment -> {
            orderShipment.getDocuments().stream()
                    .collect(Collectors.groupingBy(document -> document.getDocument().getSavedFileName()))
                    .entrySet().forEach(fileNameAndDocuments -> {
                String permanentPath = moveFileToPermanentSpace(fileNameAndDocuments.getKey());
                fileNameAndDocuments.getValue().forEach(orderDocument -> orderDocument.getDocument().setPath(permanentPath));
            });
        });
    }
    private String moveFileToPermanentSpace(String temporaryFile){
        if(fileService.fileExistsInTempDir(temporaryFile)){
            return fileService.moveFileToPermDir(temporaryFile);
        }
        throw new ApplicationException("File {0} not found in temp directory", temporaryFile);
    }

    private Order save(Order order){
    	boolean initial = Objects.isNull(order.getId());
        Order savedOrder = orderRepository.save(order);
        List<OrderShipment> savedShipments = new ArrayList<>();
        order.getShipments().forEach(orderShipment -> {
            orderShipment.setOrder(savedOrder);
            savedShipments.add(save(orderShipment));

        });
        savedOrder.setShipments(savedShipments);

        saveOrderDocuments(order.getDocuments(), savedOrder);
        savedOrder.setInitial(initial);
        return savedOrder;
    }

    private OrderShipment save(OrderShipment shipment){
        OrderShipment savedShipment = shipmentRepository.save(shipment);

        saveShipmentUnits(shipment.getUnits(), savedShipment);
        saveShipmentAdrDetails(shipment.getAdrDetails(), savedShipment);
        saveShipmentVehicleRequirements(shipment.getVehicleRequirements(), savedShipment);
        saveShipmentEquipmentRequirements(shipment.getEquipmentRequirements(), savedShipment);
        saveShipmentCustoms(shipment.getDepartureCustoms(), shipment.getArrivalCustoms(), savedShipment);
        saveShipmentDocuments(shipment.getDocuments(), savedShipment);
        saveShipmentGoods(shipment.getDefinitionOfGoods(), savedShipment);

        return savedShipment;
    }

    private void saveShipmentGoods(List<OrderShipmentDefinitionOfGoods> definitionOfGoods, OrderShipment savedShipment) {
    	definitionOfGoods.forEach(orderShipmentDefinitionOfGoods -> {
    		orderShipmentDefinitionOfGoods.setShipment(savedShipment);
        });
        savedShipment.setDefinitionOfGoods(save(definitionOfGoods));
		
	}


	private void saveShipmentUnits(List<OrderShipmentUnit> units, OrderShipment savedShipment){
        List<OrderShipmentUnit> savedShipmentUnits = new ArrayList<>();
        units.forEach(orderShipmentUnit -> {
            orderShipmentUnit.setShipment(savedShipment);
            savedShipmentUnits.add(save(orderShipmentUnit));
        });
        savedShipment.setUnits(savedShipmentUnits);
    }

    private void saveShipmentAdrDetails(List<OrderShipmentAdr> adrDetails, OrderShipment savedShipment){
        List<OrderShipmentAdr> savedShipmentAdrDetails = new ArrayList<>();
        adrDetails.forEach(orderShipmentAdr -> {
            orderShipmentAdr.setShipment(savedShipment);
            savedShipmentAdrDetails.add(save(orderShipmentAdr));
        });
        savedShipment.setAdrDetails(savedShipmentAdrDetails);
    }

    private void saveShipmentVehicleRequirements(List<OrderShipmentVehicleRequirement> requirements, OrderShipment savedShipment){
        List<OrderShipmentVehicleRequirement> savedShipmentRequirements = new ArrayList<>();
        requirements.forEach(requirement -> {
            requirement.setShipment(savedShipment);
            savedShipmentRequirements.add(save(requirement));
        });
        savedShipment.setVehicleRequirements(savedShipmentRequirements);
    }

    private void saveShipmentEquipmentRequirements(List<OrderShipmentEquipmentRequirement> requirements, OrderShipment savedShipment){
        List<OrderShipmentEquipmentRequirement> savedShipmentRequirements = new ArrayList<>();
        requirements.forEach(requirement -> {
            requirement.setShipment(savedShipment);
            savedShipmentRequirements.add(save(requirement));
        });
        savedShipment.setEquipmentRequirements(savedShipmentRequirements);
    }

    private void saveShipmentCustoms(OrderShipmentDepartureCustoms departureCustoms, OrderShipmentArrivalCustoms arrivalCustoms, OrderShipment savedShipment){
        if(departureCustoms != null){
            departureCustoms.setShipment(savedShipment);
            savedShipment.setDepartureCustoms(save(departureCustoms));
        }
        if(arrivalCustoms != null){
            arrivalCustoms.setShipment(savedShipment);
            savedShipment.setArrivalCustoms(save(arrivalCustoms));
        }
    }

    private void saveOrderDocuments(List<OrderDocument> documents, Order savedOrder){
        List<OrderDocument> savedDocuments = new ArrayList<>();
        documents.forEach(document -> {
            document.setOrder(savedOrder);
            savedDocuments.add(save(document));
        });
        savedOrder.setDocuments(savedDocuments);
    }

    private void saveShipmentDocuments(List<OrderShipmentDocument> documents, OrderShipment savedShipment){
        List<OrderShipmentDocument> savedDocuments = new ArrayList<>();
        documents.forEach(document -> {
            document.setShipment(savedShipment);
            savedDocuments.add(save(document));
        });
        savedShipment.setDocuments(savedDocuments);
    }
    
    private List<OrderShipmentDefinitionOfGoods> save(List<OrderShipmentDefinitionOfGoods> orderShipmentDefinitionOfGoods) {
    	return StreamSupport.stream(Optional.ofNullable(orderShipmentDefinitionOfGoodsRepository.save(orderShipmentDefinitionOfGoods)).orElseGet(ArrayList::new).spliterator(), true).collect(toList());
    }

    private OrderShipmentUnit save(OrderShipmentUnit shipmentUnit){
        return shipmentUnitRepository.save(shipmentUnit);
    }

    private OrderShipmentAdr save(OrderShipmentAdr shipmentAdr){
        return shipmentAdrRepository.save(shipmentAdr);
    }

    private OrderShipmentVehicleRequirement save(OrderShipmentVehicleRequirement requirement){
        return shipmentVehicleRequirementRepository.save(requirement);
    }
    private OrderShipmentEquipmentRequirement save(OrderShipmentEquipmentRequirement requirement){
        return shipmentEquipmentRequirementRepository.save(requirement);
    }
    private OrderDocument save(OrderDocument document){
        return orderDocumentRepository.save(document);
    }
    private OrderShipmentDocument save(OrderShipmentDocument document){
        return orderShipmentDocumentRepository.save(document);
    }

    private OrderShipmentArrivalCustoms save(OrderShipmentArrivalCustoms customs){
        return orderShipmentArrivalCustomsRepository.save(customs);
    }
    private OrderShipmentDepartureCustoms save(OrderShipmentDepartureCustoms customs){
        return orderShipmentDepartureCustomsRepository.save(customs);
    }
}
