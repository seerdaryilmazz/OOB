package ekol.orders.order.service;

import ekol.exceptions.BadRequestException;
import ekol.orders.transportOrder.domain.ShipmentUnitPackage;
import ekol.orders.transportOrder.dto.LoadingMeterParameter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoadingMeterCalculator {


    private static BigDecimal LOADING_METER_AREA = new BigDecimal(24000);

    public BigDecimal calculateLoadingMeter(ShipmentUnitPackage shipmentUnitPackage){

        if(shipmentUnitPackage == null || shipmentUnitPackage.getWidthInCentimeters() == null || shipmentUnitPackage.getLengthInCentimeters() == null
                || shipmentUnitPackage.getStackSize() == null || shipmentUnitPackage.getStackSize() == 0) {
            throw new BadRequestException("loading meter calculator received invalid parameters");
        }

        BigDecimal ldm ;
        BigDecimal width = shipmentUnitPackage.getWidthInCentimeters();
        BigDecimal length = shipmentUnitPackage.getLengthInCentimeters();
        BigDecimal scaleWidth= LoadingMeterParameter.findScale(width);
        BigDecimal scaleLength = LoadingMeterParameter.findScale(length);
        BigDecimal stackSize = new BigDecimal(shipmentUnitPackage.getStackSize()!=null ? shipmentUnitPackage.getStackSize():1);

        if(stackSize.compareTo(new BigDecimal("0")) == 0) throw new BadRequestException("Stack Size can not be 0");

        //calculate according to width of the shipmentunitpackage
        BigDecimal ldmAccordingtoWidth=scaleWidth.multiply(length).divide(LOADING_METER_AREA,2, RoundingMode.HALF_UP);

        //calculate according to length of the shipmentunitpackage
        BigDecimal ldmAccordingtpLength = scaleLength.multiply(width).divide(LOADING_METER_AREA,2,BigDecimal.ROUND_HALF_UP);
        if(ldmAccordingtoWidth.compareTo(ldmAccordingtpLength)<=0)
            ldm= ldmAccordingtoWidth;
        else ldm =ldmAccordingtpLength;

        ldm = ldm.multiply(new BigDecimal(shipmentUnitPackage.getCount())).divide( stackSize,2,BigDecimal.ROUND_HALF_UP);
        return ldm;

    }

    public BigDecimal calculateLoadingMeter(BigDecimal length,BigDecimal width,Integer stackSize,Integer count){

        if(length == null || width == null || stackSize == null || stackSize == 0 || count == null) {
            throw new BadRequestException("loading meter calculator received invalid parameters");
        }

        BigDecimal ldm;
        BigDecimal scaleWidth= LoadingMeterParameter.findScale(width);
        BigDecimal scaleLength = LoadingMeterParameter.findScale(length);
        BigDecimal stackSizeBg = new BigDecimal((stackSize != null) ? stackSize : 1);

        if(stackSizeBg.compareTo(new BigDecimal("0")) == 0) throw new BadRequestException("Stack Size can not be 0");


        //calculate according to width of the shipmentunitpackage
        BigDecimal ldmAccordingtoWidth=scaleWidth.multiply(length).divide(LOADING_METER_AREA,5, BigDecimal.ROUND_HALF_UP);

        //calculate according to length of the shipmentunitpackage
        BigDecimal ldmAccordingtpLength = scaleLength.multiply(width).divide(LOADING_METER_AREA,5,BigDecimal.ROUND_HALF_UP);
        if(ldmAccordingtoWidth.compareTo(ldmAccordingtpLength)<=0)
            ldm= ldmAccordingtoWidth;
        else ldm =ldmAccordingtpLength;

        ldm = ldm.multiply(new BigDecimal(count)).divide( stackSizeBg,2,BigDecimal.ROUND_HALF_UP);
        return ldm;

    }

    public BigDecimal calculateVolume(BigDecimal length, BigDecimal width, BigDecimal height, Integer count){
        return length.multiply(width).multiply(height).multiply(new BigDecimal(count))
                .divide(BigDecimal.TEN.pow(6), 2, RoundingMode.HALF_EVEN);
    }

}


