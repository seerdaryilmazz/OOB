package ekol.orders.transportOrder.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import ekol.exceptions.ApplicationException;
import ekol.orders.order.domain.dto.response.location.WarehouseResponse;
import ekol.orders.order.service.LocationServiceClient;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.domain.ShipmentBarcode;
import ekol.orders.transportOrder.event.TripPlanCreatedEventMessage;
import ekol.orders.transportOrder.repository.ShipmentBarcodeRepository;
import ekol.orders.transportOrder.repository.ShipmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 13/09/2017.
 */
@Service
public class BarcodeService {

    @Value("${oneorder.barcode.directory}")
    private String directory;

    @Value("${oneorder.barcode.companyPrefix}")
    private String prefix;

    @Value("${oneorder.barcode.applicationIdentifier}")
    private String identifier;

    @Value("${oneorder.barcode.extensionDigit}")
    private String extensionDigit;

    @Value("${oneorder.barcode.width}")
    private int width;

    @Value("${oneorder.barcode.height}")
    private int height;

    @Value("${oneorder.barcode.imageFormat}")
    private String imageFormat;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentBarcodeRepository shipmentBarcodeRepository;

    @Autowired
    private LocationServiceClient locationServiceClient;

    private static final String serialNoKey = "oneorder:barcode:serialNo";

    private String calculateCheckDigit(String code){
        int total = 0;
        for(int i = 0; i < code.length(); i++){
            total += ((code.charAt(i) - 48) * (i % 2 == 0 ? 3 : 1));
        }
        return String.valueOf((10 - (total % 10)) % 10);
    }
    private String generateSerialNo(){
        final int serialLength = 9;
        Long nextSerial = redisTemplate.opsForValue().increment(serialNoKey, 1L);
        return StringUtils.right(StringUtils.leftPad(String.valueOf(nextSerial), serialLength, '0'), serialLength) ;
    }
    public String generateBarcode(){
        StringBuilder code = new StringBuilder(20);
        String serial = generateSerialNo();
        code.append(identifier).append(extensionDigit).append(prefix).append(serial);
        code.append(calculateCheckDigit(code.toString()));
        return code.toString();
    }

    public void saveBarcode(String barcode) {
        try {
            BitMatrix bitMatrix = new Code128Writer().encode(barcode, BarcodeFormat.CODE_128, width, height);
            MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, new FileOutputStream(
                    new File(directory + "/" + barcode + "." + imageFormat)));
        } catch (Exception e) {
            throw new ApplicationException("Error writing barcode", e);
        }

    }

    public void createAndSaveBarcodesFor(Long shipmentId) {
        if(shipmentRepository.exists(shipmentId)){
            createAndSaveBarcodesFor(shipmentRepository.findWithDetailsById(shipmentId));
        }
    }
    public void createAndSaveBarcodesFor(Shipment shipment) {
        List<ShipmentBarcode> shipmentBarcodes = new ArrayList<>();
        shipment.getShipmentUnits().forEach(shipmentUnit -> {
            shipmentUnit.getShipmentUnitPackages().forEach(shipmentUnitPackage -> {
                Integer count = shipmentUnitPackage.getCount();
                for(int i = 1; i <= count; i++){
                    String barcode = generateBarcode();
                    saveBarcode(barcode);
                    shipmentBarcodes.add(createShipmentBarcode(shipment, barcode, i));
                }
            });
        });

        shipmentBarcodeRepository.save(shipmentBarcodes);

    }

    private ShipmentBarcode createShipmentBarcode(Shipment shipment, String barcode, Integer index){
        ShipmentBarcode shipmentBarcode = new ShipmentBarcode();
        shipmentBarcode.setShipment(shipment);
        shipmentBarcode.setBarcode(barcode);
        shipmentBarcode.setIndexNo(index);
        return shipmentBarcode;
    }

    private boolean isWarehouseOwnedByEkol(Long locationId){
        WarehouseResponse warehouse = locationServiceClient.findWarehouseByLocationId(locationId);
        return warehouse != null && warehouse.isEkolWarehouse();
    }

    private boolean isShipmentHasBarcodes(Long shipmentId){
        return shipmentBarcodeRepository.existsByShipmentId(shipmentId);
    }

    @Transactional
    public void processTripPlanCreated(TripPlanCreatedEventMessage eventMessage){
        eventMessage.getTrips().forEach(trip -> {
            if(isWarehouseOwnedByEkol(trip.getToTripStop().getLocation().getId())){
                trip.getToTripStop().getTripOperations().forEach(tripOperation -> {
                    if(!isShipmentHasBarcodes(tripOperation.getTransport().getShipmentId())){
                        createAndSaveBarcodesFor(tripOperation.getTransport().getShipmentId());
                    }

                });
            }
        });
    }
}
