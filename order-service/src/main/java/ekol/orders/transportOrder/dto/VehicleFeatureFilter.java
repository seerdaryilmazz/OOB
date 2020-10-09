package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
    public class VehicleFeatureFilter {

    private boolean curtainSider;
    private boolean box;
    private boolean mega;
    private boolean doubleDeck;
    private boolean frigoTrailer;
    private boolean isolated;
    private boolean liftingRoof;
    private boolean securitySensor;
    private boolean slidingRoof;
    private boolean suitableForHangingLoads;
    private boolean suitableForTrain;
    private boolean tailLift;
    private boolean xlCertificate;

    public boolean isCurtainSider() {
        return curtainSider;
    }

    public void setCurtainSider(boolean curtainSider) {
        this.curtainSider = curtainSider;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public boolean isMega() {
        return mega;
    }

    public void setMega(boolean mega) {
        this.mega = mega;
    }

    public boolean isDoubleDeck() {
        return doubleDeck;
    }

    public void setDoubleDeck(boolean doubleDeck) {
        this.doubleDeck = doubleDeck;
    }

    public boolean isFrigoTrailer() {
        return frigoTrailer;
    }

    public void setFrigoTrailer(boolean frigoTrailer) {
        this.frigoTrailer = frigoTrailer;
    }

    public boolean isIsolated() {
        return isolated;
    }

    public void setIsolated(boolean isolated) {
        this.isolated = isolated;
    }

    public boolean isLiftingRoof() {
        return liftingRoof;
    }

    public void setLiftingRoof(boolean liftingRoof) {
        this.liftingRoof = liftingRoof;
    }

    public boolean isSecuritySensor() {
        return securitySensor;
    }

    public void setSecuritySensor(boolean securitySensor) {
        this.securitySensor = securitySensor;
    }

    public boolean isSlidingRoof() {
        return slidingRoof;
    }

    public void setSlidingRoof(boolean slidingRoof) {
        this.slidingRoof = slidingRoof;
    }

    public boolean isSuitableForHangingLoads() {
        return suitableForHangingLoads;
    }

    public void setSuitableForHangingLoads(boolean suitableForHangingLoads) {
        this.suitableForHangingLoads = suitableForHangingLoads;
    }

    public boolean isSuitableForTrain() {
        return suitableForTrain;
    }

    public void setSuitableForTrain(boolean suitableForTrain) {
        this.suitableForTrain = suitableForTrain;
    }

    public boolean isTailLift() {
        return tailLift;
    }

    public void setTailLift(boolean tailLift) {
        this.tailLift = tailLift;
    }

    public boolean isXlCertificate() {
        return xlCertificate;
    }

    public void setXlCertificate(boolean xlCertificate) {
        this.xlCertificate = xlCertificate;
    }

    public void merge(VehicleFeatureFilter filter) {
        if (filter != null) {
            this.setCurtainSider(this.isCurtainSider() || filter.isCurtainSider());
            this.setBox(this.isBox() || filter.isBox());

            this.setMega(this.isMega() || filter.isMega());
            this.setDoubleDeck(this.isDoubleDeck() || filter.isDoubleDeck());
            this.setFrigoTrailer(this.isFrigoTrailer() || filter.isFrigoTrailer());
            this.setIsolated(this.isIsolated() || filter.isIsolated());
            this.setLiftingRoof(this.isLiftingRoof() || filter.isLiftingRoof());
            this.setSecuritySensor(this.isSecuritySensor() || filter.isSecuritySensor());
            this.setSlidingRoof(this.isSlidingRoof() || filter.isSlidingRoof());
            this.setSuitableForHangingLoads(this.isSuitableForHangingLoads() || filter.isSuitableForHangingLoads());
            this.setSuitableForTrain(this.isSuitableForTrain() || filter.isSuitableForTrain());
            this.setTailLift(this.isTailLift() || filter.isTailLift());
            this.setXlCertificate(this.isXlCertificate() || filter.isXlCertificate());
        }
    }

}