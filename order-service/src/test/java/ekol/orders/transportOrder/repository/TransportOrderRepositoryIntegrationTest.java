package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.AdrClass;
import ekol.orders.lookup.domain.DocumentType;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.domain.PackageType;
import ekol.orders.lookup.repository.AdrClassRepository;
import ekol.orders.lookup.repository.DocumentTypeRepository;
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
public class TransportOrderRepositoryIntegrationTest {

    @Autowired
    private EquipmentRequirementRepository equipmentRequirementRepository;

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @Autowired
    private VehicleRequirementRepository vehicleRequirementRepository;

    @Autowired
    private RouteRequirementRouteRepository routeRequirementRouteRepository;

    @Autowired
    private RouteRequirementRepository routeRequirementRepository;

    @Autowired
    private TransportTypeRepository transportTypeRepository;

    @Autowired
    private ShipmentUnitPackageRepository shipmentUnitPackageRepository;

    @Autowired
    private PackageTypeRepository packageTypeRepository;

    @Autowired
    private ShipmentUnitRepository shipmentUnitRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private AdrClassRepository adrClassRepository;

    @Autowired
    private TransportOrderDocumentRepository transportOrderDocumentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private IncotermRepository incotermRepository;

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                equipmentRequirementRepository,
                equipmentTypeRepository,
                vehicleRequirementRepository,
                routeRequirementRouteRepository,
                routeRequirementRepository,
                transportTypeRepository,
                shipmentUnitPackageRepository,
                packageTypeRepository,
                shipmentUnitRepository,
                shipmentRepository,
                adrClassRepository,
                transportOrderDocumentRepository,
                documentTypeRepository,
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

        TransportOrder persistedEntity = transportOrderRepository.save(SomeData.someTransportOrder(persistedRequest, persistedIncoterm).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }

    @Test
    public void findWithDetailsByIdMustWorkCorrectly() {

        TransportOrderRequest persistedRequest = transportOrderRequestRepository.save(SomeData.someTransportOrderRequest(null).build());

        Incoterm persistedIncoterm = incotermRepository.save(SomeData.someIncoterm().build());

        TransportOrder persistedEntity = transportOrderRepository.save(SomeData.someTransportOrder(persistedRequest, persistedIncoterm).build());

        // documents
        DocumentType persistedDocumentType = documentTypeRepository.save(SomeData.someDocumentType().build());

        TransportOrderDocument persistedTransportOrderDocument = transportOrderDocumentRepository.save(
                SomeData.someTransportOrderDocument(persistedEntity, persistedDocumentType).build());

        // shipments
        AdrClass persistedAdrClass = adrClassRepository.save(SomeData.someAdrClass().build());

        Shipment persistedShipment = shipmentRepository.save(SomeData.someShipment(persistedEntity, persistedAdrClass).build());

        // shipmentUnits
        PackageType persistedPackageType = packageTypeRepository.save(SomeData.somePackageType().build());

        ShipmentUnit persistedShipmentUnit = shipmentUnitRepository.save(SomeData.someShipmentUnit(persistedShipment, persistedPackageType).build());

        // shipmentUnitPackages
        ShipmentUnitPackage persistedShipmentUnitPackage = shipmentUnitPackageRepository.save(
                SomeData.someShipmentUnitPackage(persistedShipmentUnit).build());

        // routeRequirements
        TransportType persistedTransportType = transportTypeRepository.save(SomeData.someTransportType().build());

        RouteRequirement persistedRouteRequirement = routeRequirementRepository.save(SomeData.someRouteRequirement(persistedEntity, persistedTransportType).build());

        // routeRequirementRoutes
        RouteRequirementRoute persistedRouteRequirementRoute = routeRequirementRouteRepository.save(
                SomeData.someRouteRequirementRoute(persistedRouteRequirement).build());

        // vehicleRequirements
        VehicleRequirement persistedVehicleRequirement = vehicleRequirementRepository.save(SomeData.someVehicleRequirement(persistedEntity).build());

        // equipmentRequirements
        EquipmentType persistedEquipmentType = equipmentTypeRepository.save(SomeData.someEquipmentType().build());

        EquipmentRequirement persistedEquipmentRequirement = equipmentRequirementRepository.save(
                SomeData.someEquipmentRequirement(persistedEntity, persistedEquipmentType).build());

        TransportOrder queriedEntity = transportOrderRepository.findWithDetailsById(persistedEntity.getId());

        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity);
        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getId());
        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getRequest());
        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getIncoterm());

        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getDocuments());
        Assert.assertTrue("'find with details' does not work correctly.", queriedEntity.getDocuments().size() > 0);

        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getShipments());
        Assert.assertTrue("'find with details' does not work correctly.", queriedEntity.getShipments().size() > 0);

        Shipment shipment = queriedEntity.getShipments().iterator().next();

        Assert.assertNotNull("'find with details' does not work correctly.", shipment.getAdrClass());
        Assert.assertNotNull("'find with details' does not work correctly.", shipment.getShipmentUnits());
        Assert.assertTrue("'find with details' does not work correctly.", shipment.getShipmentUnits().size() > 0);

        ShipmentUnit shipmentUnit = shipment.getShipmentUnits().iterator().next();

        Assert.assertNotNull("'find with details' does not work correctly.", shipmentUnit.getType());
        Assert.assertNotNull("'find with details' does not work correctly.", shipmentUnit.getShipmentUnitPackages());
        Assert.assertTrue("'find with details' does not work correctly.", shipmentUnit.getShipmentUnitPackages().size() > 0);

        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getRouteRequirements());
        Assert.assertTrue("'find with details' does not work correctly.", queriedEntity.getRouteRequirements().size() > 0);

        RouteRequirement routeRequirement = queriedEntity.getRouteRequirements().iterator().next();

        Assert.assertNotNull("'find with details' does not work correctly.", routeRequirement.getTransportType());
        Assert.assertNotNull("'find with details' does not work correctly.", routeRequirement.getRoutes());
        Assert.assertTrue("'find with details' does not work correctly.", routeRequirement.getRoutes().size() > 0);

        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getVehicleRequirements());
        Assert.assertTrue("'find with details' does not work correctly.", queriedEntity.getVehicleRequirements().size() > 0);

        VehicleRequirement vehicleRequirement = queriedEntity.getVehicleRequirements().iterator().next();


        Assert.assertNotNull("'find with details' does not work correctly.", queriedEntity.getEquipmentRequirements());
        Assert.assertTrue("'find with details' does not work correctly.", queriedEntity.getEquipmentRequirements().size() > 0);

        EquipmentRequirement equipmentRequirement = queriedEntity.getEquipmentRequirements().iterator().next();

        Assert.assertNotNull("'find with details' does not work correctly.", equipmentRequirement.getEquipmentType());
    }
}
