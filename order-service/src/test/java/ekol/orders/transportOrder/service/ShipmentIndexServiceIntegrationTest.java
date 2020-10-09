package ekol.orders.transportOrder.service;

import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;
import ekol.model.User;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.domain.ServiceType;
import ekol.orders.lookup.domain.TruckLoadType;
import ekol.orders.order.domain.Status;
import ekol.orders.order.domain.dto.response.location.WarehouseResponse;
import ekol.orders.order.service.LocationServiceClient;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.domain.*;
import ekol.orders.transportOrder.elastic.shipment.config.ShipmentSearchConfig;
import ekol.orders.transportOrder.elastic.shipment.document.ShipmentDocument;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentIndexService;
import ekol.orders.transportOrder.elastic.shipment.service.ShipmentSearchService;
import ekol.orders.transportOrder.repository.TransportOrderRepository;
import ekol.resource.oauth2.SessionOwner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class ShipmentIndexServiceIntegrationTest{

    private static final long SHIPMENT_ID = 1;
    private static final String SHIPMENT_CODE = "01";
    private static final Company COMPANY = new Company();
    private static final long TRANSPORT_ORDER_ID = 2;
    private static final Location LOCATION = new Location();
    private static final WarehouseResponse COLL_WAREHOUSE_RESPONSE = new WarehouseResponse();
    private static final IdNamePair COLL_WAREHOUSE = new IdNamePair();
    private static final IdNamePair COLL_WAREHOUSE_LOC = new IdNamePair();
    private static final WarehouseResponse DIST_WAREHOUSE_RESPONSE = new WarehouseResponse();
    private static final IdNamePair DIST_WAREHOUSE = new IdNamePair();
    private static final IdNamePair DIST_WAREHOUSE_LOC = new IdNamePair();
    private static final SenderOrReceiver SENDER_OR_RECEIVER = new SenderOrReceiver();
    private static final Status STATUS = Status.CLOSED;
    private static final LocalDateTime DATE_TIME = LocalDateTime.now().withSecond(0).withNano(0);
    private static final String DATE_TIME_TIMEZONE = "Europe/Istanbul";
    private static final BigDecimal GROSS_WEIGHT = BigDecimal.TEN;
    private static final BigDecimal NET_WEIGHT = BigDecimal.ONE;
    private static final BigDecimal VOLUME = BigDecimal.ONE;
    private static final BigDecimal LDM = BigDecimal.ONE;
    private static final BigDecimal PW = BigDecimal.ONE;

    private static final int COUNT = 1;
    private static final PackageType PACKAGE_TYPE = new PackageType();

    static {
        COMPANY.setId(1l);
        COMPANY.setName("CUSTOMER");

        LOCATION.setId(3l);
        LOCATION.setName("LOCATION_NAME");
        LOCATION.setTimezone("Europe/Istanbul");

        COLL_WAREHOUSE.setName("Coll Warehouse");
        COLL_WAREHOUSE.setId(1L);
        COLL_WAREHOUSE_LOC.setName("Coll Warehouse");
        COLL_WAREHOUSE_LOC.setId(1L);
        COLL_WAREHOUSE_RESPONSE.setId(1L);
        COLL_WAREHOUSE_RESPONSE.setName("Coll Warehouse");
        COLL_WAREHOUSE_RESPONSE.setCompanyLocation(COLL_WAREHOUSE_LOC);

        DIST_WAREHOUSE.setName("Dist Warehouse");
        DIST_WAREHOUSE.setId(2L);
        DIST_WAREHOUSE_LOC.setName("Dist Warehouse");
        DIST_WAREHOUSE_LOC.setId(2L);
        DIST_WAREHOUSE_RESPONSE.setId(2L);
        DIST_WAREHOUSE_RESPONSE.setName("Dist Warehouse");
        DIST_WAREHOUSE_RESPONSE.setCompanyLocation(DIST_WAREHOUSE_LOC);

        Country country = new Country();
        country.setIso("COUNTRY_ISO");

        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setPostalCode("POSTAL_CODE");
        postalAddress.setCountry(country);

        LOCATION.setPostaladdress(postalAddress);

        SENDER_OR_RECEIVER.setCompanyId(COMPANY.getId());
        SENDER_OR_RECEIVER.setLocationOwnerCompanyId(COMPANY.getId());
        SENDER_OR_RECEIVER.setLocationId(LOCATION.getId());

        PACKAGE_TYPE.setId(4l);
        PACKAGE_TYPE.setName("PACKAGE_TYPE");
    }

    @Mock
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    @InjectMocks
    private ShipmentIndexService shipmentIndexService;

    @Autowired
    private ShipmentSearchService shipmentSearchService;


    @MockBean
    private SessionOwner sessionOwner;

    @MockBean
    private KartoteksClient kartoteksClient;

    @MockBean
    private LocationServiceClient locationServiceClient;

    private TransportOrder getTestTransportOrder() {
        ShipmentUnitPackage shipmentUnitPackage = new ShipmentUnitPackage();
        shipmentUnitPackage.setCount(COUNT);
        shipmentUnitPackage.setWidthInCentimeters(BigDecimal.ONE);
        shipmentUnitPackage.setHeightInCentimeters(BigDecimal.ONE);
        shipmentUnitPackage.setLengthInCentimeters(BigDecimal.ONE);
        shipmentUnitPackage.setStackSize(1);

        Set<ShipmentUnitPackage> shipmentUnitPackages = new HashSet<>();
        shipmentUnitPackages.add(shipmentUnitPackage);

        ShipmentUnit shipmentUnit = new ShipmentUnit();
        shipmentUnit.setType(PACKAGE_TYPE);
        shipmentUnit.setTotalGrossWeightInKilograms(GROSS_WEIGHT);
        shipmentUnit.setTotalNetWeightInKilograms(NET_WEIGHT);
        shipmentUnit.setTotalVolumeInCubicMeters(VOLUME);
        shipmentUnit.setTotalLdm(LDM);
        shipmentUnit.setShipmentUnitPackages(shipmentUnitPackages);

        Set<ShipmentUnit> shipmentUnits = new HashSet<>();
        shipmentUnits.add(shipmentUnit);

        Shipment shipment = new Shipment();
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setId(1l);

        Set<Shipment> shipments = new HashSet<>();
        shipments.add(shipment);

        transportOrder.setShipments(shipments);
        transportOrder.setCustomerId(COMPANY.getId());
        transportOrder.setId(TRANSPORT_ORDER_ID);
        transportOrder.setStatus(STATUS);
        transportOrder.setSubsidiary(IdNamePair.createWith(1L, "Ekol"));
        transportOrder.setTruckLoadType(TruckLoadType.FTL);
        transportOrder.setServiceType(ServiceType.STANDARD);

        shipment.setTransportOrder(transportOrder);
        shipment.setId(SHIPMENT_ID);
        shipment.setCode(SHIPMENT_CODE);
        shipment.setSender(SENDER_OR_RECEIVER);
        shipment.setReceiver(SENDER_OR_RECEIVER);
        shipment.setStatus(Status.CREATED);
        shipment.setReadyAtDate(new FixedZoneDateTime(DATE_TIME, DATE_TIME_TIMEZONE));
        shipment.setRequestedDeliveryDate(new FixedZoneDateTime(DATE_TIME, DATE_TIME_TIMEZONE));
        shipment.setCollectionArrivalDate(new FixedZoneDateTime(DATE_TIME, DATE_TIME_TIMEZONE));
        shipment.setLinehaulArrivalDate(new FixedZoneDateTime(DATE_TIME, DATE_TIME_TIMEZONE));
        shipment.setPickupAppointment(new FixedZoneDateTimeWindow(new FixedZoneDateTime(DATE_TIME, "UTC"), new FixedZoneDateTime(DATE_TIME, "UTC")));
        shipment.setDeliveryAppointment(new FixedZoneDateTimeWindow(new FixedZoneDateTime(DATE_TIME, "UTC"), new FixedZoneDateTime(DATE_TIME, "UTC")));
        shipment.setShipmentUnits(shipmentUnits);
        shipment.setPayWeight(PW);
        shipment.setCollectionWarehouse(COLL_WAREHOUSE);
        shipment.setDistributionWarehouse(DIST_WAREHOUSE);

        return transportOrder;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(transportOrderRepository.findWithDetailsById(getTestTransportOrder().getId())).thenReturn(getTestTransportOrder());


        User user = new User();
        user.setTimeZoneId("Europe/Istanbul");
        given(this.sessionOwner.getCurrentUser()).willReturn(user);
        given(kartoteksClient.findCompanyById(COMPANY.getId())).willReturn(COMPANY);
        given(kartoteksClient.findLocationById(LOCATION.getId())).willReturn(LOCATION);

        given(locationServiceClient.findWarehouseById(COLL_WAREHOUSE.getId())).willReturn(COLL_WAREHOUSE_RESPONSE);
        given(locationServiceClient.findWarehouseById(DIST_WAREHOUSE.getId())).willReturn(DIST_WAREHOUSE_RESPONSE);
    }

    @Test
    public void shouldIndexTransportOrder() {
        shipmentIndexService.indexTransportOrder(getTestTransportOrder().getId());

        try {
            // Elastic needs some more time for indexing
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ShipmentDocument shipmentDocument = shipmentSearchService
                .searchShipments(new ShipmentSearchConfig())
                .getShipments()
                .get(0)
                .get(0);

        assertEquals(shipmentDocument.getId(), String.valueOf(SHIPMENT_ID));
        assertEquals(shipmentDocument.getCode(), String.valueOf(SHIPMENT_CODE));
        assertEquals(shipmentDocument.getCustomerName(), COMPANY.getName());
        assertEquals(shipmentDocument.getTransportOrderId().longValue(), TRANSPORT_ORDER_ID);
        assertEquals(shipmentDocument.getSender().getCompanyName(), COMPANY.getName());
        assertEquals(shipmentDocument.getSender().getLocationOwnerCompanyName(), COMPANY.getName());
        assertEquals(shipmentDocument.getSender().getLocation().getCountryIsoCode(), LOCATION.getPostaladdress().getCountry().getIso());
        assertEquals(shipmentDocument.getSender().getLocation().getName(), LOCATION.getName());
        assertEquals(shipmentDocument.getSender().getLocation().getPostalCode(), LOCATION.getPostaladdress().getPostalCode());
        assertEquals(shipmentDocument.getReceiver().getCompanyName(), COMPANY.getName());
        assertEquals(shipmentDocument.getReceiver().getLocationOwnerCompanyName(), COMPANY.getName());
        assertEquals(shipmentDocument.getReceiver().getLocation().getCountryIsoCode(), LOCATION.getPostaladdress().getCountry().getIso());
        assertEquals(shipmentDocument.getReceiver().getLocation().getName(), LOCATION.getName());
        assertEquals(shipmentDocument.getReceiver().getLocation().getPostalCode(), LOCATION.getPostaladdress().getPostalCode());
        assertEquals(shipmentDocument.getReadyAtDate().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getRequestedDeliveryDate().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getCollectionArrivalDate().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getLinehaulArrivalDate().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getPickupAppointment().getStart().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getPickupAppointment().getEnd().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getDeliveryAppointment().getStart().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getDeliveryAppointment().getEnd().getLocalDateTime(), DATE_TIME.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        assertEquals(shipmentDocument.getGrossWeight(), new Double(GROSS_WEIGHT.doubleValue()));
        assertEquals(shipmentDocument.getVolume(), new Double(VOLUME.doubleValue()));
        assertEquals(shipmentDocument.getLdm(), new Double(LDM.doubleValue()));
        assertEquals(shipmentDocument.getPackageTypes().get(0).getName(), PACKAGE_TYPE.getName());
        assertEquals(shipmentDocument.getPackageTypes().get(0).getCount(), COUNT);
    }
}
