package ekol.orders.order.controller;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekol.model.IdNamePair;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.builder.CreateOrderRequestJsonBuilder;
import ekol.orders.order.builder.MockOrderData;
import ekol.orders.order.builder.OrderJsonBuilder;
import ekol.orders.order.builder.OrderShipmentJsonBuilder;
import ekol.orders.order.builder.ShipmentHandlingPartyJsonBuilder;
import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.Status;
import ekol.orders.order.domain.dto.json.CreateOrderRequestJson;
import ekol.orders.order.domain.dto.json.IdCodeNameTrio;
import ekol.orders.order.service.CreateOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @MockBean
    private CreateOrderService createOrderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    private Order savedOrder = MockOrderData.validNewOrder()
            .withId(1L).withStatus(Status.CREATED)
            .withShipments(Collections.singletonList(MockOrderData.newShipment1().withId(1L).build()))
            .build();

    private IdNamePair senderCompany = new IdNamePair(100L, "sender 1");
    private IdNamePair senderLocation = new IdNamePair(200L, "sender location 1");
    private IdNamePair consigneeCompany = new IdNamePair(300L, "consignee 1");
    private IdNamePair consigneeLocation = new IdNamePair(400L, "consignee location 1");

    private ShipmentHandlingPartyJsonBuilder sender = ShipmentHandlingPartyJsonBuilder.aShipmentHandlingPartyJson()
            .withCompany(senderCompany).withHandlingCompany(senderCompany).withHandlingLocation(senderLocation)
            .withHandlingLocationCountryCode("AA").withHandlingLocationPostalCode("10000");

    private ShipmentHandlingPartyJsonBuilder consignee = ShipmentHandlingPartyJsonBuilder.aShipmentHandlingPartyJson()
            .withCompany(consigneeCompany).withHandlingCompany(consigneeCompany).withHandlingLocation(consigneeLocation)
            .withHandlingLocationCountryCode("BB").withHandlingLocationPostalCode("20000");

    private OrderShipmentJsonBuilder shipmentRequest =
            OrderShipmentJsonBuilder.anOrderShipmentJson().withCode("0001")
                    .withSender(sender.build()).withConsignee(consignee.build())
                    .withPaymentMethod(IdCodeNameTrio.with(1L, "CAD", "Payment"))
                    .withReadyAtDate(ZonedDateTime.of(2018, 5, 31,9,0,0, 0, ZoneId.of("UTC")))
                    .withIncoterm(IdCodeNameTrio.with(1L, "EXW", "EXW"));


    private OrderJsonBuilder orderRequest = OrderJsonBuilder.anOrderJson()
            .withCustomer(IdNamePair.with(1L, "Customer 1"))
            .withSubsidiary(IdNamePair.with(1L, "Ekol TR"))
            .withServiceType(ServiceType.STANDARD)
            .withTruckLoadType(TruckLoadType.FTL)
            .withShipments(Collections.singletonList(shipmentRequest.build()));

    private CreateOrderRequestJsonBuilder createOrderRequest = CreateOrderRequestJsonBuilder.aCreateOrderRequestJson()
            .withOrder(orderRequest.build())
            .withConfirmed(false)
            .withNumberOfReplications(1);



    @Test
    public void givenValidRequest_whenSave_thenServiceSave() throws Exception{
        given(createOrderService.create(any(CreateOrderRequestJson.class))).willReturn(Collections.singletonList(savedOrder));

        CreateOrderRequestJson request = createOrderRequest.build();
        this.mvc.perform(
                post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(iterableWithSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(savedOrder.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customer.id").value(savedOrder.getCustomer().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].subsidiary.id").value(savedOrder.getSubsidiary().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].serviceType.code").value(savedOrder.getServiceType().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].truckLoadType.code").value(savedOrder.getTruckLoadType().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].status.code").value(savedOrder.getStatus().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[*].id").value(iterableWithSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].code").value("001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].incoterm.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].sender.company.id").value(savedOrder.getShipments().get(0).getSender().getCompany().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].sender.handlingCompany.id").value(savedOrder.getShipments().get(0).getSender().getHandlingCompany().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].sender.handlingLocation.id").value(savedOrder.getShipments().get(0).getSender().getHandlingLocation().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].consignee.company.id").value(savedOrder.getShipments().get(0).getConsignee().getCompany().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].consignee.handlingCompany.id").value(savedOrder.getShipments().get(0).getConsignee().getHandlingCompany().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].shipments.[0].consignee.handlingLocation.id").value(savedOrder.getShipments().get(0).getConsignee().getHandlingLocation().getId()));

    }
}
