package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.ShipmentBarcode;
import ekol.orders.transportOrder.repository.ShipmentBarcodeRepository;
import ekol.orders.transportOrder.service.BarcodeService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kilimci on 13/09/2017.
 */
@RestController
@RequestMapping("/barcode")
public class ShipmentBarcodeController {

    @Autowired
    private BarcodeService barcodeService;

    @Value("${oneorder.barcode.directory}")
    private String directory;

    @Autowired
    private ShipmentBarcodeRepository shipmentBarcodeRepository;

    @RequestMapping(value = {"/{shipmentId}"}, method = RequestMethod.GET)
    public List<Map<String, String>> listForShipment(@PathVariable Long shipmentId) {
        List<ShipmentBarcode> barcodes = shipmentBarcodeRepository.findByShipmentIdOrderByIndexNoAsc(shipmentId);
        int total = barcodes.size();
        List<Map<String, String>> result = new ArrayList<>();
        barcodes.forEach(shipmentBarcode -> {
            Map<String, String> data = new HashMap<>();
            String packageNo = shipmentBarcode.getIndexNo() + "/" + total;
            data.put("packageNo", packageNo);
            data.put("barcode", shipmentBarcode.getBarcode());
            result.add(data);
        });
        return result;

    }


    @RequestMapping(value = {"/by-barcode/{barcode}"}, method = RequestMethod.GET)
    public Long retrieveShipmentIdByBarcode(@PathVariable String barcode) {
        ShipmentBarcode shipmentBarcode = shipmentBarcodeRepository.findByBarcode(barcode);

        if(shipmentBarcode == null) {
            return null;
        }

        return shipmentBarcode.getShipment().getId();

    }

    @RequestMapping(value = {"/view/{barcode}"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> viewBarcode(@PathVariable String barcode) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(
                FileUtils.readFileToByteArray(new File(directory + "/" + barcode + ".png"))
        );
    }

}
