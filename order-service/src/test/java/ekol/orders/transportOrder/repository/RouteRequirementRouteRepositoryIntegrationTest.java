package ekol.orders.transportOrder.repository;

import ekol.hibernate5.test.TestUtils;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.repository.IncotermRepository;
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
public class RouteRequirementRouteRepositoryIntegrationTest {

    @Autowired
    private TransportOrderRequestRepository transportOrderRequestRepository;

    @Autowired
    private IncotermRepository incotermRepository;

    @Autowired
    private TransportOrderRepository transportOrderRepository;

    @Autowired
    private TransportTypeRepository transportTypeRepository;

    @Autowired
    private RouteRequirementRepository routeRequirementRepository;

    @Autowired
    private RouteRequirementRouteRepository routeRequirementRouteRepository;

    public void cleanup() {
        // Daha önceden kayıt eklenmişse hepsini sil.
        TestUtils.softDeleteAll(
                routeRequirementRouteRepository,
                routeRequirementRepository,
                transportTypeRepository,
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

        TransportType persistedTransportType = transportTypeRepository.save(SomeData.someTransportType().build());

        RouteRequirement persistedRouteRequirement = routeRequirementRepository.save(SomeData.someRouteRequirement(persistedTransportOrder, persistedTransportType).build());

        RouteRequirementRoute persistedEntity = routeRequirementRouteRepository.save(SomeData.someRouteRequirementRoute(persistedRouteRequirement).build());

        Assert.assertNotNull("'save' does not work correctly.", persistedEntity);
        Assert.assertNotNull("'save' does not work correctly.", persistedEntity.getId());
    }
}
