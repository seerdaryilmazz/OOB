package ekol.orders.transportOrder.elastic.shipment.document;


import ekol.orders.transportOrder.domain.ShipmentUnitPackage;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by kilimci on 17/08/2017.
 */

public class ShipmentPackageDocument {

    @Field(type = FieldType.Integer)
    private Integer count;

    @Field(type = FieldType.Double)
    private Double length;

    @Field(type = FieldType.Double)
    private Double width;

    @Field(type = FieldType.Double)
    private Double height;

    @Field(type = FieldType.Integer)
    private Integer stackSize;

    public static ShipmentPackageDocument createWith(ShipmentUnitPackage shipmentUnitPackage){
        ShipmentPackageDocument shipmentPackageDocument = new ShipmentPackageDocument();
        shipmentPackageDocument.setWidth(shipmentUnitPackage.getWidthInCentimeters().doubleValue());
        shipmentPackageDocument.setHeight(shipmentUnitPackage.getHeightInCentimeters().doubleValue());
        shipmentPackageDocument.setLength(shipmentUnitPackage.getLengthInCentimeters().doubleValue());
        shipmentPackageDocument.setCount(shipmentUnitPackage.getCount());
        shipmentPackageDocument.setStackSize(shipmentUnitPackage.getStackSize());
        return shipmentPackageDocument;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getStackSize() {
        return stackSize;
    }

    public void setStackSize(Integer stackSize) {
        this.stackSize = stackSize;
    }
}
