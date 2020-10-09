package ekol.orders.order.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import ekol.exceptions.BadRequestException;

/**
 * Created by burak on 26/07/17.
 */
@Service
public class PayWeightCalculator {

    private static final BigDecimal MULTIPLIER_WEIGHT = new BigDecimal("1");
    private static final BigDecimal MULTIPLIER_VOLUME = new BigDecimal("250");
    private static final BigDecimal MULTIPLIER_LDM = new BigDecimal("1750");

    private static final BigDecimal ROUNDING_STEP= new BigDecimal("100");

    public BigDecimal calculatePayWeight(BigDecimal weight, BigDecimal volume, BigDecimal ldm){

        if(weight == null || volume == null || ldm == null) {
            throw new BadRequestException("pay weight calculator received invalid parameters");
        }

        BigDecimal pwWeight = 0 == BigDecimal.ZERO.compareTo(weight) ? BigDecimal.ZERO: weight.multiply(MULTIPLIER_WEIGHT);
        BigDecimal pwVolume = 0 == BigDecimal.ZERO.compareTo(volume) ? BigDecimal.ZERO : volume.multiply(MULTIPLIER_VOLUME);
        BigDecimal pwLDM = 0 == BigDecimal.ZERO.compareTo(volume) ? BigDecimal.ZERO : ldm.multiply(MULTIPLIER_LDM);

        BigDecimal greatestValue = pwWeight;


        if(pwVolume.compareTo(greatestValue) > 0 ){
            greatestValue = pwVolume;
        }

        if(pwLDM.compareTo(greatestValue) > 0 ){
            greatestValue = pwLDM;
        }

        BigDecimal[] divisionResults = greatestValue.divideAndRemainder(ROUNDING_STEP);

        if(divisionResults[1].compareTo(BigDecimal.ZERO) == 0) {
            return greatestValue;
        } else {
            return divisionResults[0].add(BigDecimal.ONE).multiply(ROUNDING_STEP);
        }

    }
}
