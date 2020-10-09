package ekol.orders.transportOrder.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.transportOrder.domain.ShipmentBarcode;

import java.util.List;

/**
 * Created by kilimci on 13/09/2017.
 */
public interface ShipmentBarcodeRepository extends ApplicationRepository<ShipmentBarcode> {

    List<ShipmentBarcode> findByShipmentId(Long shipmentId);

    List<ShipmentBarcode> findByShipmentIdOrderByIndexNoAsc(Long shipmentId);

    ShipmentBarcode findByBarcode(String barcode);

    boolean existsByShipmentId(Long shipmentId);
}
