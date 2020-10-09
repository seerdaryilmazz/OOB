package ekol.orders;

import ekol.exceptions.ResourceNotFoundException;
import ekol.orders.order.service.LoadingMeterCalculator;
import ekol.orders.transportOrder.domain.ShipmentUnitPackage;
import ekol.orders.transportOrder.dto.LoadingMeterParameter;
import ekol.orders.transportOrder.repository.ShipmentUnitPackageRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by fatmaozyildirim on 10/12/16.
 */

@RunWith(SpringRunner.class)
public class LoadingMeterCalculatorServiceTest {

    @InjectMocks
    private LoadingMeterCalculator loadingMeterCalculatorService;

    @Mock
    private ShipmentUnitPackageRepository shipmentRepository;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private void expectException(String message){
        thrown.expect(ResourceNotFoundException.class);
        thrown.expectMessage(message);
    }

    @Test
    public void shouldReturnOneLdmIfShipmentUnitPackageIsOneOne(){

        ShipmentUnitPackage shipmentUnitPackage = new ShipmentUnitPackage();
        shipmentUnitPackage.setId(1L);
        shipmentUnitPackage.setCount(1);
        shipmentUnitPackage.setHeightInCentimeters(new BigDecimal(100));
        shipmentUnitPackage.setLengthInCentimeters(new BigDecimal(100));
        shipmentUnitPackage.setWidthInCentimeters(new BigDecimal(100));
        shipmentUnitPackage.setStackSize(1);
        when(shipmentRepository.findOne(1L)).thenReturn(shipmentUnitPackage);
        BigDecimal ldm = loadingMeterCalculatorService.calculateLoadingMeter(shipmentUnitPackage);
        assertEquals(new BigDecimal("0.50"),ldm);
    }

    @Test
    public void shouldReturnException(){
        try{
            BigDecimal width = new BigDecimal(99999);
            LoadingMeterParameter.findScale(width);
            fail("expected: BadRequestException msg: 'No Match found!'");
        } catch (ResourceNotFoundException e) {
            assertThat(e.getMessage(), equalTo("No Match found!"));
        }
    }

    @Test
    public void shouldThrowExceptionWhenParameterCouldntMatch(){
        expectException("No Match found!");
        ShipmentUnitPackage shipmentUnitPackage = new ShipmentUnitPackage();
        shipmentUnitPackage.setId(1L);
        shipmentUnitPackage.setCount(1);
        shipmentUnitPackage.setHeightInCentimeters(new BigDecimal(99999));
        shipmentUnitPackage.setLengthInCentimeters(new BigDecimal(99999));
        shipmentUnitPackage.setWidthInCentimeters(new BigDecimal(100));
        shipmentUnitPackage.setStackSize(1);
        when(shipmentRepository.findOne(1L)).thenReturn(shipmentUnitPackage);
        loadingMeterCalculatorService.calculateLoadingMeter(shipmentUnitPackage);
    }

}

