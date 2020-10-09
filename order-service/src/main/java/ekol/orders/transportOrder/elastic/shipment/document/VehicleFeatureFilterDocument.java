package ekol.orders.transportOrder.elastic.shipment.document;


import ekol.orders.transportOrder.domain.VehicleFeature;
import ekol.orders.transportOrder.domain.VehicleRequirement;
import ekol.orders.transportOrder.dto.VehicleFeatureFilter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * Created by burak on 16/11/17.
 */
@Document(indexName = ShipmentDocument.INDEX_NAME)
@Setting(settingPath = "/elastic/ShipmentSettings.json")
public class VehicleFeatureFilterDocument {

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

    public static VehicleFeatureFilterDocument fromVehicleFeatureFilter(VehicleFeatureFilter filter) {
        VehicleFeatureFilterDocument document = new VehicleFeatureFilterDocument();

        if(filter != null) {
            document.setCurtainSider(filter.isCurtainSider());
            document.setBox(filter.isBox());
            document.setMega(filter.isMega());
            document.setDoubleDeck(filter.isDoubleDeck());
            document.setFrigoTrailer(filter.isFrigoTrailer());
            document.setIsolated(filter.isIsolated());
            document.setLiftingRoof(filter.isLiftingRoof());
            document.setSecuritySensor(filter.isSecuritySensor());
            document.setSlidingRoof(filter.isSlidingRoof());
            document.setSuitableForHangingLoads(filter.isSuitableForHangingLoads());
            document.setSuitableForTrain(filter.isSuitableForTrain());
            document.setTailLift(filter.isTailLift());
            document.setXlCertificate(filter.isXlCertificate());
        }
        return document;
    }

    public static VehicleFeatureFilterDocument fromTransportOrderRequirement(VehicleRequirement vehicleRequirements) {
        VehicleFeatureFilterDocument document = new VehicleFeatureFilterDocument();

        if(vehicleRequirements != null && vehicleRequirements.getVehicleFeatures() != null) {

            document.setCurtainSider(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.CURTAIN_SIDER));
            document.setBox(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.BOX_BODY));
            document.setMega(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.MEGA));
            document.setDoubleDeck(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.DOUBLE_DECK));
            document.setFrigoTrailer(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.FRIGO));
            document.setIsolated(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.ISOLATED));
            document.setLiftingRoof(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.LIFTING_ROOF));
            document.setSecuritySensor(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.SECURITY_SENSOR));
            document.setSlidingRoof(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.SLIDING_ROOF));
            document.setSuitableForHangingLoads(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.SUITABLE_FOR_HANGING_LOADS));
            document.setSuitableForTrain(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.SUITABLE_FOR_TRAIN));
            document.setTailLift(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.TAIL_LIFT));
            document.setXlCertificate(vehicleRequirements.getVehicleFeatures().contains(VehicleFeature.XL_CERTIFICATE));

        }
        return document;
    }

    public void merge(VehicleFeatureFilterDocument filter) {
        if(filter != null) {
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

}
