package ekol.orders.order.service;

import static ekol.orders.order.builder.MockOrderData.adrDetails1;
import static ekol.orders.order.builder.MockOrderData.boxRequirement;
import static ekol.orders.order.builder.MockOrderData.consignee1;
import static ekol.orders.order.builder.MockOrderData.customsArrivalTR;
import static ekol.orders.order.builder.MockOrderData.customsDepartureEU;
import static ekol.orders.order.builder.MockOrderData.equipmentRequirement;
import static ekol.orders.order.builder.MockOrderData.invoiceOrderDocument;
import static ekol.orders.order.builder.MockOrderData.newShipment1;
import static ekol.orders.order.builder.MockOrderData.pallet1;
import static ekol.orders.order.builder.MockOrderData.sender1;
import static ekol.orders.order.builder.MockOrderData.validNewOrder;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import ekol.exceptions.ValidationException;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.orders.order.builder.CompanyResponseBuilder;
import ekol.orders.order.builder.CountryBuilder;
import ekol.orders.order.builder.DocumentBuilder;
import ekol.orders.order.builder.LocationResponseBuilder;
import ekol.orders.order.builder.OrderBuilder;
import ekol.orders.order.builder.OrderDocumentBuilder;
import ekol.orders.order.builder.OrderShipmentDocumentBuilder;
import ekol.orders.order.builder.PostalAddressBuilder;
import ekol.orders.order.builder.RegionResponseBuilder;
import ekol.orders.order.builder.ShipmentLoadSpecRuleResponseBuilder;
import ekol.orders.order.builder.SubsidiaryResponseBuilder;
import ekol.orders.order.builder.UnitLoadSpecRuleResponseBuilder;
import ekol.orders.order.domain.Document;
import ekol.orders.order.domain.IdNameEmbeddable;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderDocument;
import ekol.orders.order.domain.OrderShipment;
import ekol.orders.order.domain.OrderShipmentAdr;
import ekol.orders.order.domain.OrderShipmentArrivalCustoms;
import ekol.orders.order.domain.OrderShipmentDefinitionOfGoods;
import ekol.orders.order.domain.OrderShipmentDepartureCustoms;
import ekol.orders.order.domain.OrderShipmentDocument;
import ekol.orders.order.domain.OrderShipmentEquipmentRequirement;
import ekol.orders.order.domain.OrderShipmentUnit;
import ekol.orders.order.domain.OrderShipmentVehicleRequirement;
import ekol.orders.order.domain.ShipmentHandlingParty;
import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import ekol.orders.order.domain.dto.response.location.RegionResponse;
import ekol.orders.order.event.OrderEvent;
import ekol.orders.order.repository.OrderDocumentRepository;
import ekol.orders.order.repository.OrderRepository;
import ekol.orders.order.repository.OrderShipmentAdrRepository;
import ekol.orders.order.repository.OrderShipmentArrivalCustomsRepository;
import ekol.orders.order.repository.OrderShipmentDefinitionOfGoodsRepository;
import ekol.orders.order.repository.OrderShipmentDepartureCustomsRepository;
import ekol.orders.order.repository.OrderShipmentDocumentRepository;
import ekol.orders.order.repository.OrderShipmentEquipmentRequirementRepository;
import ekol.orders.order.repository.OrderShipmentRepository;
import ekol.orders.order.repository.OrderShipmentUnitRepository;
import ekol.orders.order.repository.OrderShipmentVehicleRequirementRepository;
import ekol.orders.order.validator.OrderValidator;
import ekol.resource.service.FileService;

