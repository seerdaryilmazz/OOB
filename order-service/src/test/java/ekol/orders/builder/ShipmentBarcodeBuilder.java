package ekol.orders.builder;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.domain.ShipmentBarcode;


/**
 * Created by kilimci on 13/09/2017.
 */
public final class ShipmentBarcodeBuilder {
    private Long id;
    private Shipment shipment;
    private Integer indexNo;
    private String barcode;
    private boolean deleted;
    private UtcDateTime deletedAt;
    private UtcDateTime lastUpdated;
    private String lastUpdatedBy;

    private ShipmentBarcodeBuilder() {
    }

    public static ShipmentBarcodeBuilder aShipmentBarcode() {
        return new ShipmentBarcodeBuilder();
    }

    public ShipmentBarcodeBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ShipmentBarcodeBuilder withShipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public ShipmentBarcodeBuilder withIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
        return this;
    }

    public ShipmentBarcodeBuilder withBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public ShipmentBarcodeBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public ShipmentBarcodeBuilder withDeletedAt(UtcDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public ShipmentBarcodeBuilder withLastUpdated(UtcDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        return this;
    }

    public ShipmentBarcodeBuilder withLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public ShipmentBarcodeBuilder but() {
        return aShipmentBarcode().withId(id).withShipment(shipment).withIndexNo(indexNo).withBarcode(barcode)
                .withDeleted(deleted).withLastUpdated(lastUpdated).withLastUpdatedBy(lastUpdatedBy);
    }

    public ShipmentBarcode build() {
        ShipmentBarcode shipmentBarcode = new ShipmentBarcode();
        shipmentBarcode.setId(id);
        shipmentBarcode.setShipment(shipment);
        shipmentBarcode.setIndexNo(indexNo);
        shipmentBarcode.setBarcode(barcode);
        shipmentBarcode.setDeleted(deleted);
        shipmentBarcode.setDeletedAt(deletedAt);
        shipmentBarcode.setLastUpdated(lastUpdated);
        shipmentBarcode.setLastUpdatedBy(lastUpdatedBy);
        return shipmentBarcode;
    }
}
