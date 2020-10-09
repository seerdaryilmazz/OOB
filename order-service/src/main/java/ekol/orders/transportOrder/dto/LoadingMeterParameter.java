package ekol.orders.transportOrder.dto;

import ekol.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by fatmaozyildirim on 10/14/16.
 */
public enum LoadingMeterParameter {
    TENTOTWENTY(new BigDecimal(0.9),new BigDecimal(20),new BigDecimal(20)),
    TWENTYTOTHIRTY(new BigDecimal(20.1),new BigDecimal(30),new BigDecimal(30)),
    THIRTYTOFORTY(new BigDecimal(30.1),new BigDecimal(40),new BigDecimal(40)),
    FORTYTOFIFTHY(new BigDecimal(40.1),new BigDecimal(50),new BigDecimal(50)),
    FIFTHYTOSIXTY(new BigDecimal(50.1),new BigDecimal(60),new BigDecimal(60)),
    SIXTYTOSEVENTY(new BigDecimal(60.1),new BigDecimal(62),new BigDecimal(70)),
    SEVENTYTOEIGHTY(new BigDecimal(62.1),new BigDecimal(83),new BigDecimal(80)),
    EIGHTYTOTAHUNDREDANDTWENTY(new BigDecimal(83.1),new BigDecimal(122),new BigDecimal(120)),
    HUNDREDTWENTYTOHUNDREDSIXTY(new BigDecimal(122.1),new BigDecimal(160),new BigDecimal(160)),
    HUNDREDSIXTYTO240(new BigDecimal(160.1),new BigDecimal(240),new BigDecimal(240)),
    THE240TOTHOUSAND(new BigDecimal(240.1),new BigDecimal(999),new BigDecimal(1000)),
    THOUSANDTO1500(new BigDecimal(999.1),new BigDecimal(9999),new BigDecimal(1500));


    private BigDecimal from;
    private BigDecimal to;
    private BigDecimal scale;

    public BigDecimal getFrom() {
        return from;
    }

    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    LoadingMeterParameter(BigDecimal from, BigDecimal to, BigDecimal scale) {
        this.from = from;
        this.to = to;
        this.scale = scale;
    }

    public static BigDecimal findScale(BigDecimal parameter){
        LoadingMeterParameter result=Arrays.stream(LoadingMeterParameter.values()).filter(p->p.from.compareTo(parameter)<0
                &&p.to.compareTo(parameter)>=0).findAny().orElseThrow(()-> new ResourceNotFoundException(
                "No Match found!"));
        return result.getScale();

    }
}