@RunWith(SpringRunner.class)
public class CreateOrderServiceUnitTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderShipmentRepository shipmentRepository;

    @MockBean
    private OrderShipmentUnitRepository shipmentUnitRepository;

    @MockBean
    private OrderShipmentAdrRepository shipmentAdrRepository;

    @MockBean
    private OrderShipmentVehicleRequirementRepository vehicleRequirementRepository;

    @MockBean
    private OrderShipmentEquipmentRequirementRepository equipmentRequirementRepository;

    @MockBean
    private OrderDocumentRepository orderDocumentRepository;

    @MockBean
    private OrderShipmentDocumentRepository shipmentDocumentRepository;

    @MockBean
    private OrderShipmentArrivalCustomsRepository shipmentArrivalCustomsRepository;

    @MockBean
    private OrderShipmentDepartureCustomsRepository shipmentDepartureCustomsRepository;
    
    @MockBean
    private OrderShipmentDefinitionOfGoodsRepository orderShipmentDefinitionOfGoodsRepository;

    @MockBean
    private OrderValidator orderValidator;

    @MockBean
    private KartoteksServiceClient kartoteksServiceClient;

    @MockBean
    private OrderTemplateServiceClient orderTemplateServiceClient;

    @MockBean
    private LocationServiceClient locationServiceClient;

    @MockBean
    private AuthorizationServiceClient authorizationServiceClient;

    @MockBean
    private LoadingMeterCalculator loadingMeterCalculator;

    @MockBean
    private PayWeightCalculator payWeightCalculator;

    @MockBean
    private FileService fileService;

    @MockBean
    private ApplicationEventPublisher publisher;

    @MockBean
    private CodeGenerator codeGenerator;

    private CreateOrderService service;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<OrderShipment> orderShipmentCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentUnit> orderShipmentUnitCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentAdr> orderShipmentAdrCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentVehicleRequirement> vehicleRequirementCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentEquipmentRequirement> equipmentRequirementCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentArrivalCustoms> orderShipmentArrivalCustomsCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentDepartureCustoms> orderShipmentDepartureCustomsCaptor;

    @Captor
    private ArgumentCaptor<OrderDocument> orderDocumentCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentDocument> orderShipmentDocumentCaptor;

    @Captor
    private ArgumentCaptor<OrderShipmentDefinitionOfGoods> orderShipmentDefinitionOfGoodsCaptor;

    @Before
    public void init(){
        OrderShipmentLoadSpecsService shipmentLoadSpecsService = new OrderShipmentLoadSpecsService(orderTemplateServiceClient);
        this.service = new CreateOrderService(orderRepository,
                shipmentRepository,
                shipmentUnitRepository,
                shipmentAdrRepository,
                vehicleRequirementRepository,
                equipmentRequirementRepository,
                orderDocumentRepository,
                shipmentDocumentRepository,
                shipmentArrivalCustomsRepository,
                shipmentDepartureCustomsRepository,
                orderShipmentDefinitionOfGoodsRepository,
                orderValidator,
                kartoteksServiceClient,
                shipmentLoadSpecsService,
                locationServiceClient,
                authorizationServiceClient,
                loadingMeterCalculator,
                payWeightCalculator,
                fileService,
                publisher,
                codeGenerator);


        given(orderRepository.save(any(Order.class))).willReturn(validNewOrder().but().withId(1L).build());
        given(shipmentRepository.save(any(OrderShipment.class))).willReturn(newShipment1().withId(1L).build());
        given(shipmentUnitRepository.save(any(OrderShipmentUnit.class))).willReturn(pallet1().withId(1L).build());
        given(shipmentAdrRepository.save(any(OrderShipmentAdr.class))).willReturn(adrDetails1().withId(1L).build());
        given(shipmentArrivalCustomsRepository.save(any(OrderShipmentArrivalCustoms.class))).willReturn(customsArrivalTR().withId(1L).build());
        given(shipmentDepartureCustomsRepository.save(any(OrderShipmentDepartureCustoms.class))).willReturn(customsDepartureEU().withId(1L).build());
        given(vehicleRequirementRepository.save(any(OrderShipmentVehicleRequirement.class))).willReturn(boxRequirement().withId(1L).build());
        given(equipmentRequirementRepository.save(any(OrderShipmentEquipmentRequirement.class))).willReturn(equipmentRequirement().withId(1L).build());
        given(orderDocumentRepository.save(any(OrderDocument.class))).willReturn(invoiceOrderDocument().withId(1L).build());
        given(shipmentDocumentRepository.save(any(OrderShipmentDocument.class))).willAnswer(m -> {
                    OrderShipmentDocument document = m.getArgumentAt(0, OrderShipmentDocument.class);
                    document.setId(1L);
                    return document;
                });

        given(kartoteksServiceClient.isCompanyPartner(1L)).willReturn(true);
        given(orderTemplateServiceClient.runLoadSpecRulesForShipment(
                any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .willReturn(ShipmentLoadSpecRuleResponseBuilder.aShipmentLoadSpecRuleResponse().build());
        given(orderTemplateServiceClient.runLoadSpecRulesForUnit(
                any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .willReturn(UnitLoadSpecRuleResponseBuilder.anUnitLoadSpecRuleResponse().build());

        given(kartoteksServiceClient.getLocation(anyLong())).willReturn(
                LocationResponseBuilder.aLocationResponse()
                        .withId(1L).withName("location 1")
                        .withTimezone("timezone")
                        .withPostaladdress(PostalAddressBuilder.aPostalAddress()
                                .withCountry(CountryBuilder.aCountry().withIso("AA").build())
                                .withPostalCode("34567")
                                .build()
                        ).build()
        );

        given(authorizationServiceClient.getSubsidiary(anyLong()))
                .willReturn(SubsidiaryResponseBuilder.aSubsidiaryResponse().withDefaultInvoiceCompany(IdNamePair.with(1000L, "subsidiary")).build());
        given(kartoteksServiceClient.getCompany(1000L))
                .willReturn(CompanyResponseBuilder.aCompanyResponse().withCountry(
                        CountryBuilder.aCountry().withIso("TR").build()).build());

        given(fileService.fileExistsInTempDir(anyString())).willReturn(true);
        given(fileService.moveFileToPermDir(anyString())).willAnswer(m -> "/path/to/" + m.getArgumentAt(0, String.class));

        given(codeGenerator.getNewOrderCode()).willReturn("000001");
        given(codeGenerator.getNewShipmentCode()).willReturn("000111");
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenGetNewOrderCode() {
        String orderCode = "3456789";
        OrderBuilder order = validNewOrder();
        given(codeGenerator.getNewOrderCode()).willReturn(orderCode);

        service.create(order.build());

        then(orderRepository).should(times(1)).save(orderCaptor.capture());
        assertEquals(orderCode, orderCaptor.getValue().getCode());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenGetNewShipmentCode() {
        String shipmentCode = "8578098";
        OrderBuilder order = validNewOrder();
        given(codeGenerator.getNewShipmentCode()).willReturn(shipmentCode);

        service.create(order.build());

        then(shipmentRepository).should(times(1)).save(orderShipmentCaptor.capture());
        assertEquals(shipmentCode, orderShipmentCaptor.getValue().getCode());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetVolumeForEachUnit() {
        OrderBuilder order = validNewOrder();
        BigDecimal volume = new BigDecimal(12.22);
        given(loadingMeterCalculator.calculateVolume(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class), anyInt()))
                .willReturn(volume);

        service.create(order.build());

        then(shipmentUnitRepository).should(times(1)).save(orderShipmentUnitCaptor.capture());
        orderShipmentUnitCaptor.getAllValues().forEach(shipmentUnit -> assertEquals(volume, shipmentUnit.getVolume()));
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetLdmForEachUnit() {
        OrderBuilder order = validNewOrder();
        BigDecimal ldm = new BigDecimal(6.08);
        given(loadingMeterCalculator.calculateLoadingMeter(any(BigDecimal.class), any(BigDecimal.class), anyInt(), anyInt()))
                .willReturn(ldm);

        service.create(order.build());

        then(shipmentUnitRepository).should(times(1)).save(orderShipmentUnitCaptor.capture());
        assertEquals(ldm, orderShipmentUnitCaptor.getValue().getLdm());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetLoadSpecsForEachShipment() {
        OrderBuilder order = validNewOrder();

        given(orderTemplateServiceClient.runLoadSpecRulesForShipment(
                any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .willReturn(ShipmentLoadSpecRuleResponseBuilder.aShipmentLoadSpecRuleResponse()
                        .withHeavyLoad(true).withValuableLoad(true).build());

        service.create(order.build());

        then(shipmentRepository).should(times(1)).save(orderShipmentCaptor.capture());
        assertTrue(orderShipmentCaptor.getValue().isHeavyLoad());
        assertTrue(orderShipmentCaptor.getValue().isValuableLoad());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetLoadSpecsForEachUnit() {
        OrderBuilder order = validNewOrder();

        given(orderTemplateServiceClient.runLoadSpecRulesForUnit(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .willReturn(UnitLoadSpecRuleResponseBuilder.anUnitLoadSpecRuleResponse()
                        .withLongLoad(true).withOversizeLoad(true).build());

        service.create(order.build());

        then(shipmentUnitRepository).should(times(1)).save(orderShipmentUnitCaptor.capture());
        assertTrue(orderShipmentUnitCaptor.getValue().isLongLoad());
        assertTrue(orderShipmentUnitCaptor.getValue().isOversizeLoad());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetHandlingLocationDetails() {
        Long senderLocationId = 345678L;
        Long consigneeLocationId = 98753L;
        OrderBuilder order = validNewOrder().withShipments(
                Collections.singletonList(newShipment1()
                        .withSender(sender1()
                                .withHandlingLocation(IdNameEmbeddable.with(senderLocationId, "location"))
                                .withHandlingLocationCountryCode(null)
                                .withHandlingLocationPostalCode(null)
                                .build())
                        .withConsignee(consignee1()
                                .withHandlingLocation(IdNameEmbeddable.with(consigneeLocationId, "location"))
                                .withHandlingLocationCountryCode(null)
                                .withHandlingLocationPostalCode(null)
                                .build())
                        .build())
        );
        String senderCountryCode = "AA";
        String senderPostalCode = "12333";
        LocationResponse senderLocationResponse = LocationResponseBuilder.aLocationResponse()
                .withId(1L).withName("location 1")
                .withTimezone("timezone 1")
                .withPostaladdress(PostalAddressBuilder.aPostalAddress()
                        .withCountry(CountryBuilder.aCountry().withIso(senderCountryCode).build())
                        .withPostalCode(senderPostalCode)
                        .build()
                ).build();

        String consigneeCountryCode = "BB";
        String consigneePostalCode = "87567";
        LocationResponse consigneeLocationResponse = LocationResponseBuilder.aLocationResponse()
                .withId(1L).withName("location 1")
                .withTimezone("timezone 1")
                .withPostaladdress(PostalAddressBuilder.aPostalAddress()
                        .withCountry(CountryBuilder.aCountry().withIso(consigneeCountryCode).build())
                        .withPostalCode(consigneePostalCode)
                        .build()
                ).build();

        given(kartoteksServiceClient.getLocation(senderLocationId)).willReturn(senderLocationResponse);
        given(kartoteksServiceClient.getLocation(consigneeLocationId)).willReturn(consigneeLocationResponse);

        service.create(order.build());

        then(shipmentRepository).should(times(1)).save(orderShipmentCaptor.capture());
        assertEquals(senderCountryCode, orderShipmentCaptor.getValue().getSender().getHandlingLocationCountryCode());
        assertEquals(senderPostalCode, orderShipmentCaptor.getValue().getSender().getHandlingLocationPostalCode());
        assertEquals(consigneeCountryCode, orderShipmentCaptor.getValue().getConsignee().getHandlingLocationCountryCode());
        assertEquals(consigneePostalCode, orderShipmentCaptor.getValue().getConsignee().getHandlingLocationPostalCode());
    }


    @Test
    public void givenValidOrder_whenCreateOrder_thenMoveDocumentsToPermanentSpace() {
        OrderBuilder order = validNewOrder().withDocuments(
                Arrays.asList(
                        OrderDocumentBuilder.anOrderDocument()
                                .withDocument(DocumentBuilder.aDocument()
                                        .withOriginalFileName("order-document-1")
                                        .withSavedFileName("order-document-1-saved").build())
                                .build(),
                        OrderDocumentBuilder.anOrderDocument()
                                .withDocument(DocumentBuilder.aDocument()
                                        .withOriginalFileName("order-document-2")
                                        .withSavedFileName("order-document-2-saved").build())
                                .build()
                )
        ).withShipments(
                Collections.singletonList(
                        newShipment1().withDocuments(
                                Arrays.asList(
                                        OrderShipmentDocumentBuilder.anOrderShipmentDocument()
                                                .withDocument(DocumentBuilder.aDocument()
                                                        .withOriginalFileName("shipment-document-1")
                                                        .withSavedFileName("shipment-document-1-saved").build())
                                                .build(),
                                        OrderShipmentDocumentBuilder.anOrderShipmentDocument()
                                                .withDocument(DocumentBuilder.aDocument()
                                                        .withOriginalFileName("shipment-document-2")
                                                        .withSavedFileName("shipment-document-2-saved").build())
                                                .build()
                                )
                        ).build()
                )
        );

        given(fileService.fileExistsInTempDir(anyString())).willReturn(true);
        given(fileService.moveFileToPermDir(anyString())).willAnswer(m -> "/perm/path/" + m.getArgumentAt(0, String.class));

        service.create(order.build());

        then(orderDocumentRepository).should(times(2)).save(orderDocumentCaptor.capture());
        then(shipmentDocumentRepository).should(times(2)).save(orderShipmentDocumentCaptor.capture());
        assertEquals(2, orderDocumentCaptor.getAllValues().size());
        assertEquals("/perm/path/order-document-1-saved", orderDocumentCaptor.getAllValues().get(0).getDocument().getPath());
        assertEquals("/perm/path/order-document-2-saved", orderDocumentCaptor.getAllValues().get(1).getDocument().getPath());
        assertEquals(2, orderShipmentDocumentCaptor.getAllValues().size());
        assertEquals("/perm/path/shipment-document-1-saved", orderShipmentDocumentCaptor.getAllValues().get(0).getDocument().getPath());
        assertEquals("/perm/path/shipment-document-2-saved", orderShipmentDocumentCaptor.getAllValues().get(1).getDocument().getPath());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetCustomerPartnership() {
        Long customerId = 45393L;
        OrderBuilder order = validNewOrder()
                .withPartnerCustomer(false)
                .withCustomer(IdNameEmbeddable.with(customerId, "customer"));

        given(kartoteksServiceClient.isCompanyPartner(customerId)).willReturn(true);

        service.create(order.build());

        then(orderRepository).should(times(1)).save(orderCaptor.capture());
        assertTrue(orderCaptor.getValue().isPartnerCustomer());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetHandlingRegions() {
        Long senderLocationId = 45393L;
        Long consigneeLocationId = 56789L;
        OrderBuilder order = validNewOrder().withShipments(
                Collections.singletonList(newShipment1()
                        .withSender(
                                sender1().withHandlingLocation(IdNameEmbeddable.with(senderLocationId, "location"))
                                        .withHandlingOperationRegion(null)
                                        .withHandlingRegion(null)
                                        .withHandlingRegionCategory(null)
                                        .build()
                        ).withConsignee(
                                consignee1().withHandlingLocation(IdNameEmbeddable.with(consigneeLocationId, "location"))
                                        .withHandlingOperationRegion(null)
                                        .withHandlingRegion(null)
                                        .withHandlingRegionCategory(null)
                                        .build()
                        ).build()
                )
        );
        RegionResponse senderRegionResponse = RegionResponseBuilder.aRegionResponse()
                .withId(1L).withName("region 1")
                .withCategory(CodeNamePair.with("A","A"))
                .withOperationRegion(IdNamePair.with(11L, "op region 1"))
                .build();

        RegionResponse consigneeRegionResponse = RegionResponseBuilder.aRegionResponse()
                .withId(2L).withName("region 2")
                .withCategory(CodeNamePair.with("B","B"))
                .withOperationRegion(IdNamePair.with(22L, "op region 2"))
                .build();
        given(locationServiceClient.getCollectionRegionOfCompanyLocation(senderLocationId))
                .willReturn(senderRegionResponse);

        given(locationServiceClient.getDistributionRegionOfCompanyLocation(consigneeLocationId))
                .willReturn(consigneeRegionResponse);

        service.create(order.build());

        then(shipmentRepository).should(times(1)).save(orderShipmentCaptor.capture());

        assertEquals(senderRegionResponse.getId(), orderShipmentCaptor.getValue().getSender().getHandlingRegion().getId());
        assertEquals(senderRegionResponse.getName(), orderShipmentCaptor.getValue().getSender().getHandlingRegion().getName());
        assertEquals(senderRegionResponse.getCategory().getCode(), orderShipmentCaptor.getValue().getSender().getHandlingRegionCategory().getCode());
        assertEquals(senderRegionResponse.getCategory().getName(), orderShipmentCaptor.getValue().getSender().getHandlingRegionCategory().getName());
        assertEquals(senderRegionResponse.getOperationRegion().getId(), orderShipmentCaptor.getValue().getSender().getHandlingOperationRegion().getId());
        assertEquals(senderRegionResponse.getOperationRegion().getName(), orderShipmentCaptor.getValue().getSender().getHandlingOperationRegion().getName());

        assertEquals(consigneeRegionResponse.getId(), orderShipmentCaptor.getValue().getConsignee().getHandlingRegion().getId());
        assertEquals(consigneeRegionResponse.getName(), orderShipmentCaptor.getValue().getConsignee().getHandlingRegion().getName());
        assertEquals(consigneeRegionResponse.getCategory().getCode(), orderShipmentCaptor.getValue().getConsignee().getHandlingRegionCategory().getCode());
        assertEquals(consigneeRegionResponse.getCategory().getName(), orderShipmentCaptor.getValue().getConsignee().getHandlingRegionCategory().getName());
        assertEquals(consigneeRegionResponse.getOperationRegion().getId(), orderShipmentCaptor.getValue().getConsignee().getHandlingOperationRegion().getId());
        assertEquals(consigneeRegionResponse.getOperationRegion().getName(), orderShipmentCaptor.getValue().getConsignee().getHandlingOperationRegion().getName());
    }


    @Test
    public void givenValidOrder_whenCreateOrder_thenSetCrossDockInfo() {
        Long senderLocationId = 45393L;
        Long consigneeLocationId = 56789L;
        OrderBuilder order = validNewOrder().withShipments(
                Collections.singletonList(newShipment1()
                        .withSender(
                                sender1().withHandlingLocation(IdNameEmbeddable.with(senderLocationId, "location"))
                                        .withHandlingAtCrossDock(false)
                                        .build()
                        ).withConsignee(
                                consignee1().withHandlingLocation(IdNameEmbeddable.with(consigneeLocationId, "location"))
                                        .withHandlingAtCrossDock(false)
                                        .build()
                        ).build()
                )
        );

        given(locationServiceClient.isCrossDockWarehouse(senderLocationId))
                .willReturn(true);

        given(locationServiceClient.isCrossDockWarehouse(consigneeLocationId))
                .willReturn(true);

        service.create(order.build());

        then(shipmentRepository).should(times(1)).save(orderShipmentCaptor.capture());

        assertTrue(orderShipmentCaptor.getValue().getSender().isHandlingAtCrossDock());
        assertTrue(orderShipmentCaptor.getValue().getConsignee().isHandlingAtCrossDock());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSetOrderCountryCode() {
        String countryCode = "AA";
        OrderBuilder order = validNewOrder();

        given(authorizationServiceClient.getSubsidiary(order.build().getSubsidiary().getId()))
                .willReturn(SubsidiaryResponseBuilder.aSubsidiaryResponse().withDefaultInvoiceCompany(IdNamePair.with(1L, "subsidiary")).build());

        given(kartoteksServiceClient.getCompany(1L))
                .willReturn(CompanyResponseBuilder.aCompanyResponse().withCountry(CountryBuilder.aCountry().withIso(countryCode).build()).build());

        service.create(order.build());

        then(orderRepository).should(times(1)).save(orderCaptor.capture());

        assertEquals(countryCode, orderCaptor.getValue().getCountryCode());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveOrder() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(orderRepository).should(times(1)).save(orderCaptor.capture());

        assertNull(orderCaptor.getValue().getId());
        assertCapturedOrderEquals(order.build(), orderCaptor.getValue());
    }
    private void assertCapturedOrderEquals(Order expected, Order captured){
        assertEquals(expected.getCustomer().getId(), captured.getCustomer().getId());
        assertEquals(expected.getSubsidiary().getId(), captured.getSubsidiary().getId());
        assertEquals(expected.getServiceType(), captured.getServiceType());
        assertEquals(expected.getTruckLoadType(), captured.getTruckLoadType());
        assertEquals(expected.getShipments().size(), captured.getShipments().size());

    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipments() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(shipmentRepository).should(times(1)).save(orderShipmentCaptor.capture());

        assertCapturedShipmentEquals(order.build().getShipments().get(0), orderShipmentCaptor.getValue());
    }
    private void assertCapturedShipmentEquals(OrderShipment expected, OrderShipment captured){
        assertEquals(expected.getIncoterm().getId(), captured.getIncoterm().getId());
        assertEquals(expected.getReadyAtDate(), captured.getReadyAtDate());
        assertCapturedHandlingPartyEquals(expected.getSender(), captured.getSender());
        assertCapturedHandlingPartyEquals(expected.getConsignee(), captured.getConsignee());

    }
    private void assertCapturedHandlingPartyEquals(ShipmentHandlingParty expected, ShipmentHandlingParty captured){
        assertEquals(expected.getCompany(), captured.getCompany());
        assertEquals(expected.getHandlingCompany(), captured.getHandlingCompany());
        assertEquals(expected.getHandlingLocation(), captured.getHandlingLocation());
        assertEquals(expected.getCompanyContact(), captured.getCompanyContact());
        assertEquals(expected.getHandlingContact(), captured.getHandlingContact());
    }
    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipmentUnits() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(shipmentUnitRepository).should(times(1)).save(orderShipmentUnitCaptor.capture());

        assertCapturedShipmentUnitEquals(order.build().getShipments().get(0).getUnits().get(0), orderShipmentUnitCaptor.getValue());
    }
    private void assertCapturedShipmentUnitEquals(OrderShipmentUnit expected, OrderShipmentUnit captured){
        assertEquals(expected.getQuantity(), captured.getQuantity());
        assertEquals(expected.getPackageType().getId(), captured.getPackageType().getId());
        assertEquals(expected.getWidth(), captured.getWidth());
        assertEquals(expected.getLength(), captured.getLength());
        assertEquals(expected.getHeight(), captured.getHeight());
        assertEquals(expected.getStackSize(), captured.getStackSize());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipmentAdrDetails() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(shipmentAdrRepository).should(times(1)).save(orderShipmentAdrCaptor.capture());

        assertCapturedShipmentAdrDetailsEquals(order.build().getShipments().get(0).getAdrDetails().get(0), orderShipmentAdrCaptor.getValue());
    }
    private void assertCapturedShipmentAdrDetailsEquals(OrderShipmentAdr expected, OrderShipmentAdr captured){
        assertEquals(expected.getQuantity(), captured.getQuantity());
        assertEquals(expected.getInnerQuantity(), captured.getInnerQuantity());
        assertEquals(expected.getPackageType(), captured.getPackageType());
        assertEquals(expected.getInnerPackageType(), captured.getInnerPackageType());
        assertEquals(expected.getAmount(), captured.getAmount());
        assertEquals(expected.getUnit(), captured.getUnit());
        assertEquals(expected.getAdrClassDetails().getId(), captured.getAdrClassDetails().getId());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipmentCustomsDetails() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(shipmentArrivalCustomsRepository).should(times(1)).save(orderShipmentArrivalCustomsCaptor.capture());
        then(shipmentDepartureCustomsRepository).should(times(1)).save(orderShipmentDepartureCustomsCaptor.capture());

        assertCapturedShipmentDepartureCustomsEquals(order.build().getShipments().get(0).getDepartureCustoms(), orderShipmentDepartureCustomsCaptor.getValue());
        assertCapturedShipmentArrivalCustomsEquals(order.build().getShipments().get(0).getArrivalCustoms(), orderShipmentArrivalCustomsCaptor.getValue());
    }
    private void assertCapturedShipmentDepartureCustomsEquals(OrderShipmentDepartureCustoms expected, OrderShipmentDepartureCustoms captured){
        assertEquals(expected.getCustomsAgent(), captured.getCustomsAgent());
        assertEquals(expected.getCustomsAgentLocation(), captured.getCustomsAgentLocation());
    }
    private void assertCapturedShipmentArrivalCustomsEquals(OrderShipmentArrivalCustoms expected, OrderShipmentArrivalCustoms captured){
        assertEquals(expected.getCustomsOffice(), captured.getCustomsOffice());
        assertEquals(expected.getCustomsType(), captured.getCustomsType());
        assertEquals(expected.getCustomsLocation(), captured.getCustomsLocation());
        assertEquals(expected.getCustomsAgent(), captured.getCustomsAgent());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipmentVehicleRequirements() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(vehicleRequirementRepository).should(times(1)).save(vehicleRequirementCaptor.capture());

        assertCapturedShipmentVehicleRequirementEquals(
                order.build().getShipments().get(0).getVehicleRequirements().get(0), vehicleRequirementCaptor.getValue());
    }
    private void assertCapturedShipmentVehicleRequirementEquals(OrderShipmentVehicleRequirement expected, OrderShipmentVehicleRequirement captured){
        assertEquals(expected.getOperationType(), captured.getOperationType());
        assertEquals(expected.getRequirement(), captured.getRequirement());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipmentEquipmentRequirements() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(equipmentRequirementRepository).should(times(1)).save(equipmentRequirementCaptor.capture());

        assertCapturedShipmentEquipmentRequirementEquals(
                order.build().getShipments().get(0).getEquipmentRequirements().get(0), equipmentRequirementCaptor.getValue());
    }
    private void assertCapturedShipmentEquipmentRequirementEquals(OrderShipmentEquipmentRequirement expected, OrderShipmentEquipmentRequirement captured){
        assertEquals(expected.getEquipment().getId(), captured.getEquipment().getId());
        assertEquals(expected.getCount(), captured.getCount());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveOrderDocuments() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(orderDocumentRepository).should(times(1)).save(orderDocumentCaptor.capture());

        assertCapturedDocumentEquals(order.build().getDocuments().get(0).getDocument(), orderDocumentCaptor.getValue().getDocument());
    }
    private void assertCapturedDocumentEquals(Document expected, Document captured){
        assertEquals(expected.getType().getId(), captured.getType().getId());
        assertEquals(expected.getSavedFileName(), captured.getSavedFileName());
        assertEquals(expected.getOriginalFileName(), captured.getOriginalFileName());
        assertEquals("/path/to/" + expected.getSavedFileName(), captured.getPath());
    }


    @Test
    public void givenValidOrder_whenCreateOrder_thenSaveShipmentDocuments() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(shipmentDocumentRepository).should(times(3)).save(orderShipmentDocumentCaptor.capture());

        assertCapturedDocumentEquals(order.build().getShipments().get(0).getDocuments().get(0).getDocument(),
                orderShipmentDocumentCaptor.getAllValues().get(0).getDocument());

        assertCapturedDocumentEquals(order.build().getShipments().get(0).getDocuments().get(1).getDocument(),
                orderShipmentDocumentCaptor.getAllValues().get(1).getDocument());

        assertCapturedDocumentEquals(order.build().getShipments().get(0).getDocuments().get(2).getDocument(),
                orderShipmentDocumentCaptor.getAllValues().get(2).getDocument());
    }

    @Test
    public void givenValidOrder_whenCreateOrder_thenPublishEvent() {
        OrderBuilder order = validNewOrder();

        service.create(order.build());

        then(publisher).should(times(1)).publishEvent(any(OrderEvent.class));
    }

    private void thenNothingSaved(){
        then(orderRepository).should(never()).save(any(Order.class));
        then(shipmentRepository).should(never()).save(any(OrderShipment.class));
        then(shipmentUnitRepository).should(never()).save(any(OrderShipmentUnit.class));
        then(shipmentAdrRepository).should(never()).save(any(OrderShipmentAdr.class));
        then(shipmentArrivalCustomsRepository).should(never()).save(any(OrderShipmentArrivalCustoms.class));
        then(shipmentDepartureCustomsRepository).should(never()).save(any(OrderShipmentDepartureCustoms.class));
        then(vehicleRequirementRepository).should(never()).save(any(OrderShipmentVehicleRequirement.class));
        then(equipmentRequirementRepository).should(never()).save(any(OrderShipmentEquipmentRequirement.class));
        then(shipmentDocumentRepository).should(never()).save(any(OrderShipmentDocument.class));
        then(orderDocumentRepository).should(never()).save(any(OrderDocument.class));
    }

    @Test(expected = ValidationException.class)
    public void givenValidationFailed_whenCreateOrder_thenThrowException() {
        Order order = validNewOrder().build();
        willThrow(new ValidationException("exception")).given(orderValidator).validateNew(order);

        service.create(order);

        thenNothingSaved();
    }

    @Test(expected = ValidationException.class)
    public void givenMeasurementsValidationFailed_whenCreateOrder_thenThrowException() {
        Order order = validNewOrder().build();
        willThrow(new ValidationException("exception")).given(orderValidator).validateMeasurements(order);

        service.create(order);

        thenNothingSaved();
    }

    @Test(expected = ValidationException.class)
    public void givenLoadSpecsValidationFailed_whenCreateOrder_thenThrowException() {
        Order order = validNewOrder().build();
        willThrow(new ValidationException("exception")).given(orderValidator).validateLoadSpecs(order);

        service.create(order);

        thenNothingSaved();
    }

    @Test(expected = ValidationException.class)
    public void givenCustomsValidationFailed_whenCreateOrder_thenThrowException() {
        Order order = validNewOrder().build();
        willThrow(new ValidationException("exception")).given(orderValidator).validateCustoms(order);

        service.create(order);

        thenNothingSaved();

    }
}
