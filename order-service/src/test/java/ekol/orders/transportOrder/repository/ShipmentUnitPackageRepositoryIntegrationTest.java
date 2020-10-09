package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.AdrClass;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.repository.AdrClassRepository;
import ekol.orders.lookup.repository.IncotermRepository;
import ekol.orders.lookup.repository.PackageTypeRepository;
import ekol.orders.testdata.SomeData;
import ekol.orders.transportOrder.domain.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration")
public class ShipmentUnitPackageRepositoryIntegrationTest {

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private IncotermRepository incotermRepository;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private AdrClassRepository adrClassRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    @Autowired
    private ShipmentUnitRepository shipmentUnitRepository;

    @Autowired
    private ShipmentUnitPackageRepository shipmentUnitPackageRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                shipmentUnitPackageRepository,
                shipmentUnitRepository,
                packageTypeRepository,
                shipmentRepository,
                adrClassRepository,
                transportOrderRepository,
                incotermRepository,
                transportOrderRequestRepository);
    }

    @Before
    public void runBeforeEveryTest() {
        cleanup();
    }

    @After
    public void runAfterEveryTest() {
        cleanup();
    }

    @Test
    public void saveMustWorkCorrectly() {

        TransportOrderRequest persistedRequest = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());

        Incoterm persistedIncoterm = incotermRepository.save(SomeData.someIncoterm().build());

        TransportOrder persistedTransportOrder = transportOrderRepository.save(SomeData.someTransportOrder(persistedRequest, persistedIncoterm).build());

        AdrClass persistedAdrClass = adrClassRepository.save(SomeData.someAdrClass().build());

        Shipment persistedShipment = shipmentRepository.save(SomeData.someShipment(persistedTransportOrder, persistedAdrClass).build());

        PackageType persistedPackageType = packageTypeRepository.save(SomeData.somePackageType().build());

        ShipmentUnit persistedShipmentUnit = shipmentUnitRepository.save(SomeData.someShipmentUnit(persistedShipment, persistedPackageType).build());

        ShipmentUnitPackage persistedEntity = shipmentUnitPackageRepository.save(SomeData.someShipmentUnitPackage(persistedShipmentUnit).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }
}
