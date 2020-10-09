package ekol.orders.order.service;

import ekol.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CodeGenerator {

    private RedisAtomicIncrementer atomicIncrementer;

    private final char[] alphaNumericValues =
            new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','J','K','L','M','N','P','R','S','T','U','V','X','Y','Z'};

    private final char[] alphabeticValues =
            new char[]{'A','B','C','D','E','F','G','H','J','K','L','M','N','P','R','S','T','U','V','X','Y','Z'};

    @Autowired
    public CodeGenerator(RedisAtomicIncrementer atomicIncrementer){
        this.atomicIncrementer = atomicIncrementer;
    }


    public String getNewOrderCode() {
        final int orderCodeNumericLength = 6;
        //format is A123456
        long orderCode = atomicIncrementer.getNewOrderCode(); // plain number from auto incrementer like 2.017.263

        long divider = new BigDecimal("1E+0" + orderCodeNumericLength).longValue();

        //mod with 10E6 to find numeric part of the code which is 17.263, and format it with padding "0" so you get "017263"
        String numericPart = String.format("%0" + orderCodeNumericLength +"d", orderCode % divider);

        //divide with 10E6 to find first character of the code which is now 2
        int alphaNumericIndex = (int) (orderCode / divider);
        if(alphaNumericIndex >= alphaNumericValues.length){
            throw new ApplicationException("We are out of order numbers");
        }
        //take the char related to the index, for index 2 first char is '2', if index was 11 the char would be 'B'
        char alphanumericPart = alphaNumericValues[alphaNumericIndex];
        return alphanumericPart + numericPart;
    }

    public String getNewShipmentCode() {
        final int shipmentCodeNumericLength = 5;
        long shipmentCode = atomicIncrementer.getNewShipmentCode(); // plain number from auto incrementer like 3.000.345

        long divider = new BigDecimal("1E+0" + shipmentCodeNumericLength).longValue();

        //mod with 10E5 to find numeric part of the code which is 345, and format it with padding "0" so you get "00345"
        String numericPart = String.format("%0" + shipmentCodeNumericLength +"d", shipmentCode % divider);

        //divide with 10E5 to find the value to be represented in alphabetic, which is 30
        int alphabeticValue = (int) (shipmentCode / divider);

        if(alphabeticValue >= (int)Math.pow(alphabeticValues.length, 2)){
            throw new ApplicationException("We are out of shipment numbers");
        }

        //second char is 30 % 22 = 8 => 'J'
        String secondChar = String.valueOf(alphabeticValues[alphabeticValue % alphabeticValues.length]);
        //second char is 30 / 22 = 1 => 'B'
        String firstChar = String.valueOf(alphabeticValues[alphabeticValue / alphabeticValues.length]);

        return firstChar + secondChar + numericPart;
    }
}
