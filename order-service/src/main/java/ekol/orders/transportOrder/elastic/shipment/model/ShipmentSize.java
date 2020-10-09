package ekol.orders.transportOrder.elastic.shipment.model;

import java.math.BigDecimal;

/**
 * Created by ozer on 14/10/16.
 */
public class ShipmentSize {

    private BigDecimal grossWeight = BigDecimal.ZERO;
    private BigDecimal volume = BigDecimal.ZERO;
    private BigDecimal ldm = BigDecimal.ZERO;
    private BigDecimal payWeight = BigDecimal.ZERO;
    private Integer count = 0;

    public void addCount(Integer value){
        this.count += value;
    }

    public void addGrossWeight(BigDecimal value){
        this.grossWeight = this.grossWeight.add(value);
    }

    public void addVolume(BigDecimal value){
        this.volume = this.volume.add(value);
    }

    public void addLdm(BigDecimal value){
        this.ldm = this.ldm.add(value);
    }

    public void addPayWeight(BigDecimal value){
        this.payWeight = this.payWeight.add(value);
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getLdm() {
        return ldm;
    }

    public void setLdm(BigDecimal ldm) {
        this.ldm = ldm;
    }

    public BigDecimal getPayWeight() {
        return payWeight;
    }

    public void setPayWeight(BigDecimal payWeight) {
        this.payWeight = payWeight;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
