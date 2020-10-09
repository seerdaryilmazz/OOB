package ekol.orders.transportOrder.controller;


import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.UnauthorizedAccessException;
import ekol.orders.order.service.LoadingMeterCalculator;
import ekol.orders.order.service.PayWeightCalculator;
import ekol.orders.transportOrder.domain.Shipment;
import ekol.orders.transportOrder.repository.ShipmentRepository;
import ekol.resource.oauth2.SessionOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shipment")
public class ShipmentApiController {

    private static final String COUNTER_KEY = "orderService.shipmentCode.counter";

    @Autowired
    private LoadingMeterCalculator loadingMeterCalculator;

    @Autowired
    private PayWeightCalculator payWeightCalculator;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private RedisAtomicLong shipmentCodeCounter;


    @Autowired
    private SessionOwner sessionOwner;

    @PostConstruct
    public void init() {
        /**
         * Dikkat: initialValue alan RedisAtomicLong constructor'ını kullanırsak
         * uygulama her açıldığında, sayaç initialValue değerinden başlıyor.
         * Bizim istediğimiz, uygulama her açıldığında sayacın kaldığı yerden devam etmesi.
         */
        shipmentCodeCounter = new RedisAtomicLong(COUNTER_KEY, redisConnectionFactory);
    }

    @RequestMapping(value = {"/new-shipment-code", "/new-shipment-code/"}, method = RequestMethod.GET)
    public String getNewShipmentCode() {
        return String.format("%06d", shipmentCodeCounter.incrementAndGet());
    }

    @RequestMapping(value = {"/calculate-ldm"}, method = RequestMethod.GET)
    public BigDecimal calculateLdm(@RequestParam BigDecimal length, @RequestParam BigDecimal width,
                                   @RequestParam Integer stackSize, @RequestParam int count) {
        return loadingMeterCalculator.calculateLoadingMeter(length, width, stackSize, count);
    }

    @RequestMapping(value = {"/calculate-volume"}, method = RequestMethod.GET)
    public BigDecimal calculateVolume(@RequestParam BigDecimal length, @RequestParam BigDecimal width,
                                      @RequestParam BigDecimal height, @RequestParam int count) {
        return loadingMeterCalculator.calculateVolume(length, width, height, count);
    }

    @RequestMapping(value = {"/calculate-volume-ldm"}, method = RequestMethod.GET)
    public Map<String, BigDecimal> calculateVolume(@RequestParam BigDecimal length, @RequestParam BigDecimal width,
                                                   @RequestParam(required = false) BigDecimal height,
                                                   @RequestParam(required = false) Integer stackSize,
                                                   @RequestParam int count) {
        Map<String, BigDecimal> result = new HashMap<>();
        if(length != null && width != null && height != null && count != 0){
            result.put("volume", loadingMeterCalculator.calculateVolume(length, width, height, count));
        }
        if(length != null && width != null && stackSize != null && count != 0){
            result.put("ldm", loadingMeterCalculator.calculateLoadingMeter(length, width, stackSize, count));
        }
        return result;
    }

    @RequestMapping(value = {"/calculate-pw"}, method = RequestMethod.GET)
    public BigDecimal calculatePW(@RequestParam BigDecimal weight, @RequestParam BigDecimal volume, @RequestParam BigDecimal ldm) {
        return payWeightCalculator.calculatePayWeight(weight, volume, ldm);
    }


    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public Shipment findWithDetailsById(@PathVariable Long id) {
        return shipmentRepository.findWithDetailsById(id);
    }



    @RequestMapping(value = "/having-my-subsidiaries/{shipmentId}", method = RequestMethod.GET)
    public Shipment findShipmentHavingMySubsidiaryById(@PathVariable Long shipmentId) {

        ekol.model.IdNamePair subsidiary;
        if(sessionOwner.getCurrentUser().getSubsidiaries() != null && sessionOwner.getCurrentUser().getSubsidiaries().size() > 0) {
            subsidiary = sessionOwner.getCurrentUser().getSubsidiaries().get(0);
        } else {
            throw new UnauthorizedAccessException("User does not have any subsidiary.");
        }
        return shipmentRepository.findOneByIdAndTransportOrderSubsidiaryId(shipmentId, subsidiary.getId());
    }

    @RequestMapping(value = "/code-by-id", method = RequestMethod.GET)
    public String findCodeById(@RequestParam(value = "id") Long id) {

        Shipment shipment = shipmentRepository.findOne(id);

        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment with specified id cannot be found: " + id);
        } else {
            return shipment.getCode();
        }
    }

    @RequestMapping(value = "/id-by-code", method = RequestMethod.GET)
    public Long findIdByCode(@RequestParam(value = "code") String code) {

        Shipment shipment = shipmentRepository.findByCode(code);

        if (shipment == null) {
            throw new ResourceNotFoundException("Shipment with specified code cannot be found: " + code);
        } else {
            return shipment.getId();
        }
    }
}
