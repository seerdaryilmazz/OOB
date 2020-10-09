package ekol.orders.transportOrder.elastic.shipment.document;


import ekol.orders.transportOrder.domain.ShipmentUnit;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 16/08/2017.
 */
public class ShipmentUnitDocument {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Double)
    private Double grossWeight;

    @Field(type = FieldType.Double)
    private Double netWeight;

    @Field(type = FieldType.Double)
    private Double volume;

    @Field(type = FieldType.Double)
    private Double ldm;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String type;

    @Field(type = FieldType.Nested)
    private List<ShipmentPackageDocument> packages = new ArrayList<>();

    public static ShipmentUnitDocument createWith(ShipmentUnit shipmentUnit){
        ShipmentUnitDocument shipmentUnitDocument = new ShipmentUnitDocument();
        shipmentUnitDocument.setId(shipmentUnit.getId());
        shipmentUnitDocument.setGrossWeight(shipmentUnit.getTotalGrossWeightInKilograms().doubleValue());
        shipmentUnitDocument.setNetWeight(shipmentUnit.getTotalNetWeightInKilograms().doubleValue());
        shipmentUnitDocument.setLdm(shipmentUnit.getTotalLdm().doubleValue());
        shipmentUnitDocument.setVolume(shipmentUnit.getTotalVolumeInCubicMeters().doubleValue());
        shipmentUnitDocument.setType(shipmentUnit.getType().getName());
        shipmentUnit.getShipmentUnitPackages().forEach(
                shipmentUnitPackage -> shipmentUnitDocument.getPackages().add(ShipmentPackageDocument.createWith(shipmentUnitPackage)));

        return shipmentUnitDocument;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getLdm() {
        return ldm;
    }

    public void setLdm(Double ldm) {
        this.ldm = ldm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ShipmentPackageDocument> getPackages() {
        return packages;
    }

    public void setPackages(List<ShipmentPackageDocument> packages) {
        this.packages = packages;
    }
}
