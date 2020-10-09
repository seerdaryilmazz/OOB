package ekol.orders.order.service;

import ekol.exceptions.ApplicationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class CodeGeneratorUnitTest {

    @MockBean
    private RedisAtomicIncrementer atomicIncrementer;

    private CodeGenerator codeGenerator;

    @Before
    public void init(){
        this.codeGenerator = new CodeGenerator(atomicIncrementer);
    }

    @Test
    public void givenIncrementerReturnsValuesWithDifferentDigits_whenGenerateOrderCode_thenCodeIsCorrect() {

        given(atomicIncrementer.getNewOrderCode()).willReturn(4L);
        assertEquals("0000004", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(54L);
        assertEquals("0000054", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(854L);
        assertEquals("0000854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(1854L);
        assertEquals("0001854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(21854L);
        assertEquals("0021854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(621854L);
        assertEquals("0621854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(9621854L);
        assertEquals("9621854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(10621854L);
        assertEquals("A621854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(20621854L);
        assertEquals("L621854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(30621854L);
        assertEquals("Y621854", this.codeGenerator.getNewOrderCode());

        given(atomicIncrementer.getNewOrderCode()).willReturn(31999999L);
        assertEquals("Z999999", this.codeGenerator.getNewOrderCode());
    }

    @Test(expected = ApplicationException.class)
    public void givenIncrementerReturnsValueOutOfLimits_whenGenerateOrderCode_thenThrowException() {

        given(atomicIncrementer.getNewOrderCode()).willReturn(32000000L);
        this.codeGenerator.getNewOrderCode();
    }


    @Test
    public void givenIncrementerReturnsValuesWithDifferentDigits_whenGenerateShipmentCode_thenCodeIsCorrect() {

        given(atomicIncrementer.getNewShipmentCode()).willReturn(4L);
        assertEquals("AA00004", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(54L);
        assertEquals("AA00054", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(854L);
        assertEquals("AA00854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(1854L);
        assertEquals("AA01854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(21854L);
        assertEquals("AA21854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(621854L);
        assertEquals("AG21854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(1621854L);
        assertEquals("AT21854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(2121854L);
        assertEquals("AZ21854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(2221854L);
        assertEquals("BA21854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(4621854L);
        assertEquals("CC21854", this.codeGenerator.getNewShipmentCode());

        given(atomicIncrementer.getNewShipmentCode()).willReturn(48399999L);
        assertEquals("ZZ99999", this.codeGenerator.getNewShipmentCode());
    }

    @Test(expected = ApplicationException.class)
    public void givenIncrementerReturnsValueOutOfLimits_whenGenerateShipmentCode_thenThrowException() {

        given(atomicIncrementer.getNewShipmentCode()).willReturn(48400000L);
        this.codeGenerator.getNewShipmentCode();
    }

}
